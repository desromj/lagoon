package com.greenbatgames.lagoon.player.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.greenbatgames.lagoon.physics.Climbable;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Quiv on 23-02-2017.
 *
 * Responsible for handling keyboard input and general movement of the Player.
 *
 * Movement options include:
 *      - moving left/right
 *      - jumping
 *      - climbing
 *      - crawling
 *      - swimming
 */
public class MoveComponent extends PlayerComponent {

    public static final String TAG = MoveComponent.class.getSimpleName();

    private int numFootContacts;
    private boolean crouching;
    private boolean canUncrouch;
    private boolean facingRight;
    private boolean knockedBack;

    private float cannotJumpFor;
    private float cannotMoveFor;
    private float ignoreGroundClingFor;

    private Vector2 previousVelocity;


    public MoveComponent(Player player) {
        super(player);
    }


    @Override
    public void init() {
        numFootContacts = 0;
        crouching = false;
        canUncrouch = false;
        facingRight = true;
        knockedBack = false;
        cannotJumpFor = 0.0f;
        cannotMoveFor = 0.0f;
        ignoreGroundClingFor = 0.0f;
        previousVelocity = new Vector2();
    }


    @Override
    public boolean update(float delta) {
        cannotJumpFor -= delta;
        ignoreGroundClingFor -= delta;

        if (knockedBack) {
            cannotMoveFor -= delta;
            knockedBack = cannotMoveFor > 0f;
            return false;
        }

        Body body = player().getBody();
        body.setGravityScale(isOnGround() ? 0 : 1);

        // Handle jumping logic - cancel y momentum then apply impulse
        if (canJump() && Gdx.input.isKeyJustPressed(Constants.KEY_JUMP)) {
            doVerticalJump(body);
        }

        // Toggle crouching logic
        if (Gdx.input.isKeyJustPressed(Constants.KEY_CROUCH)) {
            // check if our colliding boxes will allow us to crouch or stand up
            if (crouching) {
                setCanUncrouch(true);

                GameScreen.level().getWorld().rayCast(((fixture, point, normal, fraction) -> {
                        if (!fixture.isSensor() && fixture.getBody().getUserData() != player()) {
                            setCanUncrouch(false);
                            Gdx.app.log(TAG, "Blocked by fixture: " + fixture.getBody().getUserData());
                            return 0;
                        }
                        return 1;
                }),
                        player().getMiddleX() / Constants.PTM,
                        (player().getY()) / Constants.PTM,
                        player().getMiddleX() / Constants.PTM,
                        (player().getY() + player().getHeight()*2.2f) / Constants.PTM);

                if (canUncrouch) {
                    crouching = !crouching;
                    setCanUncrouch(false);
                }
            } else {
                crouching = true;
            }
        }

        // Handle left/right movement on the ground and in the air
        if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT)) {
            body.setLinearVelocity(
                    (isCrouching()) ? Constants.PLAYER_CROUCH_MOVE_SPEED.x : Constants.PLAYER_MOVE_SPEED.x,
                    body.getLinearVelocity().y);
        }

        if (Gdx.input.isKeyPressed(Constants.KEY_LEFT)) {
            body.setLinearVelocity(
                    (isCrouching()) ? -Constants.PLAYER_CROUCH_MOVE_SPEED.x : -Constants.PLAYER_MOVE_SPEED.x,
                    body.getLinearVelocity().y);
        }

        // TODO: Dampen horizontal movement if we do not have a move button held
        if (!player().isMoveButtonHeld()) {
            body.setLinearVelocity(
                    0f,
                    body.getLinearVelocity().y);
        }

        // Set our faced direction based on x velocity
        if (body.getLinearVelocity().x > 0.1f)
            facingRight = true;
        else if (body.getLinearVelocity().x < -0.1f)
            facingRight = false;

        // Cling the player to the ground, if necessary
        if (!crouching && ignoreGroundClingFor < 0 && isOnGround()) {
            clingToGround();
        }

        // Determine if the player should take fall damage or not
        if (!Utils.almostEqualTo(previousVelocity.y, 0, 0.001f)) {
            if (previousVelocity.y - body.getLinearVelocity().y < Constants.FALL_VELOCITY_DAMAGE_THRESHOLD) {
                player().health().damage(Constants.FALL_DAMAGE, true);
            }
        }

        previousVelocity.set(body.getLinearVelocity());

        return true;
    }


    /**
     * Cling the player to the ground if the player is grounded. The bottom of the circle
     * contact should be positioned at the contact point of a raycast straight down.
     * Only the player Y position needs to be considered here
     */
    private void clingToGround() {
        List<Vector2> hits = new LinkedList<>();

        RayCastCallback callback = ((fixture, point, normal, fraction) -> {
            if (
                    fixture.getBody().getUserData() instanceof Climbable
                    && !fixture.isSensor()
                    && fixture.getBody().getUserData() != player()) {
                hits.add(new Vector2(point.x, point.y));
                return 0;
            }
            return 1;   // Continue with raycast otherwise
        });

        // Short on left side
        GameScreen.level().getWorld().rayCast(callback,
                player().getX() / Constants.PTM,
                (player().getY() + player().getHeight() / 2f) / Constants.PTM,
                player().getX() / Constants.PTM,
                (player().getY() - player().getHeight() / 4f) / Constants.PTM);

        // Short on right side
        GameScreen.level().getWorld().rayCast(callback,
                (player().getX() + player().getWidth()) / Constants.PTM,
                (player().getY() + player().getHeight() / 2f) / Constants.PTM,
                (player().getX() + player().getWidth()) / Constants.PTM,
                (player().getY() - player().getHeight() / 4f) / Constants.PTM);

        // Ground Sensor in middle - lowers down a bit further
        GameScreen.level().getWorld().rayCast(callback,
                player().getMiddleX() / Constants.PTM,
                (player().getY() + player().getHeight() / 2f) / Constants.PTM,
                player().getMiddleX() / Constants.PTM,
                (player().getY() - player().getHeight() / 3f) / Constants.PTM);

        try {
            Vector2 highest = hits.stream()
                    .max((f1, f2) -> Float.compare(f1.y, f2.y))
                    .get();

            player().setGamePosition(
                    player().getX(),
                    highest.y * Constants.PTM);
        } catch (Exception ex) {}

        player().getBody().setLinearVelocity(
                player().getBody().getLinearVelocity().x,
                0f
        );
    }


    // Apply an impulse to the player body to jump vertically
    public void doVerticalJump(Body body) {
        body.setLinearVelocity(
                body.getLinearVelocity().x,
                0f
        );
        body.applyLinearImpulse(
                Constants.PLAYER_JUMP_IMPULSE.x,
                Constants.PLAYER_JUMP_IMPULSE.y,
                player().getX(),
                player().getY(),
                true
        );
        ignoreGroundClingFor = 0.25f;
    }


    /**
     * Set grounded to true and initialize a jump recovery period, during which
     * the player is not allowed to jump again
     */
    public void land() {
        if (isOnGround()) {
            return;
        }
        cannotJumpFor = Constants.PLAYER_JUMP_RECOVERY;
    }


    public void decrementNumFootContacts() {
        numFootContacts = (numFootContacts == 0) ? 0 : numFootContacts - 1;
    }


    public void incrementNumFootContacts() {
        if (!isOnGround()) {
            land();
        }
        numFootContacts++;
    }


    public void applyKnockback() {
        knockedBack = true;
        cannotMoveFor = Constants.PLAYER_KNOCKBACK_TIME;
        Body body = player().getBody();

        // Cancel momentum, them apply knockback impulse
        body.setLinearVelocity(
                0f,
                0f
        );
        body.applyLinearImpulse(
                (facingRight) ? -Constants.PLAYER_KNOCKBACK_IMPULSE.x : Constants.PLAYER_KNOCKBACK_IMPULSE.x,
                Constants.PLAYER_KNOCKBACK_IMPULSE.y,
                player().getX() / Constants.PTM,
                player().getY() / Constants.PTM,
                true
        );
    }


    public void resetNumFootContacts() {
        this.numFootContacts = 0;
    }

    /*
        Getters and Setters
     */

    public boolean canJump() { return !isCrouching() && isOnGround() && cannotJumpFor <= 0f; }
    public void setCrouching(boolean crouching) { this.crouching = crouching; }
    public void setCanUncrouch(boolean val) { this.canUncrouch = val; }
    public boolean isBeingKnockedBack() { return knockedBack; }

    public boolean isOnGround() { return numFootContacts > 0; }
    public boolean isMovingUp() { return player().getBody().getLinearVelocity().y > 0; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isCrouching() { return crouching; }
}
