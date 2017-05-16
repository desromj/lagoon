package com.greenbatgames.lagoon.player;

public class PlayerHealthComponent extends PlayerComponent {

    private int health;
    private int maxHealth;

    public PlayerHealthComponent(Player player, int health, int maxHealth) {
        super(player);
        this.health = health;
        this.maxHealth = maxHealth;
    }

    @Override
    public boolean update(float delta) {
        return true;
    }

    /**
     * Decrements the player's health by the passed damage amount
     * @param damage The amount of damage to apply to the player
     * @return true if the player is dead (health less than 0), False otherwise
     */
    public boolean damage(int damage) {
        health -= damage;
        return isDead();
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
}
