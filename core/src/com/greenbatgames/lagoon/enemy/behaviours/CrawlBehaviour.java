package com.greenbatgames.lagoon.enemy.behaviours;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.lagoon.ai.B2dLocation;
import com.greenbatgames.lagoon.ai.B2dSteerable;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Utils;

import java.util.Random;

public class CrawlBehaviour {

    private PhysicsBody parent;
    private B2dSteerable steerable;

    private SteeringBehavior<Vector2> crawlLeft;
    private SteeringBehavior<Vector2> crawlRight;

    private boolean moving;

    public CrawlBehaviour(PhysicsBody parent) {
        this.parent = parent;
        steerable = null;
        moving = false;
    }


    private void makeBehavior() {
        steerable = new B2dSteerable.Builder()
                .parent(parent)
                .boundingRadius(1)
                .maxAngularAcceleration(3)
                .maxAngularSpeed(2)
                .maxLinearAcceleration(1500)
                .maxLinearSpeed(2)
                .build();

        crawlLeft = new Arrive<>(steerable)
                .setArrivalTolerance(0.5f)
                .setDecelerationRadius(2f)
                .setTarget(new B2dLocation(
                        (parent.getX() - 500) / Constants.PTM,
                        (parent.getY() + parent.getHeight() * 2f) / Constants.PTM
                ));

        crawlRight = new Arrive<>(steerable)
                .setArrivalTolerance(0.5f)
                .setDecelerationRadius(2f)
                .setTarget(new B2dLocation(
                        (parent.getX() + 500) / Constants.PTM,
                        (parent.getY() + parent.getHeight() * 2f) / Constants.PTM
                ));

        steerable.setBehavior(new Random().nextBoolean() ? crawlRight : crawlLeft);
    }


    public void update(float delta) {
        if (steerable == null) {
            makeBehavior();
        } else {
            moving = !Utils.almostEqualTo(parent.getBody().getLinearVelocity().len(), 0f, 0.1f);

            if (!moving) {
                if (steerable.getBehavior().equals(crawlLeft)) {
                    steerable.setBehavior(crawlRight);
                } else {
                    steerable.setBehavior(crawlLeft);
                }
            }

            steerable.update(delta);
        }
    }
}
