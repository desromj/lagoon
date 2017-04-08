package com.greenbatgames.lagoon.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
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
    private Vector2 velocity;
    private Vector2 targetPoint;

    public ChaseCam(OrthographicCamera camera, PhysicsBody target) {
        this.target = target;
        this.camera = camera;
        this.following = true;
        this.velocity = new Vector2();
        this.targetPoint = new Vector2();

        centreOnTarget();
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (!following)
                centreOnTarget();

            following = !following;
        }

        if (following && target != null) {
            autoFollowTarget();
        } else {
            manuallyMoveCamera(delta);
        }
    }

    // Manual controls for moving the camera around the entire game world
    private void manuallyMoveCamera(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            camera.position.x -= delta * Constants.CHASE_CAM_MAX_VELOCITY_MANUAL;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            camera.position.x += delta * Constants.CHASE_CAM_MAX_VELOCITY_MANUAL;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            camera.position.y += delta * Constants.CHASE_CAM_MAX_VELOCITY_MANUAL;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            camera.position.y -= delta * Constants.CHASE_CAM_MAX_VELOCITY_MANUAL;
        }
    }

    // Natural behaviour for the camera to follow its target
    private void autoFollowTarget() {

        // Once we have the target point, accelerate towards it, unless we are already on it/passed it
        setTargetPoint();

        Vector2 distToTarget = new Vector2(
                targetPoint.x - camera.position.x,
                targetPoint.y - camera.position.y
        );

        // TODO: Still jumps around a tiny bit, could use optimising
        if (distToTarget.len() > Constants.CHASE_CAM_THRESHOLD) {
            velocity.set(distToTarget.x, distToTarget.y);
            velocity.nor().scl(Constants.CHASE_CAM_MAX_VELOCITY);
            camera.position.add(velocity.x, velocity.y, 0f);
        } else {
            centreOnTarget();
        }

    }

    // Set the new target point, based on the target and what player buttons are being pressed
    private void setTargetPoint() {
        targetPoint.set(target.getMiddleX(), target.getMiddleY());

        // Determine the target point based on if we're holding down any movement keys
        if (Gdx.input.isKeyPressed(Constants.KEY_LEFT)) {
            targetPoint.set(target.getMiddleX() - Constants.CHASE_CAM_OFFSET, targetPoint.y);
        }
        if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT)) {
            targetPoint.set(target.getMiddleX() + Constants.CHASE_CAM_OFFSET, targetPoint.y);
        }
        if (Gdx.input.isKeyPressed(Constants.KEY_UP)) {
            targetPoint.set(targetPoint.x, target.getMiddleY() + Constants.CHASE_CAM_OFFSET);
        }
        if (Gdx.input.isKeyPressed(Constants.KEY_DOWN)) {
            targetPoint.set(targetPoint.x, target.getMiddleY() - Constants.CHASE_CAM_OFFSET);
        }
    }

    // Instantly centres the camera on the target point, if a target is set
    public void centreOnTarget() {
        camera.position.x = targetPoint.x;
        camera.position.y = targetPoint.y;
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
