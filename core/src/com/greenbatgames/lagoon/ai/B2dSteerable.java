package com.greenbatgames.lagoon.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.util.Utils;

public class B2dSteerable implements Steerable<Vector2> {

    private PhysicsBody parent;
    private float boundingRadius;

    private boolean useForce;

    private boolean tagged;
    private float maxLinearSpeed;
    private float maxLinearAcceleration;
    private float maxAngularSpeed;
    private float maxAngularAcceleration;
    private float zeroLinearSpeedThreshold;

    private SteeringBehavior<Vector2> behavior;
    private SteeringAcceleration<Vector2> steerOutput;

    private B2dSteerable() {
        tagged = false;
        boundingRadius = 0f;
        useForce = false;
        maxLinearSpeed = 0f;
        maxLinearAcceleration = 0f;
        maxAngularSpeed = 0f;
        maxAngularAcceleration = 0f;
        zeroLinearSpeedThreshold = 0.001f;
        steerOutput = new SteeringAcceleration<>(new Vector2());
    }

    /**
     * Builder class should set every float field possible, except for zeroLinearSpeedThreshold
     */
    public static class Builder {

        private B2dSteerable steerable;

        public Builder() {
            steerable = new B2dSteerable();
        }

        public Builder parent(PhysicsBody parent) {
            steerable.parent = parent;
            return this;
        }

        public Builder boundingRadius(float boundingRadius) {
            steerable.boundingRadius = boundingRadius;
            return this;
        }

        public Builder useForce(boolean useForce) {
            steerable.useForce = useForce;
            return this;
        }

        public Builder tagged(boolean tagged) {
            steerable.tagged = tagged;
            return this;
        }

        public Builder maxLinearSpeed(float maxLinearSpeed) {
            steerable.maxLinearSpeed = maxLinearSpeed;
            return this;
        }

        public Builder maxLinearAcceleration(float maxLinearAcceleration) {
            steerable.maxLinearAcceleration = maxLinearAcceleration;
            return this;
        }

        public Builder maxAngularSpeed(float maxAngularSpeed) {
            steerable.maxAngularSpeed = maxAngularSpeed;
            return this; }

        public Builder maxAngularAcceleration(float maxAngularAcceleration) {
            steerable.maxAngularAcceleration = maxAngularAcceleration;
            return this;
        }

        public Builder zeroLinearSpeedThreshold(float zeroLinearSpeedThreshold) {
            steerable.zeroLinearSpeedThreshold = zeroLinearSpeedThreshold;
            return this;
        }

        public B2dSteerable build() {
            if (steerable.parent == null) {
                throw new IllegalArgumentException("Steerable object must have a parent!");
            }

            return steerable;
        }
    }


    /**
     * Calculate the steering to use, then apply the calculations
     * @param delta
     */
    public void update(float delta) {
        if (behavior != null) {
            behavior.calculateSteering(steerOutput);
            applySteering(delta);
        }
    }


    /**
     * Check for accelerations, and apply the calculated forces to the body as needed
     * @param delta
     */
    private void applySteering(float delta) {
        boolean anyAccel = false;

        // Apply linear force - depending on whether or not pseudophysics are being used (useForce)
        if (!steerOutput.linear.isZero()) {
            Vector2 force = steerOutput.linear.scl(delta);
            // Pseudo-physics: add the calculated force to current velocity, instead of setting velocity directly
            if (useForce) {
                getBody().applyForceToCenter(force, true);
            } else {
                getBody().setLinearVelocity(
                        getBody().getLinearVelocity().x + force.x,
                        getBody().getLinearVelocity().y + force.y
                );
            }
            anyAccel = true;
        }

        // Apply angular force
        if (steerOutput.angular != 0) {
            getBody().applyTorque(steerOutput.angular * delta, true);
            anyAccel = true;
        } else {
            if (!getLinearVelocity().isZero()) {
                float newAngle = vectorToAngle(getLinearVelocity());
                getBody().setAngularVelocity((newAngle - getAngularVelocity()) * delta);
                getBody().setTransform(getBody().getPosition(), newAngle);
            }
        }

        // If there are any new accelerations, cap the maximum velocities
        if (anyAccel) {
            // Cap linear velocity
            Vector2 linVel = getLinearVelocity();
            float currentSpeedSquared = linVel.len2();
            if (currentSpeedSquared > maxLinearSpeed * maxLinearSpeed) {
                getBody().setLinearVelocity(linVel.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquared)));
            }

            // Cap angular velocity
            if (getBody().getAngularVelocity() > maxAngularSpeed) {
                getBody().setAngularVelocity(maxAngularSpeed);
            }
        }
    }


    public Body getBody() {
        return parent.getBody();
    }

    public SteeringBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(SteeringBehavior behavior) {
        this.behavior = behavior;
    }

    public Location<Vector2> getLocation() {
        return new B2dLocation(getBody().getPosition());
    }

    @Override
    public Vector2 getLinearVelocity() {
        return parent.getBody().getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return parent.getBody().getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.zeroLinearSpeedThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return parent.getBody().getPosition();
    }

    @Override
    public float getOrientation() {
        return parent.getBody().getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        parent.getBody().setTransform(parent.getBody().getPosition(), orientation);
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
