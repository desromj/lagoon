package com.greenbatgames.lagoon.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.util.Constants;

/**
 * Created by Quiv on 20-03-2017.
 */
public class ChaseCam extends Actor {

    private PhysicsBody target;
    private OrthographicCamera camera;
    private Boolean following;

    public ChaseCam(OrthographicCamera camera, PhysicsBody target) {
        this.target = target;
        this.camera = camera;
        init();
    }

    public void init() {
        this.following = true;
        this.centreOnTarget();
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (!following)
                centreOnTarget();

            following = !following;
        }

        if (following && target != null) {
            float
                    xLeeway = Constants.CHASE_CAM_X_LEEWAY / 2.0f,
                    yLeeway = Constants.CHASE_CAM_Y_LEEWAY / 2.0f;

            if (camera.position.x > target.getX()
                    && camera.position.x - target.getX() > xLeeway)
                camera.position.x = target.getX() + xLeeway;

            if (camera.position.x < target.getX()
                    && target.getX() - camera.position.x > xLeeway)
                camera.position.x = target.getX() - xLeeway;

            if (camera.position.y > target.getY()
                    && camera.position.y - target.getY() > yLeeway)
                camera.position.y = target.getY() + yLeeway;

            if (camera.position.y < target.getY()
                    && target.getY() - camera.position.y > yLeeway)
                camera.position.y = target.getY() - yLeeway;

        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.J)) {
                camera.position.x -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                camera.position.x += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.I)) {
                camera.position.y += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                camera.position.y -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
        }
    }

    public void centreOnTarget() {
        if (target != null) {
            camera.position.x = target.getX();
            camera.position.y = target.getY();
        }
    }

    /*
        Getters and Setters
     */

    public float getX() { return camera.position.x - camera.viewportWidth / 2.0f; }
    public float getY() { return camera.position.y - camera.viewportHeight / 2.0f; }

    public float getLeft() { return camera.position.x - camera.viewportWidth / 2.0f; }
    public float getBottom() { return camera.position.y - camera.viewportHeight / 2.0f; }
    public float getRight() { return camera.position.x + camera.viewportWidth / 2.0f; }
    public float getTop() { return camera.position.y + camera.viewportHeight / 2.0f; }

    public float getWidth() { return camera.viewportWidth; }
    public float getHeight() { return camera.viewportHeight; }
    public PhysicsBody getTarget() { return target; }
    public void setTarget(PhysicsBody target) { this.target = target; }
    public OrthographicCamera getCamera() { return camera; }
}
