package com.greenbatgames.lagoon.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Quiv on 15-03-2017.
 */
public class QueuedWorld {

    public static final String TAG = QueuedWorld.class.getSimpleName();

    World world;
    Queue<NewPhysicsBody> bodiesToAdd;
    Queue<Body> bodiesToRemove;
    Queue<Shape> shapesToDispose;

    public QueuedWorld(Vector2 gravity, boolean doSleep) {
        world = new World(gravity, doSleep);
        bodiesToAdd = new LinkedList<>();
        bodiesToRemove = new LinkedList<>();
        shapesToDispose = new LinkedList<>();
    }

    public void step(float delta) {
        world.step(delta, 6, 6);

        runQueuedChanges();
    }

    private void runQueuedChanges() {

        // Add bodies
        while (!bodiesToAdd.isEmpty()) {
            // Create the body and register it to the parent PhysicsBody
            NewPhysicsBody newBodyData = bodiesToAdd.poll();

            PhysicsBody newBodyUserData = (PhysicsBody) newBodyData.getBodyUserData();
            Body body = world.createBody(newBodyData.getBodyDef());
            body.setUserData(newBodyUserData);

            newBodyUserData.setBody(body);

            // Loop through the fixtures and set the data and user data of each
            List<FixtureDef> fixtures = newBodyData.getFixtureDefList();
            List<Object> userData = newBodyData.getFixtureUserDataList();

            for (int i = 0; i < fixtures.size(); i++) {
                FixtureDef fixtureDef = fixtures.get(i);
                Fixture fix = body.createFixture(fixtureDef);
                fix.setUserData(userData.get(i));
            }

            // List all used shapes from the new object as disposable
            for (Shape shape: newBodyData.getShapesToDispose()) {
                shapesToDispose.add(shape);
            }
        }

        // Remove bodies
        while (!bodiesToRemove.isEmpty()) {
            world.destroyBody(bodiesToRemove.poll());
        }

        // Dispose of any used shapes
        while (!shapesToDispose.isEmpty()) {
            shapesToDispose.poll().dispose();
        }
    }

    public void addToAdd(NewPhysicsBody toAdd) {
        bodiesToAdd.add(toAdd);
    }
    public void addToRemove(Body toRemove) {
        bodiesToRemove.add(toRemove);
    }

    public World world() { return world; }
}
