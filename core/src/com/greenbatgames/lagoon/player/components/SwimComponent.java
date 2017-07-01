package com.greenbatgames.lagoon.player.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Utils;

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
public class SwimComponent extends PlayerComponent {

    private boolean swimming;
    private boolean submerged;
    private float breathRemaining;
    private float nextStrokeIn;
    private float surfaceYpoint;

    private Vector2 movementVector;

    public SwimComponent(Player player) {
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

        // Handle checking if we should be submerged or treading the water surface
        if (Utils.almostEqualTo(
                body.getTransform().getPosition().y + Constants.PLAYER_SWIM_FIXTURE_Y_OFFSET,
                surfaceYpoint / Constants.PTM,
                Constants.PLAYER_WATER_ENTRY_VARIANCE
        )) {
            // Allow diving beneath the water
            if (Gdx.input.isKeyPressed(Constants.KEY_DOWN)) {
                submerged = true;
            } else {
                submerged = false;
                body.setTransform(
                        body.getTransform().getPosition().x,
                        (surfaceYpoint / Constants.PTM) - Constants.PLAYER_SWIM_FIXTURE_Y_OFFSET,
                        body.getAngle()
                );
            }

            body.setLinearVelocity(
                    body.getLinearVelocity().x,
                    submerged ? body.getLinearVelocity().y : 0f);
        } else {
            submerged = true;
        }

        // Handle jumping out of the water, if not submerged
        if (!submerged) {

            breathRemaining = Constants.PLAYER_HOLD_BREATH_TIME;

            if (Gdx.input.isKeyJustPressed(Constants.KEY_JUMP)) {
                player().mover().doVerticalJump(body);
                stopSwimming();
                return true;
            }
        } else {
            breathRemaining -= delta;
            if (breathRemaining < 0) {
                player().health().damage(1, false);
                breathRemaining = 2f;
            }
        }

        // Handle strokes in the water
        nextStrokeIn -= delta;

        boolean
                pressedLeft = Gdx.input.isKeyPressed(Constants.KEY_LEFT),
                pressedRight = Gdx.input.isKeyPressed(Constants.KEY_RIGHT),
                pressedUp = Gdx.input.isKeyPressed(Constants.KEY_UP),
                pressedDown = Gdx.input.isKeyPressed(Constants.KEY_DOWN);

        if (nextStrokeIn < 0 && (player().isMoveButtonHeld() || player().isUpOrDownButtonHeld())) {
            // Reset, scale, then normalize movement based on input
            nextStrokeIn = Constants.PLAYER_STROKE_PERIOD;
            movementVector.set(0,0);

            if (pressedUp) { movementVector.add(0,1); }
            if (pressedDown) { movementVector.add(0,-1); }
            if (pressedRight) { movementVector.add(1,0); }
            if (pressedLeft) { movementVector.add(-1,0); }

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
        } else {
            // TODO: Change the player's movement direction if any movement buttons are pressed and the player is drifting

        }

        // Dampen movement while in water
        player().getBody().setLinearVelocity(
                player().getBody().getLinearVelocity().x * Constants.PLAYER_WATER_MOVEMENT_DAMPEN,
                player().getBody().getLinearVelocity().y * Constants.PLAYER_WATER_MOVEMENT_DAMPEN
        );

        return false;
    }

    public void draw(Batch batch, float parentAlpha) {
        // TODO: Draw the breath meter here when ready
    }
}
