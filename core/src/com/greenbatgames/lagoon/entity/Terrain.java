package com.greenbatgames.lagoon.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.greenbatgames.lagoon.physics.Climbable;
import com.greenbatgames.lagoon.physics.NewPhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

public class Terrain extends PhysicsBody implements Climbable {

    private float [] verts;

    public Terrain(float x, float y, float width, float height, float [] verts) {
        super(x, y, width, height);
        this.verts = verts;
        getPhysicsLoader().load(this);
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
            ChainShape shape = new ChainShape();

            // Edit the vertices to be considered in the Box2D engine
            for (int i = 0; i < verts.length; i++)
                verts[i] /= Constants.PTM;

            shape.createChain(verts);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.friction = 0.5f;

            newBody.addFixtureDef(fixtureDef);
            newBody.addShapeToDispose(shape);

            GameScreen.level().queuePhysicsBodyToAdd(newBody);
        };
    }

    @Override
    public float[] getVerts() {
        return verts;
    }
}
