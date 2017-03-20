package com.greenbatgames.lagoon.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Enums;

/**
 * Created by Quiv on 23-01-2017.
 */

public class Player extends PhysicsBody {

    private PlayerComponent mover;

    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
        getPhysicsLoader().load(this);

        // Initialize components and assets
        mover = new PlayerMoveComponent(this);
    }

    @Override
    public PhysicsLoader getPhysicsLoader() {
        return new PlayerPhysicsLoader();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Run Component updates in sequence, and break through
        // any later updates if we receive a returned request to
        do {
            if (!mover.update(delta)) break;
        } while (false);

        // Ensure the player is always ready to respond to physics collisions
        if (body != null) {
            body.setAwake(true);
        }
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO: Draw the assets here
    }



    /*
        Getters and Setters
     */

    public Fixture getFixture(Enums.PlayerFixtures type) {
        for (Fixture fix: body.getFixtureList())
            if (fix.getUserData() == type)
                return fix;
        return null;
    }

    public boolean fixtureIsEnabled(Enums.PlayerFixtures type) {
        Fixture fixture = getFixture(type);

        if (fixture.isSensor())
            return false;

        // TODO: If we're crouching, crouching is enabled. Otherwise, normal body fixture is enabled


        return false;
    }

    public boolean isCollisionDisabled() {
        // TODO: Replace with movement checks return mover.isCollisionDisabled();
        return false;
    }

    public boolean isJumpButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_JUMP);
    }

    public boolean isClimbButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_ATTACK);
    }

    public boolean isMoveButtonHeld() { return Gdx.input.isKeyPressed(Constants.KEY_RIGHT) || Gdx.input.isKeyPressed(Constants.KEY_LEFT); }

}
