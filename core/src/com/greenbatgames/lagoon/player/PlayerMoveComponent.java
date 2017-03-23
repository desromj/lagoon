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

    private boolean grounded;
    private boolean crouching;
    private boolean facingRight;

    private float cannotJumpFor;

    public PlayerMoveComponent(Player player) {
        super(player);
        grounded = true;
        crouching = false;
        facingRight = true;
        cannotJumpFor = 0.0f;
    }

    @Override
    public boolean update(float delta) {

        // Handle jumping timer and recovery
        cannotJumpFor -= delta;
        if (!canJump()) return true;

        // Grab the player body and do physics calculations
        Body body = player().getBody();

        // Determine our horizontal movement based on whether we're on the ground or in the air
        if (isOnGround()) {
            if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT)) {
                body.setLinearVelocity(
                        Constants.PLAYER_MOVE_SPEED.x,
                        body.getLinearVelocity().y);
            }

            if (Gdx.input.isKeyPressed(Constants.KEY_LEFT)) {
                body.setLinearVelocity(
                        -Constants.PLAYER_MOVE_SPEED.x,
                        body.getLinearVelocity().y);
            }

            // TODO: Dampen horizontal movement if we do not have a move button held
            if (!player().isMoveButtonHeld()) {
                body.setLinearVelocity(
                        0f,
                        body.getLinearVelocity().y);
            }
        }

        // Set our faced direction based on x velocity
        if (body.getLinearVelocity().x > 0.1f)
            facingRight = true;
        else if (body.getLinearVelocity().x < -0.1f)
            facingRight = false;

        return true;
    }

    /**
     * Set grounded to true and initialize a jump recovery period, during which
     * the player is not allowed to jump again
     */
    public void land() {
        if (grounded) { return; }
        grounded = true;
        cannotJumpFor = Constants.PLAYER_JUMP_RECOVERY;
    }

    /**
     * Cannot jump if we are already airborne, otherwise set grounded to false
      */
    public void jump() {
        if (!grounded) return;
        grounded = false;
    }

    /*
        Getters and Setters
     */

    public boolean canJump() { return cannotJumpFor <= 0f; }

    public boolean isOnGround() { return grounded; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isCrouching() { return crouching; }
}
