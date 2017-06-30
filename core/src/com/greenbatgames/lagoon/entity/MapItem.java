package com.greenbatgames.lagoon.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.greenbatgames.lagoon.animation.FadeOutText;
import com.greenbatgames.lagoon.physics.BoxPhysicsLoader;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.screen.GameScreen;

public class MapItem extends PhysicsBody {

    private Integer id;
    private String mapName;
    private String itemName;

    public MapItem(float x, float y, float width, float height, Integer id, String mapName, String itemName) {
        super(x, y, width, height);
        this.id = id;
        this.mapName = mapName;
        this.itemName = itemName;
        getPhysicsLoader().load(this);
    }

    @Override
    protected PhysicsLoader getPhysicsLoader() {
        return new BoxPhysicsLoader(this, true, BodyDef.BodyType.KinematicBody);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO: Draw the icon here, animated if need be
    }

    public Integer getId() {
        return id;
    }

    public String getMapName() {
        return mapName;
    }

    public String getItemName() { return itemName; }

    public void pickUp(Player player) {
        // Add item to inventory history
        player.inventoryHistory().record(this);
        GameScreen.level().queuePhysicsBodyToRemove(this);
        this.remove();

        // Create and add text to stage
        FadeOutText.create(
                this.getX(),
                this.getY() + this.getHeight() * 4f,
                String.format("Picked up -%s-", itemName)
        );

        // TODO: Maybe play a pickup sound or something

        // Actually pick the item up for the player inventory
        GameScreen.level().getPlayer().inventory().add(this.itemName);
    }
}
