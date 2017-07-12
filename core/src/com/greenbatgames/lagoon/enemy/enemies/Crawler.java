package com.greenbatgames.lagoon.enemy.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.greenbatgames.lagoon.ai.B2dSteerable;
import com.greenbatgames.lagoon.enemy.Enemy;
import com.greenbatgames.lagoon.enemy.EnemyBehavior;
import com.greenbatgames.lagoon.enemy.behaviours.CrawlBehaviour;
import com.greenbatgames.lagoon.physics.CirclePhysicsLoader;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.util.Constants;

public class Crawler extends Enemy {

    private EnemyBehavior behavior;

    public Crawler(float x, float y, float width, float height) {
        super(x, y, width, height, Constants.CRAWLER_HEALTH);
        getPhysicsLoader().load(this);
        behavior = null;
        loadBehavior();
    }


    @Override
    protected PhysicsLoader getPhysicsLoader() {
        return new CirclePhysicsLoader(this, false, BodyDef.BodyType.DynamicBody);
    }


    @Override
    protected B2dSteerable makeSteerable() {
        return new B2dSteerable.Builder()
                .parent(this)
                .boundingRadius(1)
                .maxAngularAcceleration(3)
                .maxAngularSpeed(2)
                .maxLinearAcceleration(1500)
                .maxLinearSpeed(2)
                .build();
    }


    private void loadBehavior() {
        behavior = new CrawlBehaviour(this);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        if (behavior != null) {
            behavior.update(delta);
        }
    }


    // TODO: Eventually draw
    @Override
    public void draw(Batch batch, float parentAlpha) {

    }


    @Override
    public int getContactDamage() {
        return Constants.CRAWLER_CONTACT_DAMAGE;
    }
}
