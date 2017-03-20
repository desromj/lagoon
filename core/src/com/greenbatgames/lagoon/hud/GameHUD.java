package com.greenbatgames.lagoon.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.lagoon.screen.GameScreen;

/**
 * Created by Quiv on 23-01-2017.
 */

public class GameHUD extends Actor {

    public static final String TAG = GameHUD.class.getSimpleName();

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // Move on to sprites and fonts
        Viewport viewport = GameScreen.level().getViewport();
        batch.setProjectionMatrix(viewport.getCamera().combined);

    }
}
