package com.greenbatgames.lagoon.enemy.behaviours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.lagoon.ai.B2dLocation;
import com.greenbatgames.lagoon.enemy.EnemyBehavior;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Bats are dormant until activated
 *
 * When activated, bats will fly around the player, trying to stay
 * above them (combination of Pursue and Evade, switch between the two based on proximity)
 *
 * Gravity needs to be in effect, and reversed, based on the position difference between
 * the bat and its target (maybe a scale flip-flopping between 0.2 and -0.2 or something)
 *
 * Occasionally, bats will swoop in and attack the player, then
 * go back to hovering above their head (Arrive behaviour, large acceleration, large decel radius, eventual decel)
 *
 * Once the Arrive behaviour has been reached, reset the swooping attack timer before trying again
 *
 * Bats need to be within a certain range to make a swooping attack,
 * and can only make the attack a certain length of time after being within range
 *
 * TODO: finish the logic here
 */
public class BatBehaviour extends EnemyBehavior {

    private boolean active;
    private float swoopTimer;
    private float swoopDuration;

    private Face watch;
    private Pursue pursue;
    private Evade evade;
    private Arrive swoop;

    public BatBehaviour(PhysicsBody parent) {
        super(parent);
        active = false;
        swoopTimer = 0f;
        swoopDuration = 0f;
        watch = null;
        pursue = null;
        evade = null;
        swoop = null;
    }

    @Override
    protected void makeBehavior() {
        steerable = parent.getSteerable();
        watch = new Face<>(steerable, Utils.player().getSteerable());
        pursue = new Pursue<>(steerable, Utils.player().getSteerable());
        evade = new Evade<>(steerable, Utils.player().getSteerable());
        swoop = new Arrive<Vector2>(steerable)
                .setArrivalTolerance(0.5f)
                .setDecelerationRadius(10f);
        steerable.setBehavior(watch);
    }

    @Override
    protected void useSteering(float delta) {

        // Check whether or not to activate the bat, otherwise just return
        if (!checkForActivation()) {
            parent.getBody().setGravityScale(-0.1f);
            parent.getBody().setLinearVelocity(0f, 0f);
            return;
        }

        parent.getBody().setAwake(true);

        // If the current behaviour is Arrive, complete the arrive until not moving
        if (steerable.getBehavior().equals(swoop)) {
            // If the Arrive behaviour is complete, proceed with the normal Pursue/Evade loop
            swoopDuration -= delta;
            if (swoopDuration <= 0f) {
                swoopTimer = 0;
                pursue();
            }
        } else {
            // If active...
            //      if within swoop range, increment the swoop timer
            //          If the swoop timer is high enough, create a new Arrive behaviour at a set distance towards the target
            //      set behaviour to either pursue or evade based on distance
            //      set gravity scale to positive or negative based on target Y position
            float targetDistance = parent.getGamePosition().dst(Utils.player().getGamePosition());

            if (targetDistance < Constants.BAT_SWOOP_RANGE) {
                swoopTimer += delta;
                if (swoopTimer >= Constants.BAT_SWOOP_DELAY) {
                    swoop();
                    return;
                }
            } else {
                swoopTimer = 0;
            }

            // Set behaviour to pursue or evade
            if (targetDistance > Constants.BAT_PURSUE_WHEN_DISTANCE) {
                pursue();
            } else if (targetDistance < Constants.BAT_EVADE_WHEN_DISTANCE) {
                evade();
            }

            // Set gravity scale based on Y distance from target (player)
            float yDiff = parent.getY() - Utils.player().getY();

            if (yDiff < Constants.WORLD_WIDTH / 12f) {
                parent.getBody().setGravityScale(-2f);
            } else {
                parent.getBody().setGravityScale(1.2f);
            }
        }

        steerable.update(delta);
    }


    /**
     * Activate the bat if the player is visible (raycast not blocked, of a certain range)
     */
    private boolean checkForActivation() {
        if (!active) {
            // Only do the raycast if not active and player is within range of the bat
            float distance = parent.getGamePosition().dst(Utils.player().getGamePosition());

            if (distance > Constants.BAT_ACTIVATION_DISTANCE) {
                return false;
            }

            List<Boolean> nonPlayerHits = new LinkedList<>();

            // Do the raycast and activate if there are no obstacles in the way
            GameScreen.level().getWorld().rayCast((fixture, point, normal, fraction) -> {
                if (!(fixture.getBody().getUserData() instanceof Player)) {
                    nonPlayerHits.add(true);
                }

                return 1;
            },
                    parent.getBody().getPosition(),
                    Utils.player().getBody().getPosition());

            if (nonPlayerHits.size() > 0) {
                Gdx.app.log("Activation", "BLOCKED BY OBSTACLE");
                return false;
            }

            Gdx.app.log("Activation", "ACTIVATED");
            active = true;
        }

        return true;
    }


    private void pursue() {
        if (!(steerable.getBehavior().equals(pursue)) || steerable.getBehavior() instanceof Evade) {
            Gdx.app.log("Behaviour", "Triggering PURSUE");
            steerable.setBehavior(pursue);
        }
    }


    private void evade() {
        if (!(steerable.getBehavior().equals(evade))) {
            Gdx.app.log("Behaviour", "Triggering EVADE");
            steerable.setBehavior(evade);
        }
    }


    private void swoop() {
        if (!(steerable.getBehavior().equals(swoop))) {
            Gdx.app.log("Behaviour", "Triggering SWOOP");

            // Get the angle to move, based on angle between the parent and the target
            Vector2 direction = new Vector2(Utils.player().getBody().getPosition());
            float angle = direction.sub(parent.getBody().getPosition()).angleRad();

            // Set the length then angle of direction to be used as a target point for the swoop behaviour
            direction.set(
                    parent.getBody().getPosition().x + Constants.BAT_SWOOP_DISTANCE * MathUtils.cos(angle),
                    parent.getBody().getPosition().y + Constants.BAT_SWOOP_DISTANCE * MathUtils.sin(angle));

            Gdx.app.log("Swoop Target", String.format(Locale.CANADA,
                    "(%.2f, %.2f)",
                    direction.x,
                    direction.y));

            Gdx.app.log("Player Body Position", String.format(Locale.CANADA,
                    "(%.2f, %.2f)",
                    Utils.player().getBody().getPosition().x,
                    Utils.player().getBody().getPosition().y));

            // TODO: Set all parameters here as required for the swoop attack
            parent.getBody().setGravityScale(0f);
            parent.getBody().setLinearVelocity(0f, 0f);

            steerable.setBehavior(swoop
                    .setTarget(new B2dLocation(direction))
            );

            swoopDuration = Constants.BAT_SWOOP_TIME;
        }
    }
}
