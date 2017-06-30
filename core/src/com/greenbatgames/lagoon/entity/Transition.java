package com.greenbatgames.lagoon.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.greenbatgames.lagoon.animation.FadeOutText;
import com.greenbatgames.lagoon.physics.BoxPhysicsLoader;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.screen.GameScreen;

public class Transition extends PhysicsBody {

    private String name;
    private String mapName;
    private String destMap;
    private String destPoint;
    private String requires;


    public Transition(float x, float y, float width, float height,
                      String name, String mapName, String destMap, String destPoint) {
        this(x, y, width, height, name, mapName, destMap, destPoint, "");
    }


    /**
     * @param x Map x position for the transition object
     * @param y Map y position for the transition object
     * @param width Width of the transition object
     * @param height Height of the transition object
     * @param name Name of this transition object, as given in the map editor
     * @param mapName Name of the map this transition exists on
     * @param destMap Name of the map this transition moves the player to
     * @param destPoint Name of the transition point on the destMap to move the player to
     * @param requires Name of an item the transition requires to unlock it
     */
    public Transition(float x, float y, float width, float height, String name,
                      String mapName, String destMap, String destPoint, String requires) {
        super(x, y, width, height);
        this.name = name;
        this.mapName = mapName;
        this.destMap = destMap;
        this.destPoint = destPoint;
        this.requires = requires;
        getPhysicsLoader().load(this);
    }


    @Override
    protected PhysicsLoader getPhysicsLoader() {
        return new BoxPhysicsLoader(this, true, BodyDef.BodyType.StaticBody);
    }


    public void transition() {
        if (canBeUsed()) {
            Player player = GameScreen.level().getPlayer();

            // Transition if nothing is required, or the transition is already unlocked
            if (requires.isEmpty() || (player.transitionHistory().isUnlocked(this))) {
                GameScreen.getInstance().loadMap(destMap, destPoint);
            } else {
                player.inventory().use(requires);
                player.transitionHistory().record(this);
                FadeOutText.create(
                        player.getX(),
                        player.getY() + player.getHeight() * 4f,
                        String.format("Used -%s-", requires)
                );
            }
        } else {
            FadeOutText.create(
                    this.getX(),
                    this.getY() + this.getHeight(),
                    String.format("Requires -%s-", requires)
            );
        }
    }


    public boolean canBeUsed() {
        return requires.isEmpty()
                || GameScreen.level().getPlayer().transitionHistory().isUnlocked(this)
                || GameScreen.level().getPlayer().inventory().isInInventory(requires);
    }


    @Override
    public String getName() { return name; }
    public String getMapName() { return mapName; }
}
