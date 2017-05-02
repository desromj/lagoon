package com.greenbatgames.lagoon.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.greenbatgames.lagoon.physics.NewPhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.physics.Swimmable;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

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
        return parent -> {
            // Body
            NewPhysicsBody newBody = new NewPhysicsBody();

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            // Set position of the body by scaling the current position
            bodyDef.position.set(
                    (parent.getX() + parent.getWidth() / 2f) / Constants.PTM,
                    (parent.getY() + parent.getHeight() / 2f) / Constants.PTM
            );

            newBody.setBodyDef(bodyDef, parent);

            // Fixtures
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                    (parent.getWidth() / 2f) / Constants.PTM,
                    (parent.getHeight() / 2f) / Constants.PTM
            );

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;

            newBody.addFixtureDef(fixtureDef);
            newBody.addShapeToDispose(shape);

            GameScreen.level().queuePhysicsBodyToAdd(newBody);
        };
    }
}
