package com.greenbatgames.lagoon.player;

import com.greenbatgames.lagoon.util.Constants;

public class PlayerHealthComponent extends PlayerComponent {

    private int health;
    private int maxHealth;
    private float invulnerableFor;

    public PlayerHealthComponent(Player player, int health, int maxHealth) {
        super(player);
        this.health = health;
        this.maxHealth = maxHealth;
        this.invulnerableFor = 0f;
    }

    @Override
    public boolean update(float delta) {
        invulnerableFor -= delta;
        return true;
    }

    /**
     * Decrements the player's health by the passed damage amount
     * @param damage The amount of damage to apply to the player
     * @return true if the player is dead (health less than 0), False otherwise
     */
    public boolean damage(int damage) {
        if (invulnerableFor > 0) {
            return false;
        }

        doDamage(damage);
        return isDead();
    }

    private void doDamage(int damage) {
        health -= damage;
        invulnerableFor = Constants.PLAYER_DAMAGE_RECOVERY_TIME;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }

    public void healFully() { this.health = maxHealth; }
}
