package com.greenbatgames.lagoon.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Align;
import com.greenbatgames.lagoon.LagoonGame;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.screen.StartScreen;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Enums;

import java.util.LinkedList;
import java.util.List;

public class Player extends PhysicsBody {

    private PlayerHealthComponent health;
    private PlayerTransitionComponent transitioner;
    private PlayerMoveComponent mover;
    private PlayerClimbComponent climber;
    private PlayerWedgeComponent wedger;
    private PlayerSwimComponent swimmer;

    private BitmapFont font;

    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
        getPhysicsLoader().load(this);

        // Initialize components and assets
        health = new PlayerHealthComponent(this, Constants.PLAYER_STARTING_HEALTH, Constants.PLAYER_STARTING_HEALTH);
        transitioner = new PlayerTransitionComponent(this);
        climber = new PlayerClimbComponent(this);
        swimmer = new PlayerSwimComponent(this);
        wedger = new PlayerWedgeComponent(this);
        mover = new PlayerMoveComponent(this);

        font = new BitmapFont(Gdx.files.internal("fonts/arial-grad.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(1f);
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
            if (!health.update(delta)) break;
            if (!transitioner.update(delta)) break;
            if (!climber.update(delta)) break;
            if (!swimmer.update(delta)) break;
            if (!wedger.update(delta)) break;
            if (!mover.update(delta)) break;
        } while (false);

        // If dead, reload the game
        if (health().isDead()) {
            LagoonGame.setScreen(StartScreen.class);
        }

        // Ensure the player is always ready to respond to physics collisions
        if (body != null) {
            body.setAwake(true);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO: Draw the assets here

        // Draw the health numerically until a GUI is made
        font.draw(
                batch,
                "HP: " + health.getHealth() + " / " + health.getMaxHealth(),
                this.getX(),
                this.getY() + this.getHeight() * 4f,
                0,
                Align.center,
                false
        );
    }

    public Fixture getFixture(Enums.PlayerFixtures type) {
        for (Fixture fix: body.getFixtureList())
            if (fix.getUserData() == type)
                return fix;
        return null;
    }

    /**
     * Checks whether or not the passed fixture is currently active, and whether or not it
     * should be considered in collisions
     * @param fixture The Fixture object to test
     * @return true if the Fixture is enabled and should be affected, false if not
     */
    public boolean fixtureIsEnabled(Fixture fixture) {

        // Ensure the fixture is within this object first
        if (fixture.getBody().getUserData() != this)
            return false;

        // If we're crouching, crouching is enabled. Otherwise, normal body fixture is enabled
        if (mover.isCrouching() && (fixture.getUserData() == Enums.PlayerFixtures.CROUCH_BODY)) {
            return true;
        } else if (!mover.isCrouching()
                && ((fixture.getUserData() == Enums.PlayerFixtures.BODY)
                    || fixture.getUserData() == Enums.PlayerFixtures.BASE)) {
            return true;
        }

        // All sensors are active
        if (fixture.isSensor()) {
            return true;
        }

        return false;
    }

    public boolean fixtureIsEnabled(Enums.PlayerFixtures type) {
        Fixture fixture = getFixture(type);
        return fixtureIsEnabled(fixture);
    }

    /*
        Getters and Setters
     */

    public PlayerHealthComponent health() { return health; }
    public PlayerTransitionComponent transitioner() { return transitioner; }
    public PlayerMoveComponent mover() { return mover; }
    public PlayerClimbComponent climber() { return climber; }
    public PlayerWedgeComponent wedger() { return wedger; }
    public PlayerSwimComponent swimmer() { return swimmer; }

    public boolean isJumpButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_JUMP);
    }
    public boolean isClimbButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_ATTACK);
    }
    public boolean isMoveButtonHeld() { return Gdx.input.isKeyPressed(Constants.KEY_RIGHT) || Gdx.input.isKeyPressed(Constants.KEY_LEFT); }
    public boolean isUpOrDownButtonHeld() { return Gdx.input.isKeyPressed(Constants.KEY_UP) || Gdx.input.isKeyPressed(Constants.KEY_DOWN); }
}
