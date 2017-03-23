package com.greenbatgames.lagoon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.greenbatgames.lagoon.physics.NewPhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

/**
 * Created by Quiv on 23-02-2017.
 */

public class Terrain extends PhysicsBody {

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

            // TODO: Position needs to be set correcly. Hardcoded value is bound to fail later
            bodyDef.position.set(
                    parent.getX() / Constants.PTM,
                    (parent.getY() - parent.getHeight() * 0.75f) / Constants.PTM
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

            newBody.addFixtureDef(fixtureDef);
            newBody.addShapeToDispose(shape);

            GameScreen.level().queuePhysicsBodyToAdd(newBody);
        };
    }
}
