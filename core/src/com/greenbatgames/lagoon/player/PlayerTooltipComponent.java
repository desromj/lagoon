package com.greenbatgames.lagoon.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.greenbatgames.lagoon.animation.AnimatedUpArrow;

public class PlayerTooltipComponent extends PlayerComponent {

    AnimatedUpArrow arrow;

    public PlayerTooltipComponent(Player player) {
        super(player);
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
