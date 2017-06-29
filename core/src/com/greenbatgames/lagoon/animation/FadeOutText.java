package com.greenbatgames.lagoon.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

/**
 * FadeOutText pops into existence with a specific message. It will appear at full opacity
 * at the passed position, then move upwards with slowing velocity while fading out.
 *
 * On instantiation, the object is immediately added to the stage in the current level.
 * Therefore, you do not need to do anything
 *
 * By default, it lasts Constants.FADE_OUT_TEXT_FADE_TIME seconds, though
 * this can be overridden
 */
public class FadeOutText extends Actor {

    private BitmapFont font;
    private Vector2 position;
    private Vector2 velocity;
    private String message;

    private float lifetime;
    private float timeRemaining;
    private float alpha;

    /**
     * Use this to create FadeOutText objects and have them immediately added to the current level's stage
     * @param position The position to spawn the message at
     * @param message The message to display
     */
    public static void create(Vector2 position, String message) {
        FadeOutText text = new FadeOutText();
        text.position.set(position.x, position.y);
        text.message = message;
        GameScreen.level().addActorToStage(text);
    }

    /**
     * Use this to create FadeOutText objects and have them immediately added to the current level's stage
     * @param x The x position to spawn the message at
     * @param y The y position to spawn the message at
     * @param message The message to display
     */
    public static void create(float x, float y, String message) {
        FadeOutText.create(new Vector2(x, y), message);
    }

    private FadeOutText() {
        font = new BitmapFont(Gdx.files.internal("fonts/arial-grad.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(1f);
        position = new Vector2();
        velocity = new Vector2();
        message = "";
        lifetime = Constants.FADE_OUT_TEXT_FADE_TIME;
        timeRemaining = Constants.FADE_OUT_TEXT_FADE_TIME;
        alpha = 1f;
    }


    @Override
    public void act(float delta) {
        // Set the alpha value to draw the font at
        this.timeRemaining -= delta;
        this.alpha = timeRemaining / this.lifetime;

        // interpolate position update - slow down accel as time goes on
        velocity.set(0, Constants.FADE_OUT_TEXT_Y_VELOCITY * Interpolation.circleOut.apply(this.alpha));
        position.add(velocity);

        // Destroy this object if the lifetime is over
        if (timeRemaining < 0) {
            this.remove();
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.setColor(1, 1, 1, this.alpha);
        font.draw(
                batch,
                this.message,
                this.position.x,
                this.position.y,
                0,
                Align.center,
                false
        );
    }
}
