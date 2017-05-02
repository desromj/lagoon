package com.greenbatgames.lagoon.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.lagoon.util.Constants;

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
public class PlayerMoveComponent extends PlayerComponent {

    public static final String TAG = PlayerMoveComponent.class.getSimpleName();

    private int numFootContacts;
    private boolean crouching;
    private boolean facingRight;

    private float cannotJumpFor;

    public PlayerMoveComponent(Player player) {
        super(player);
        numFootContacts = 0;
        crouching = false;
        facingRight = true;
        cannotJumpFor = 0.0f;
    }

    @Override
    public boolean update(float delta) {
        cannotJumpFor -= delta;

        Body body = player().getBody();

        // Handle jumping logic - cancel y momentum then apply impulse
        if (canJump() && Gdx.input.isKeyJustPressed(Constants.KEY_JUMP)) {
            doVerticalJump(body);
        }

        // Toggle crouching logic
        if (Gdx.input.isKeyJustPressed(Constants.KEY_CROUCH)) {
            crouching = !crouching;
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

        return true;
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

    /*
        Getters and Setters
     */

    public boolean canJump() { return !isCrouching() && isOnGround() && cannotJumpFor <= 0f; }
    public void setCrouching(boolean crouching) { this.crouching = crouching; }

    public boolean isOnGround() { return numFootContacts > 0; }
    public boolean isMovingUp() { return player().getBody().getLinearVelocity().y > 0; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isCrouching() { return crouching; }
}
