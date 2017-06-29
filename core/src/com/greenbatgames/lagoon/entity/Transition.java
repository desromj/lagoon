package com.greenbatgames.lagoon.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.greenbatgames.lagoon.animation.FadeOutText;
import com.greenbatgames.lagoon.physics.BoxPhysicsLoader;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.screen.GameScreen;

public class Transition extends PhysicsBody {

    private String name;
    private String destMap;
    private String destPoint;
    private String requires;

    public Transition(float x, float y, float width, float height, String name, String destMap, String destPoint) {
        this(x, y, width, height, name, destMap, destPoint, "");
    }

    public Transition(float x, float y, float width, float height, String name, String destMap, String destPoint, String requires) {
        super(x, y, width, height);
        this.name = name;
        this.destMap = destMap;
        this.destPoint = destPoint;
        getPhysicsLoader().load(this);
        this.requires = requires;
    }

    @Override
    protected PhysicsLoader getPhysicsLoader() {
        return new BoxPhysicsLoader(this, true, BodyDef.BodyType.StaticBody);
    }

    public void transition() {
        if (canBeUsed()) {
            GameScreen.level().getPlayer().inventory().use(requires);
            GameScreen.getInstance().loadMap(destMap, destPoint);

            // TODO: Remember that this transition has been unlocked, and don't require the item again when map reloads

        } else {
            FadeOutText.create(
                    this.getX(),
                    this.getY() + this.getHeight(),
                    "Requires '" + this.getItemRequired() + "'"
            );
        }
    }

    public boolean canBeUsed() {
        return requires.isEmpty()
                || (GameScreen.level().getPlayer().inventory().isInInventory(requires));
    }

    public String getItemRequired() {
        return requires;
    }
}
