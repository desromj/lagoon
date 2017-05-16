package com.greenbatgames.lagoon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.InputProcessor;
import com.greenbatgames.lagoon.level.Level;
import com.greenbatgames.lagoon.level.LevelLoader;
import com.greenbatgames.lagoon.util.Constants;

/**
 * Created by Quiv on 23-01-2017.
 */
public class GameScreen extends ScreenAdapter implements InputProcessor {

    public static final String TAG = GameScreen.class.getSimpleName();
    private static GameScreen instance = null;

    private GameScreen() {}

    private Level level;

    public static final GameScreen getInstance() {
        if (instance == null) {
            instance = new GameScreen();
            instance.level = null;
        }

        return instance;
    }

    public void init() {
        loadMap("hub-1", "t_topRight");
        Gdx.input.setInputProcessor(this);
    }

    public void loadMap(String mapName, String transitionPointName) {
        level = LevelLoader.loadLevel(mapName, transitionPointName);
        level.centreCameraOnPlayer();
    }

    /*
        ScreenAdapter Overrides
     */

    @Override
    public void render(float delta) {
        if (level != null) {
            level.render(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        if (level != null) {
            level.getViewport().update(width, height, true);
        }
    }

    /*
        InputProcessor Overrides
     */

    @Override
    public boolean keyDown(int keycode)
    {
        if (keycode == Constants.KEY_QUIT) {
            Gdx.app.exit();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /*
        Getters and Setters
     */
    public static Level level() { return instance.level; }
    public void setLevel(Level level) { this.level = level; }
}
