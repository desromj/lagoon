package com.greenbatgames.lagoon.enemy.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.greenbatgames.lagoon.enemy.Enemy;
import com.greenbatgames.lagoon.enemy.components.CrawlComponent;
import com.greenbatgames.lagoon.physics.CirclePhysicsLoader;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.util.Constants;

public class Crawler extends Enemy {

    private CrawlComponent crawler;

    public Crawler(float x, float y, float width, float height) {
        super(x, y, width, height, Constants.CRAWLER_HEALTH, Constants.CRAWLER_HEALTH);
        getPhysicsLoader().load(this);

        crawler = new CrawlComponent(this, Constants.CRAWLER_MOVE_SPEED);
    }

    @Override
    protected PhysicsLoader getPhysicsLoader() {
        return new CirclePhysicsLoader(this, false, BodyDef.BodyType.KinematicBody);
    }

    @Override
    public void act(float delta) {
        crawler.update(delta);
    }

    // TODO: Eventually draw
    @Override
    public void draw(Batch batch, float parentAlpha) {

    }

    public int getContactDamage() {
        return Constants.CRAWLER_CONTACT_DAMAGE;
    }
}
