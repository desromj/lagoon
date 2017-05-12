package com.greenbatgames.lagoon.entity;

import com.greenbatgames.lagoon.physics.BoxPhysicsLoader;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.screen.GameScreen;

public class Transition extends PhysicsBody {

    private String name;
    private String destMap;
    private String destPoint;

    public Transition(float x, float y, float width, float height, String name, String destMap, String destPoint) {
        super(x, y, width, height);
        this.name = name;
        this.destMap = destMap;
        this.destPoint = destPoint;
        getPhysicsLoader().load(this);
    }

    @Override
    protected PhysicsLoader getPhysicsLoader() {
        return new BoxPhysicsLoader(this, true);
    }

    public void transition() {
        GameScreen.getInstance().loadMap(destMap, destPoint);
    }
}
