package com.greenbatgames.lagoon.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.lagoon.util.Constants;

/**
 * Handles all logic for moving the player while swimming. There are four different
 * states the player can be in when determining how they move in the water:
 *
 * 1) Treading water
 *      Moving left and right in strokes
 *      Can jump out of the water
 *      Can climb out of the water
 *      Can move down to dive
 *
 * 2) Diving
 *      Stroke to gain speed. Speed diminishes over time until the next stroke
 *      Move in the chosen direction at the deteriorating speed
 *      Decrement breath counter
 *
 * 3) Surfacing
 *      Reset breath tracker
 *      Change to Treading water logic
 *
 * 4) Exiting water
 *      Exit water by either: jumping or climbing
 *      Set swimming to false, until the player enters another body of water
 *
 * While swimming, the max Y value of the player is constrained to the top of the water, unless
 * the player jumps or climbs out.
 */
public class PlayerSwimComponent extends PlayerComponent {

    private boolean swimming;
    private boolean submerged;
    private float breathRemaining;
    private float nextStrokeIn;
    private float surfaceYpoint;

    private Vector2 movementVector;

    public PlayerSwimComponent(Player player) {
        super(player);
        swimming = false;
        submerged = false;
        breathRemaining = Constants.PLAYER_HOLD_BREATH_TIME;
        nextStrokeIn = 0f;
        surfaceYpoint = 0f;
        movementVector = new Vector2();
    }

    public void treadWater(float surfaceYpoint) {
        swimming = true;
        submerged = false;
        breathRemaining = Constants.PLAYER_HOLD_BREATH_TIME;
        this.surfaceYpoint = surfaceYpoint;
        player().getBody().setGravityScale(0f);
    }

    public void stopSwimming() {
        swimming = false;
        submerged = false;
        breathRemaining = Constants.PLAYER_HOLD_BREATH_TIME;
        player().getBody().setGravityScale(1f);
    }

    @Override
    public boolean update(float delta) {

        // Ensure our state is set correctly if we are not swimming
        if (!swimming) {
            stopSwimming();
            return true;
        }

        // constrain the player Y position to the top of the body of water
        Body body = player().getBody();

        if (body.getTransform().getPosition().y > surfaceYpoint / Constants.PTM) {
            body.setTransform(
                    body.getTransform().getPosition().x,
                    surfaceYpoint / Constants.PTM,
                    body.getAngle()
            );
        }

        // Check if we're submerged in water or not
        submerged = body.getTransform().getPosition().y != surfaceYpoint / Constants.PTM;

        // Handle jumping out of the water, if not submerged
        if (!submerged) {

            breathRemaining = Constants.PLAYER_HOLD_BREATH_TIME;

            if (player().mover().canJump() && Gdx.input.isKeyJustPressed(Constants.KEY_JUMP)) {
                player().mover().doVerticalJump(body);
                stopSwimming();
                return true;
            }
        } else {
            breathRemaining -= delta;
        }

        // Handle strokes in the water
        nextStrokeIn -= delta;

        if (nextStrokeIn < 0 && player().isJumpButtonHeld()) {
            // Reset, scale, then normalize movement based on input
            movementVector.set(0,0);

            if (Gdx.input.isKeyPressed(Constants.KEY_UP)) {
                movementVector.add(0,1);
            }
            if (Gdx.input.isKeyPressed(Constants.KEY_DOWN)) {
                movementVector.add(0,-1);
            }
            if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT)) {
                movementVector.add(1,0);
            }
            if (Gdx.input.isKeyPressed(Constants.KEY_LEFT)) {
                movementVector.add(-1,0);
            }

            if (movementVector.len() > 0) {
                movementVector.nor();
            }

            movementVector.scl(Constants.PLAYER_STROKE_MAGNITUDE);

            // Apply the magitude to stroke in that direction
            player().getBody().applyLinearImpulse(
                    movementVector.x,
                    movementVector.y,
                    player().getX(),
                    player().getY(),
                    true
            );
        }

        return false;
    }
}
