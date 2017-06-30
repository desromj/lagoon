package com.greenbatgames.lagoon.player.components;

import com.greenbatgames.lagoon.LagoonGame;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;
import com.greenbatgames.lagoon.screen.StartScreen;
import com.greenbatgames.lagoon.util.Constants;

public class ComponentManager extends PlayerComponent {

    private InventoryComponent inventory;
    private InventoryHistoryComponent inventoryHistory;
    private TransitionHistoryComponent transitionHistory;
    private TooltipComponent tooltip;
    private HealthComponent health;
    private TransitionComponent transitioner;
    private MoveComponent mover;
    private ClimbComponent climber;
    private WedgeComponent wedger;
    private SwimComponent swimmer;

    public ComponentManager(Player player) {
        super(player);

        // Initialize components and assets
        inventory = new InventoryComponent(player);
        inventoryHistory = new InventoryHistoryComponent(player);
        transitionHistory = new TransitionHistoryComponent(player);
        tooltip = new TooltipComponent(player);
        health = new HealthComponent(player, Constants.PLAYER_STARTING_HEALTH, Constants.PLAYER_STARTING_HEALTH);
        transitioner = new TransitionComponent(player);
        climber = new ClimbComponent(player);
        swimmer = new SwimComponent(player);
        wedger = new WedgeComponent(player);
        mover = new MoveComponent(player);
    }

    @Override
    public boolean update(float delta) {

        // Run Component updates in sequence, and break through
        // any later updates if we receive a returned request to
        do {
            /*
                Components which do not require a constant update:
                    inventory
                    inventoryHistory
                    transitionHistory
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
        if (health.isDead()) {
            LagoonGame.setScreen(StartScreen.class);
            health.healFully();
            inventoryHistory.reset();
        }

        return true;
    }


    /*
        Getters and Setters
     */

    public InventoryComponent inventory() { return inventory; }
    public InventoryHistoryComponent inventoryHistory() { return inventoryHistory; }
    public TransitionHistoryComponent transitionHistory() { return transitionHistory; }
    public TooltipComponent tooltip() {return tooltip; }
    public HealthComponent health() { return health; }
    public TransitionComponent transitioner() { return transitioner; }
    public MoveComponent mover() { return mover; }
    public ClimbComponent climber() { return climber; }
    public WedgeComponent wedger() { return wedger; }
    public SwimComponent swimmer() { return swimmer; }

}
