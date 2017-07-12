package com.greenbatgames.lagoon.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.lagoon.ai.B2dLocation;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Utils;

public abstract class PhysicsBody extends Actor implements Location<Vector2> {

    public static final String TAG = PhysicsBody.class.getSimpleName();

    private Vector2 lastPosition;
    protected Body body;

    /**
     * Be sure to call getPhysicsLoader().load() in the constructor of
     * the child class. This will actually instantiate the physics. Some objects
     * require exposing vertices which can only be done after the super constructor,
     * otherwise the call would be in the constructor of PhysicsBody
     *
     * @param x The x location of the bottom-left corner of the object
     * @param y The y location of the bottom-left corner of the object
     * @param width The width of the object
     * @param height The width of the object
     */
    public PhysicsBody(float x, float y, float width, float height) {
        setGamePosition(x, y);
        setWidth(width);
        setHeight(height);

        lastPosition = new Vector2(getX(), getY());
        body = null;
    }

    protected abstract PhysicsLoader getPhysicsLoader();

    @Override
    public void act(float delta) {
        // Update our last position for the next frame
        setLastPosition(getX(), getY());

        if (body == null) {
            Gdx.app.debug(TAG, "Body is null");
            return;
        }

        // Cling this object's position to the physics body
        setGamePosition(
                (body.getPosition().x * Constants.PTM) - getWidth() / 2.0f,
                (body.getPosition().y * Constants.PTM) - getHeight() / 2.0f
        );
    }

    public void setGamePosition(float x, float y) {
        // Set position of the parent Actor
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

    public void setLastPosition(float x, float y) {
        lastPosition.set(x, y);
    }

    public Body getBody() { return body; }
    public void setBody(Body body) { this.body = body; }

    public float getMiddleX() {
        return getX() + getWidth() / 2.0f;
    }
    public float getMiddleY() {
        return getY() + getHeight() / 2.0f;
    }

    public Vector2 getGamePosition() { return new Vector2(getX(), getY()); }
    public Vector2 getLastGamePosition() { return new Vector2(lastPosition.x, lastPosition.y); }


    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(body.getPosition(), orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Utils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Utils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new B2dLocation();
    }
}
