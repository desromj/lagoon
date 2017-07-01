package com.greenbatgames.lagoon.player.components;

import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;
import com.greenbatgames.lagoon.util.Constants;

public class HealthComponent extends PlayerComponent {

    private int health;
    private int maxHealth;
    private float invulnerableFor;

    public HealthComponent(Player player, int health, int maxHealth) {
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
    public boolean damage(int damage, boolean knockback) {
        if (invulnerableFor > 0) {
            return false;
        }

        doDamage(damage, knockback);
        return isDead();
    }

    private void doDamage(int damage, boolean knockback) {
        health -= damage;
        invulnerableFor = Constants.PLAYER_DAMAGE_RECOVERY_TIME;

        // Add a tiny knockback to the player on damage. Not so high as to be very disruptive
        if (knockback) {
            player().mover().applyKnockback();
        }
    }

    public boolean isInvulnerable() { return invulnerableFor <= 0f; }

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }

    public void healFully() { this.health = maxHealth; }
}
