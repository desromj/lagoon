package com.greenbatgames.lagoon.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

public class CirclePhysicsLoader implements PhysicsLoader  {

    private PhysicsBody parent;
    private boolean isSensor;
    private BodyDef.BodyType bodyType;

    public CirclePhysicsLoader(PhysicsBody parent, boolean isSensor, BodyDef.BodyType bodyType) {
        this.parent = parent;
        this.isSensor = isSensor;
        this.bodyType = bodyType;
    }

    @Override
    public void load(PhysicsBody parent) {
        // Body
        NewPhysicsBody newBody = new NewPhysicsBody();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = this.bodyType;

        // Set position of the body by scaling the current position
        bodyDef.position.set(
                (parent.getX() + parent.getWidth() / 2f) / Constants.PTM,
                (parent.getY() + parent.getHeight() / 2f) / Constants.PTM
        );

        newBody.setBodyDef(bodyDef, parent);

        // Fixtures
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.CRAWLER_RADIUS / Constants.PTM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = this.isSensor;

        newBody.addFixtureDef(fixtureDef);
        newBody.addShapeToDispose(shape);

        GameScreen.level().queuePhysicsBodyToAdd(newBody);
    }
}
