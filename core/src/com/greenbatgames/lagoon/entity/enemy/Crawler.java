package com.greenbatgames.lagoon.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.greenbatgames.lagoon.entity.Enemy;
import com.greenbatgames.lagoon.physics.CirclePhysicsLoader;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.util.Constants;

import java.util.Random;

public class Crawler extends Enemy {

    private boolean movingRight;
    private boolean stayStill;
    private float stateTime;

    private Random rand;

    public Crawler(float x, float y, float width, float height) {
        super(x, y, width, height, Constants.CRAWLER_HEALTH, Constants.CRAWLER_HEALTH);
        getPhysicsLoader().load(this);

        rand = new Random();

        movingRight = rand.nextBoolean();
        stayStill = rand.nextBoolean();
        setNextStateTime();
    }

    @Override
    protected PhysicsLoader getPhysicsLoader() {
        return new CirclePhysicsLoader(this, false, BodyDef.BodyType.DynamicBody);
    }

    @Override
    public void act(float delta) {

        stateTime -= delta;

        if (stateTime < 0) {
            setNextStateTime();
        }

        if (stayStill) {
            getBody().setLinearVelocity(0f, 0f);
        } else {
            getBody().setLinearVelocity(
                    (movingRight) ? Constants.CRAWLER_MOVE_SPEED : -Constants.CRAWLER_MOVE_SPEED,
                    getBody().getLinearVelocity().y
            );
        }
    }

    // TODO: Eventually draw
    @Override
    public void draw(Batch batch, float parentAlpha) {

    }

    private void setNextStateTime() {
        stayStill = !stayStill;
        if (stayStill) {
            changeDirection();
        }
        stateTime = rand.nextFloat() * 8f;
    }

    private void changeDirection() {
        movingRight = !movingRight;
    }

    public int getContactDamage() {
        return Constants.CRAWLER_CONTACT_DAMAGE;
    }
}
