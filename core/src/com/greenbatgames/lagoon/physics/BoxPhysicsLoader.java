package com.greenbatgames.lagoon.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

public class BoxPhysicsLoader implements PhysicsLoader {

    private PhysicsBody parent;
    private boolean isSensor;

    public BoxPhysicsLoader(PhysicsBody parent, boolean isSensor) {
        this.parent = parent;
        this.isSensor = isSensor;
    }

    @Override
    public void load(PhysicsBody parent) {
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
        fixtureDef.isSensor = this.isSensor;

        newBody.addFixtureDef(fixtureDef);
        newBody.addShapeToDispose(shape);

        GameScreen.level().queuePhysicsBodyToAdd(newBody);
    }
}
