package com.greenbatgames.lagoon.enemy.components;

import com.greenbatgames.lagoon.enemy.Enemy;
import com.greenbatgames.lagoon.enemy.EnemyComponent;

import java.util.Random;

public class CrawlComponent extends EnemyComponent {

    private boolean movingRight;
    private boolean stayStill;
    private float stateTime;
    private float moveSpeed;

    private Random rand;

    public CrawlComponent(Enemy enemy, float moveSpeed) {
        super(enemy);

        rand = new Random();

        this.moveSpeed = moveSpeed;
        movingRight = rand.nextBoolean();
        stayStill = rand.nextBoolean();
        setNextStateTime();
    }

    @Override
    public boolean update(float delta) {

        stateTime -= delta;

        if (stateTime < 0) {
            setNextStateTime();
        }

        if (stayStill) {
            enemy().getBody().setLinearVelocity(0f, 0f);
        } else {
            enemy().getBody().setLinearVelocity(
                    (movingRight) ? moveSpeed : -moveSpeed,
                    enemy().getBody().getLinearVelocity().y
            );
        }

        return true;
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

}
