package com.greenbatgames.lagoon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.screen.StartScreen;

public class LagoonGame extends Game {

	private static Game instance;
	private static I18NBundle bundle;
	
	@Override
	public void create () {
		instance = this;

		bundle = I18NBundle.createBundle(
				Gdx.files.internal("strings/MyBundle"),
				"ISO-8859-1"
		);

        LagoonGame.setScreen(StartScreen.class);
	}

    /**
     * Allows switching between high-level screens from the com.greenbatgames.lagoon.screen package
     * @param type The class containing the desired screen to switch to
     */
    public static void setScreen(Class type) {
        if (type == GameScreen.class) {
            instance.setScreen(GameScreen.getInstance());
            GameScreen.getInstance().init();
        } else if (type == StartScreen.class) {
            instance.setScreen(new StartScreen(
                    "LGG_logo.png",
                    getString("game"),
                    getString("subtitle"),
                    0.8f));
        }
    }

    public static String getString(String key, Object ... args) {
        return bundle.format(key, args);
    }
}
