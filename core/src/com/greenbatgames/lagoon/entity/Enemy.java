package com.greenbatgames.lagoon.entity;

import com.greenbatgames.lagoon.physics.PhysicsBody;

public abstract class Enemy extends PhysicsBody {

    private int health;
    private int maxHealth;

    public Enemy(float x, float y, float width, float height, int health, int maxHealth) {
        super(x, y, width, height);
        this.health = health;
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

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
}
