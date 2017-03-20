package com.greenbatgames.lagoon.player;

/**
 * Created by Quiv on 23-02-2017.
 *
 * Responsible for handling keyboard input and general movement of the Player.
 *
 * Movement options include:
 *      - moving left/right
 *      - jumping
 *      - climbing
 *      - crawling
 *      - swimming
 */
public class PlayerMoveComponent extends PlayerComponent {

    private boolean
        moving, grounded, climbing, prone, swimming;

    public PlayerMoveComponent(Player player) {
        super(player);
    }

    @Override
    public boolean update(float delta) {
        return false;
    }
}
