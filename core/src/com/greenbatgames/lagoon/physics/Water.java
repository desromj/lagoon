package com.greenbatgames.lagoon.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

/**
 * Created by Quiv on 01-05-2017.
 */
public class Water extends PhysicsBody implements Swimmable {

    public Water(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void startSwimming(PhysicsBody body) {
        if (body instanceof Player) {
            Player player = (Player) body;
            player.swimmer().treadWater();
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
                    parent.getX() / Constants.PTM,
                    parent.getY() / Constants.PTM
            );

            newBody.setBodyDef(bodyDef, parent);

            // Fixtures
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                    parent.getWidth() / Constants.PTM,
                    parent.getHeight() / Constants.PTM
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
