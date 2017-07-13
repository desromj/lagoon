package com.greenbatgames.lagoon.enemy.enemies;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.greenbatgames.lagoon.ai.B2dSteerable;
import com.greenbatgames.lagoon.enemy.Enemy;
import com.greenbatgames.lagoon.enemy.EnemyBehavior;
import com.greenbatgames.lagoon.enemy.behaviours.BatBehaviour;
import com.greenbatgames.lagoon.physics.CirclePhysicsLoader;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.util.Constants;

public class Bat extends Enemy {

    private EnemyBehavior behavior;

    public Bat(float x, float y, float width, float height) {
        super(x, y, width, height, Constants.BAT_HEALTH);
        getPhysicsLoader().load(this);
        behavior = new BatBehaviour(this);
    }


    @Override
    public int getContactDamage() {
        return Constants.BAT_CONTACT_DAMAGE;
    }


    @Override
    protected PhysicsLoader getPhysicsLoader() {
        return new CirclePhysicsLoader(
                this,
                false,
                BodyDef.BodyType.DynamicBody,
                Constants.BAT_RADIUS);
    }


    @Override
    protected B2dSteerable makeSteerable() {
        return new B2dSteerable.Builder()
                .parent(this)
                .boundingRadius(1)
                .maxAngularAcceleration(3)
                .maxAngularSpeed(2)
                .maxLinearAcceleration(2)
                .maxLinearSpeed(Constants.BAT_MAX_SPEED)
                .build();
    }
}
