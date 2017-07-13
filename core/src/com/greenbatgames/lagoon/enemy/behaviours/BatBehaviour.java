package com.greenbatgames.lagoon.enemy.behaviours;

import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.lagoon.enemy.EnemyBehavior;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.util.Utils;

/**
 * Bats are dormant until activated
 *
 * When activated, bats will fly around the player, trying to stay
 * above them
 *
 * Occasionally, bats will swoop in and attack the player, then
 * go back to hovering above their head
 *
 * Bats need to be within a certain range to make a swooping attack,
 * and can only make the attack a certain length of time after being within range
 *
 * TODO: finish the logic here
 */
public class BatBehaviour extends EnemyBehavior {

    boolean active;
    float withinRangeFor;

    public BatBehaviour(PhysicsBody parent) {
        super(parent);
        active = false;
        withinRangeFor = 0f;
    }

    @Override
    protected void makeBehavior() {
        steerable = parent.getSteerable();

        Seek<Vector2> seek = new Seek<>(steerable, Utils.player().getSteerable());
    }

    @Override
    protected void useSteering(float delta) {

    }
}
