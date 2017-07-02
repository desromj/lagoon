package com.greenbatgames.lagoon.player.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.greenbatgames.lagoon.animation.AnimatedUpArrow;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;

public class TooltipComponent extends PlayerComponent {

    AnimatedUpArrow arrow;

    public TooltipComponent(Player player) {
        super(player);
    }

    @Override
    public void init() {
        arrow = new AnimatedUpArrow();
    }

    @Override
    public boolean update(float delta) {
        arrow.update(delta);
        arrow.setPosition(
                player().getPosition().x - player().getWidth() / 2f,
                player().getY() + player().getHeight() * 2.5f);
        return true;
    }

    public void draw(Batch batch, float parentAlpha) {
        if (arrow.isActive()) {
            arrow.draw(batch, parentAlpha);
        }
    }

    public void show() { arrow.setActive(true); }
    public void hide() { arrow.setActive(false); }
}
