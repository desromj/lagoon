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
import com.greenbatgames.lagoon.player.components.*;
import com.greenbatgames.lagoon.screen.StartScreen;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Enums;

public class Player extends PhysicsBody {

    private InventoryComponent inventory;
    private InventoryHistoryComponent inventoryHistory;
    private TooltipComponent tooltip;
    private HealthComponent health;
    private TransitionComponent transitioner;
    private MoveComponent mover;
    private ClimbComponent climber;
    private WedgeComponent wedger;
    private SwimComponent swimmer;

    private BitmapFont font;

    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
        getPhysicsLoader().load(this);

        // Initialize components and assets
        inventory = new InventoryComponent(this);
        inventoryHistory = new InventoryHistoryComponent(this);
        tooltip = new TooltipComponent(this);
        health = new HealthComponent(this, Constants.PLAYER_STARTING_HEALTH, Constants.PLAYER_STARTING_HEALTH);
        transitioner = new TransitionComponent(this);
        climber = new ClimbComponent(this);
        swimmer = new SwimComponent(this);
        wedger = new WedgeComponent(this);
        mover = new MoveComponent(this);

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
            /*
                Components which do not require a constant update:
                    inventory
                    inventoryHistory
             */

            // Components which will always update
            tooltip.update(delta);
            health.update(delta);

            // Components which can break regular program flow
            if (!transitioner.update(delta)) break;
            if (!climber.update(delta)) break;
            if (!swimmer.update(delta)) break;
            if (!wedger.update(delta)) break;
            if (!mover.update(delta)) break;
        } while (false);

        // If dead, reload the game
        if (health().isDead()) {
            LagoonGame.setScreen(StartScreen.class);
            health.healFully();
            inventoryHistory.reset();
        }

        // Ensure the player is always ready to respond to physics collisions
        if (body != null) {
            body.setAwake(true);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO: Draw the assets here

        // Draw the tooltip for transitioning
        tooltip.draw(batch, parentAlpha);

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

    public InventoryComponent inventory() { return inventory; }
    public InventoryHistoryComponent inventoryHistory() { return inventoryHistory; }
    public TooltipComponent tooltip() {return tooltip; }
    public HealthComponent health() { return health; }
    public TransitionComponent transitioner() { return transitioner; }
    public MoveComponent mover() { return mover; }
    public ClimbComponent climber() { return climber; }
    public WedgeComponent wedger() { return wedger; }
    public SwimComponent swimmer() { return swimmer; }

    public boolean isJumpButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_JUMP);
    }
    public boolean isClimbButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_ATTACK);
    }
    public boolean isMoveButtonHeld() { return Gdx.input.isKeyPressed(Constants.KEY_RIGHT) || Gdx.input.isKeyPressed(Constants.KEY_LEFT); }
    public boolean isUpOrDownButtonHeld() { return Gdx.input.isKeyPressed(Constants.KEY_UP) || Gdx.input.isKeyPressed(Constants.KEY_DOWN); }
}
