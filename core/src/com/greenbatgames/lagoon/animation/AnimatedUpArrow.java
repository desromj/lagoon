package com.greenbatgames.lagoon.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class AnimatedUpArrow {

    Animation animation;
    Vector2 position;
    long startTime;
    float elapsedTime;
    boolean active;

    public AnimatedUpArrow() {

        startTime = TimeUtils.nanoTime();
        elapsedTime = 0f;
        position = new Vector2();
        active = false;

        Sprite sprite = new Sprite(new Texture(Gdx.files.internal("icons/arrow-up.png")));

        Sprite up = new Sprite(sprite.getTexture(),0, 0, 64, 64);
        Sprite down = new Sprite(sprite.getTexture(),64, 0, 64, 64);

        animation = new Animation(1f, up, down);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
                animation.getKeyFrame(elapsedTime, true),
                position.x,
                position.y
        );
    }

    public void update(float delta) {
        elapsedTime = TimeUtils.nanosToMillis((TimeUtils.nanoTime() - startTime)) / 1000f;
    }

    public void setGamePosition(float x, float y) {
        this.position.set(x, y);
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
