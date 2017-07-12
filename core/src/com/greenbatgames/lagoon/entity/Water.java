package com.greenbatgames.lagoon.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.greenbatgames.lagoon.ai.B2dSteerable;
import com.greenbatgames.lagoon.physics.*;
import com.greenbatgames.lagoon.player.Player;

/**
 * Created by Quiv on 01-05-2017.
 */
public class Water extends PhysicsBody implements Swimmable {

    public Water(float x, float y, float width, float height) {
        super(x, y, width, height);
        getPhysicsLoader().load(this);
    }


    @Override
    public void startSwimming(PhysicsBody body) {
        if (body instanceof Player) {
            Player player = (Player) body;
            player.getBody().setLinearVelocity(
                    player.getBody().getLinearVelocity().x,
                    0f
            );
            player.mover().setCrouching(false);
            player.swimmer().treadWater(getY() + getHeight());
        }
    }


    @Override
    public void stopSwimming(PhysicsBody body) {
        if (body instanceof Player) {
            Player player = (Player) body;
            player.swimmer().stopSwimming();
        }
    }


    @Override
    protected PhysicsLoader getPhysicsLoader() {
        return new BoxPhysicsLoader(this, true, BodyDef.BodyType.StaticBody);
    }


    @Override
    protected B2dSteerable makeSteerable() {
        return new B2dSteerable.Builder()
                .parent(this)
                .build();
    }
}
