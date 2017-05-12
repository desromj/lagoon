package com.greenbatgames.lagoon.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.lagoon.util.Constants;

/**
 * Created by Quiv on 23-01-2017.
 */

public abstract class PhysicsBody extends Actor {

    public static final String TAG = PhysicsBody.class.getSimpleName();

    private Vector2 lastPosition;
    protected Body body;

    /**
     * @param x The x location of the bottom-left corner of the object
     * @param y The y location of the bottom-left corner of the object
     * @param width The width of the object
     * @param height The width of the object
     */
    public PhysicsBody(float x, float y, float width, float height) {
        setPosition(x, y);
        setWidth(width);
        setHeight(height);

        lastPosition = new Vector2(getX(), getY());
        body = null;
    }

    protected abstract PhysicsLoader getPhysicsLoader();

    @Override
    public void act(float delta) {
        // Update our last position for the next frame
        lastPosition.set(getX(), getY());

        if (body == null) {
            Gdx.app.debug(TAG, "Body is null");
            return;
        }

        // Cling this object's position to the physics body
        setPosition(
                (body.getPosition().x * Constants.PTM) - getWidth() / 2.0f,
                (body.getPosition().y * Constants.PTM) - getHeight() / 2.0f
        );
    }

    @Override
    public final void setPosition(float x, float y) {
        super.setPosition(x, y);

        if (body == null) {
            Gdx.app.debug(TAG, "Body is null");
            return;
        }

        body.setTransform(
                (getX() + getWidth() / 2f) / Constants.PTM,
                (getY() + getHeight() / 2f) / Constants.PTM,
                body.getAngle()
        );
    }

    public Body getBody() { return body; }
    public void setBody(Body body) { this.body = body; }

    public float getMiddleX() {
        return getX() + getWidth() / 2.0f;
    }
    public float getMiddleY() {
        return getY() + getHeight() / 2.0f;
    }

}
