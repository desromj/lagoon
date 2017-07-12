package com.greenbatgames.lagoon.player.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.greenbatgames.lagoon.physics.Climbable;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Enums;

public class ClimbComponent extends PlayerComponent {
    public static final String TAG = ClimbComponent.class.getSimpleName();

    /** The point where the player will grab and pivot around while climbing */
    private Vector2 gripPoint;

    private float climbTimeLeft;
    private boolean climbing;
    private boolean climbingRight;

    public ClimbComponent(Player player) {
        super(player);
    }


    @Override
    public void init() {
        gripPoint = new Vector2();
        climbTimeLeft = 0.0f;
        climbing = false;
        climbingRight = false;
    }


    @Override
    public boolean update(float delta) {

        climbTimeLeft -= delta;
        if (climbTimeLeft <= 0f) {
            climbing = false;
        }

        if (climbing) {
            return false;
        }

        if (!player().isClimbButtonHeld()) {
            return true;
        }

        // At this point, we are not climbing and the climb button is being held

        /*
            The AABB Query gets the query in terms of the climbing fixture, which is the body and these coordinates:
                    b2Unit * 1.5f, b2Unit * 0.5f,
                    b2Unit * 1.5f, b2Unit * 4f,
                    -b2Unit * 1.5f, b2Unit * 4f,
                    -b2Unit * 1.5f, b2Unit * 0.5f
          */
        float b2Unit = Constants.PLAYER_RADIUS / Constants.PTM;
        Body playerBody = player().getBody();

        GameScreen.level().getWorld().QueryAABB(
                getQuery(),
                playerBody.getPosition().x - 1.5f * b2Unit,
                playerBody.getPosition().y + 0.5f * b2Unit,
                playerBody.getPosition().x + 1.5f * b2Unit,
                playerBody.getPosition().y + 4.0f * b2Unit
        );

        return !climbing;
    }


    private QueryCallback getQuery() {
        return fixture -> {
            if (!(fixture.getBody().getUserData() instanceof Climbable)) {
                return true;
            }

            float[] verts = ((Climbable) fixture.getBody().getUserData()).getVerts();
            Vector2 checkPoint = new Vector2();
            Fixture playerFix = player().getFixture(Enums.PlayerFixtures.CLIMB_SENSOR);

            for (int i = 0; i < verts.length; i += 2) {
                checkPoint.set(
                        fixture.getBody().getPosition().x + verts[i],
                        fixture.getBody().getPosition().y + verts[i + 1]);

                // Only test vertices which are within the player climbing fixture
                if (!playerFix.testPoint(checkPoint.x, checkPoint.y)) {
                    continue;
                }

                // fail if any of the y values of adjacent points are taller than the current point
                try {
                    if (verts[i - 1] > verts[i + 1] || verts[i + 3] > verts[i + 1]) {
                        continue;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    continue;
                }

                // Otherwise scale and set our grip point
                checkPoint.scl(Constants.PTM);
                player().climber().startClimbing(checkPoint);
                return false;
            }

            return true;
        };
    }


    /**
     * Starts a climb towards the passed grip point. If the grip point is to the right
     * of the player, the player's bottom-left edge will be hoisted on top. If the
     * grip point is to the left, the player's bottom-right edge will be moved on top.
     * @param gripPoint The point to make the player climb on top of
     */
    public void startClimbing(Vector2 gripPoint) {

        // return immediately if we are already climbing
        if (climbing) return;
        this.gripPoint.set(gripPoint.x, gripPoint.y);

        // Determine if we're climbing left or right
        if (gripPoint.x < player().getX())
            climbingRight = false;
        else
            climbingRight = true;

        // Add gripPoint offset of 1/2 player width based on if we're climbing left or right
        this.gripPoint.x += (climbingRight) ? -player().getWidth() / 2f : player().getWidth() / 2f;

        /*
            To start a new climb, determine the start time of the animation based on how high
            up the ledge the player grabs on.
            Top of player hitbox =        RUBY_CLIMB_TIME * RUBY_MAX_CLIMB_RATIO
            Bottom of player hitbox =     RUBY_CLIMB_TIME * RUBY_MIN_CLIMB_RATIO
            calculation for the timer ratio:
                    top of player hitbox - grip point Y
            100% -  -----------------------------------
                              player height
            The top of the player hitbox must also contain the range of the gripY point
        */
        float timeRatio = 1f - (player().getTop() - gripY()) / player().getHeight();
        timeRatio = MathUtils.clamp(timeRatio, Constants.PLAYER_MIN_CLIMB_RATIO, Constants.PLAYER_MAX_CLIMB_RATIO);
        this.climbTimeLeft = Constants.PLAYER_CLIMB_TIME * timeRatio;

        climbing = true;
        player().mover().land();

        // Set the player position to new position based on if we're climbing left or right
        player().setGamePosition(
                (climbingRight) ? gripX() : gripX() - player().getWidth(),
                gripY()
        );

        // set velocity to 0, in case we clip into the ledge area
        player().getBody().setLinearVelocity(0f, 0f);

        // TODO: Set next animation state when we are climbing
    }

    /*
        Getters and Setters
     */

    public void cancelClimb() { climbing = false; }
    public boolean isClimbing() { return climbing; }
    public float getClimbTimeLeft() { return this.climbTimeLeft; }

    public float gripX() { return gripPoint.x; }
    public float gripY() { return gripPoint.y; }
}
