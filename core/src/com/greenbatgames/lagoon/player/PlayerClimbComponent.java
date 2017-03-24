package com.greenbatgames.lagoon.player;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.lagoon.util.Constants;

/**
 * Created by Quiv on 23-03-2017.
 */
public class PlayerClimbComponent extends PlayerComponent {
    public static final String TAG = PlayerClimbComponent.class.getSimpleName();

    /** The point where the player will grab and pivot around while climbing */
    private Vector2 gripPoint;

    private float climbTimeLeft;
    private boolean climbing;
    private boolean climbingRight;

    public PlayerClimbComponent(Player player) {
        super(player);

        gripPoint = new Vector2();
        climbTimeLeft = 0.0f;
        climbing = false;
        climbingRight = false;
    }

    @Override
    public boolean update(float delta) {

        climbTimeLeft -= delta;

        // Return true if we're not climbing or have been climbing for too long already
        if (climbTimeLeft <= 0f) {
            climbing = false;
        }

        if (!climbing) {
            return true;
        }

        /*
            If climbing, we set the player's position to its ultimate final
            position, then play the animation for climbing which is centred
            around a base point. This base point is the ledge being climbed on.
            if we're climbing right, base point is bottom-left of player box
            if we're climbing left, base point is bottom-right of player base
        */

        // Set the player position to new position based on if we're climbing left or right
        player().setPosition(
                (climbingRight) ? gripX() : gripX() - player().getWidth(),
                gripY()
        );

        // set velocity to 0, in case we clip into the ledge area
        player().getBody().setLinearVelocity(0f, 0f);

        return false;
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

        // Set next animation state when we are climbing
//        player().animator().setNext(Enums.AnimationState.CLIMB, timeRatio);
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
