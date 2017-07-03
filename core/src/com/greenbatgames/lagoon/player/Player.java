package com.greenbatgames.lagoon.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Align;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.player.components.*;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Enums;

import java.util.Arrays;

public class Player extends PhysicsBody {

    private ComponentManager manager;
    private BitmapFont font;


    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
        getPhysicsLoader().load(this);

        manager = new ComponentManager(this);

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

        manager.update(delta);

        // Ensure the player is always ready to respond to physics collisions
        if (body != null) {
            body.setAwake(true);
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO: Draw the assets here

        // Draw the tooltip for transitioning
        tooltip().draw(batch, parentAlpha);

        // Draw the health numerically until a GUI is made
        font.draw(
                batch,
                "HP: " + health().getHealth() + " / " + health().getMaxHealth(),
                this.getX(),
                this.getY() + this.getHeight() * 4f,
                0,
                Align.center,
                false
        );
    }


    public Fixture getFixture(Enums.PlayerFixtures type) {
        try {
            return Arrays.stream(body.getFixtureList().toArray())
                    .filter(fixture -> fixture.getUserData() == type)
                    .findFirst()
                    .get();
        } catch (Exception ex) {
            return null;
        }
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
        if (mover().isCrouching() && (fixture.getUserData() == Enums.PlayerFixtures.CROUCH_BODY)) {
            return true;
        } else if (!mover().isCrouching()
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

    public InventoryComponent inventory() { return manager.inventory(); }
    public InventoryHistoryComponent inventoryHistory() { return manager.inventoryHistory(); }
    public TransitionHistoryComponent transitionHistory() { return manager.transitionHistory(); }
    public TooltipComponent tooltip() {return manager.tooltip(); }
    public HealthComponent health() { return manager.health(); }
    public TransitionComponent transitioner() { return manager.transitioner(); }
    public MoveComponent mover() { return manager.mover(); }
    public ClimbComponent climber() { return manager.climber(); }
    public WedgeComponent wedger() { return manager.wedger(); }
    public SwimComponent swimmer() { return manager.swimmer(); }

    public boolean isJumpButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_JUMP);
    }
    public boolean isClimbButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_ATTACK);
    }
    public boolean isMoveButtonHeld() { return Gdx.input.isKeyPressed(Constants.KEY_RIGHT) || Gdx.input.isKeyPressed(Constants.KEY_LEFT); }
    public boolean isUpOrDownButtonHeld() { return Gdx.input.isKeyPressed(Constants.KEY_UP) || Gdx.input.isKeyPressed(Constants.KEY_DOWN); }
}
