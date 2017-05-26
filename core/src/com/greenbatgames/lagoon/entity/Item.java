package com.greenbatgames.lagoon.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.greenbatgames.lagoon.physics.BoxPhysicsLoader;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.screen.GameScreen;

public class Item extends PhysicsBody {

    private Integer id;
    private String mapName;

    public Item(float x, float y, float width, float height, Integer id, String mapName) {
        super(x, y, width, height);
        this.id = id;
        this.mapName = mapName;
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

    public void pickUp(Player player) {
        // Add item to inventory history
        player.inventoryHistory().record(this);
        GameScreen.level().queuePhysicsBodyToRemove(this);
        this.remove();

        // TODO: Maybe play a pickup sound or something

        // TODO: When implemented, actually pick the item up in the inventory as well
    }
}
