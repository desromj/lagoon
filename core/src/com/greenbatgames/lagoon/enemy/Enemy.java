package com.greenbatgames.lagoon.enemy;

import com.greenbatgames.lagoon.physics.PhysicsBody;

public abstract class Enemy extends PhysicsBody {

    private int health;
    private int maxHealth;

    public Enemy(float x, float y, float width, float height, int maxHealth) {
        super(x, y, width, height);
        this.health = maxHealth;
        this.maxHealth = maxHealth;
    }


    /**
     * Decrements the enemy's health by the passed damage amount
     * @param damage The amount of damage to apply to the enemy
     * @return true if the enemy is dead (health less than 0), False otherwise
     */
    public boolean damage(int damage) {
        health -= damage;
        return isDead();
    }


    public abstract int getContactDamage();


    public void setHealth(int newHealth) {
        if (newHealth > maxHealth) {
            health = maxHealth;
        } else {
            health = newHealth;
        }
    }
    public boolean isDead() {
        return health <= 0;
    }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
}
