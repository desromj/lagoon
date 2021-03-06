package com.greenbatgames.lagoon.player.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.greenbatgames.lagoon.entity.Transition;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

public class TransitionComponent extends PlayerComponent {

    private static final String TAG = TransitionComponent.class.getSimpleName();

    public TransitionComponent(Player player) {
        super(player);
    }

    @Override
    public void init() {}

    @Override
    public boolean update(float delta) {

        if (Gdx.input.isKeyJustPressed(Constants.KEY_UP)) {
            GameScreen.level().getWorld().QueryAABB(
                    getCallback(),
                    player().getX() / Constants.PTM,
                    player().getY() / Constants.PTM,
                    (player().getX() + player().getWidth()) / Constants.PTM,
                    (player().getY() + player().getHeight()) / Constants.PTM
            );
        }

        return true;
    }

    private QueryCallback getCallback() {
        return fixture ->  {
            if (fixture.getBody().getUserData() instanceof Transition) {
                Transition trans = (Transition) fixture.getBody().getUserData();
                trans.transition();

                return false;
            }

            return true;
        };
    }
}
