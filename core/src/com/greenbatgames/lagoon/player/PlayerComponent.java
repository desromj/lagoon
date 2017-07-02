package com.greenbatgames.lagoon.player;

/**
 * Created by Quiv on 23-02-2017.
 */

public abstract class PlayerComponent {

    private Player player;

    public PlayerComponent(Player player) {
        this.player = player;
        this.init();
    }

    /**
     * Standard update method, altered to return a status flag
     *
     * @param delta delta time since last update
     * @return true if the update loop should keep going in the sequence of Component updates. False to break
     *         the execution of any further PlayerComponent updates
     */
    public abstract boolean update(float delta);

    /**
     * Every Player Component needs to reset itself upon level reloading
     */
    public abstract void init();

    protected final Player player() { return player; }
}
