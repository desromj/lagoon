package com.greenbatgames.lagoon.enemy.behaviours;

import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.lagoon.ai.B2dSteerable;
import com.greenbatgames.lagoon.physics.PhysicsBody;

public class CrawlBehaviour {

    PhysicsBody parent;
    B2dSteerable steerable;

    public CrawlBehaviour(PhysicsBody parent) {
        this.parent = parent;
        this.steerable = null;
    }

    private void makeBehavior() {
        steerable = new B2dSteerable.Builder()
                .parent(parent)
                .boundingRadius(2)
                .maxAngularAcceleration(3)
                .maxAngularSpeed(2)
                .maxLinearAcceleration(1)
                .maxLinearSpeed(2)
                .build();

        Wander<Vector2> wander = new Wander<>(steerable)
                .setWanderOrientation(0f)
                .setWanderRadius(0)
                .setWanderRate(1)
                .setWanderOffset(1)
                .setDecelerationRadius(0.5f)
                .setEnabled(true);

        steerable.setBehavior(wander);
    }

    public void update(float delta) {
        if (steerable == null) {
            makeBehavior();
        } else {
            steerable.update(delta);
        }
    }
}
