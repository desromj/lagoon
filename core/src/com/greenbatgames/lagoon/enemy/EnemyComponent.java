package com.greenbatgames.lagoon.enemy;

// TODO: This abstract class is essentially the same as player.PlayerComponent. Might be able to combine
public abstract class EnemyComponent {

    private Enemy enemy;

    public EnemyComponent(Enemy enemy) { this.enemy = enemy; }

    /**
     * Standard update method, altered to return a status flag
     *
     * @param delta delta time since last update
     * @return true if the update loop should keep going in the sequence of Component updates. False to break
     *         the execution of any further PlayerComponent updates
     */
    public abstract boolean update(float delta);

    protected Enemy enemy() { return enemy; }
}
