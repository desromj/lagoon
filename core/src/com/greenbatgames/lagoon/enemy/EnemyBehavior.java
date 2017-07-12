package com.greenbatgames.lagoon.enemy;

import com.greenbatgames.lagoon.ai.B2dSteerable;
import com.greenbatgames.lagoon.physics.PhysicsBody;

public abstract class EnemyBehavior {

    protected PhysicsBody parent;
    protected B2dSteerable steerable;

    public EnemyBehavior(PhysicsBody parent) {
        this.parent = parent;
        steerable = null;
    }

    /**
     * This method should instantiate the steerable object, as well as load
     * any steering behaviours to be used by the child class
     */
    protected abstract void makeBehavior();

    /**
     * steerable is guaranteed to not be null here. This method is run within an
     * update loop, and only needs to determine which steering behaviours to use
     * and how to apply them to the steerable.
     * @param delta Time since last frame
     */
    protected abstract void useSteering(float delta);

    public final void update(float delta) {
        if (steerable == null) {
            makeBehavior();
        } else {
            useSteering(delta);
        }
    }
}
