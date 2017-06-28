package com.greenbatgames.lagoon.player.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.greenbatgames.lagoon.physics.Climbable;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

/**
 * A player can wedge themselves into a 'chimney' structure which is 2 tiles wide.
 * While wedged, gravity has no effect, and instead the player can slowly move up or down.
 *
 * TODO: The player will automatically climb if they come to the top of the chimney with an open edge
 */
public class WedgeComponent extends PlayerComponent {

    public static final String TAG = WedgeComponent.class.getSimpleName();

    private boolean wedged;
    private WedgeRaycastCallback raycast;
    private Vector2 from;
    private Vector2 to;

    public WedgeComponent(Player player) {
        super(player);
        wedged = false;
        raycast = new WedgeRaycastCallback();
        from = new Vector2();
        to = new Vector2();
    }

    @Override
    public boolean update(float delta) {

        // Trigger the wedged flag if we are not wedged and just pressed the key
        if (!wedged) {
            if (Gdx.input.isKeyPressed(Constants.KEY_WEDGE) && canWedge()) {
                this.wedged = true;
            } else {
                return true;
            }
        } else {
            // If we are wedged, check to see if we should remain as such
            if (!canWedge() || !Gdx.input.isKeyPressed(Constants.KEY_WEDGE)) {
                wedged = false;
            } else {

                // Set the player x position to the midpoint of the raycast... or left, whatever works
                player().setPosition(
                        (raycast.getMidpoint() * Constants.PTM) - player().getWidth() / 2f,
                        player().getY()
                );

                // Handle slow up/down movement while wedged
                if (Gdx.input.isKeyPressed(Constants.KEY_UP)) {
                    player().getBody().setLinearVelocity(
                            0f,
                            Constants.PLAYER_WEDGE_MOVE_SPEED
                    );
                } else if (Gdx.input.isKeyPressed(Constants.KEY_DOWN)) {
                    player().getBody().setLinearVelocity(
                            0f,
                            -Constants.PLAYER_WEDGE_MOVE_SPEED
                    );
                } else {
                    player().getBody().setLinearVelocity(
                            0f,
                            0f
                    );
                }
            }
        }

        // Continue normal updates if we are not wedged. Otherwise, terminate
        player().getBody().setGravityScale(wedged ? 0 : 1);
        return !wedged;
    }

    /**
     * Does a raycast from the vertical middle of the player's crouch collider, left to right
     * @return true if the player crouch collider is intersecting exactly one Climbable object on
     *              the left side and exactly one Climbable object the right side. False otherwise
     */
    private boolean canWedge() {
        // Set from and to values for the raycast, with a few tolerances
        float width = player().getWidth() * 3f;
        float height = player().getHeight() * 0.5f;

        from.set(
                (player().getMiddleX() - width) / Constants.PTM,
                (player().getMiddleY() + height) / Constants.PTM
        );

        to.set(
                (player().getMiddleX() + width) / Constants.PTM,
                (player().getMiddleY() + height) / Constants.PTM
        );

        // Do the raycast, resetting the state of the raycast immediately prior
        raycast.reset();
        GameScreen.level().getWorld().rayCast(raycast, from, to);
        return raycast.canWedge();
    }

    public boolean isWedged() {
        return wedged;
    }

    /**
     * Inner Raycast Callback class: track the number of hits for the wedge using canWedge() and reset()
     */
    class WedgeRaycastCallback implements RayCastCallback {

        private Vector2 first, second;
        private boolean firstHit, secondHit;

        public WedgeRaycastCallback() {
            super();
            first = new Vector2();
            second = new Vector2();
            firstHit = false;
            secondHit = false;
        }

        public boolean canWedge() {
            return firstHit && secondHit;
        }

        public void reset() {
            firstHit = false;
            secondHit = false;
        }

        public float getMidpoint() {
            return (first.x + second.x) / 2f;
        }

        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if (fixture.getBody().getUserData() instanceof Climbable) {
                if (!firstHit) {
                    firstHit = true;
                    first.set(point.x, point.y);
                } else if (!secondHit) {
                    secondHit = true;
                    second.set(point.x, point.y);
                }
            }

            return 1;
        }
    }
}
