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

    public abstract void update(float delta);
}
