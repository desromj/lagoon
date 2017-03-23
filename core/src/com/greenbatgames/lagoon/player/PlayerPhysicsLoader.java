package com.greenbatgames.lagoon.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.greenbatgames.lagoon.physics.NewPhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.PhysicsLoader;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Enums;

/**
 * Created by Quiv on 15-03-2017.
 */
public class PlayerPhysicsLoader implements PhysicsLoader {

    @Override
    public void load(PhysicsBody parent) {

        NewPhysicsBody newPhysicsBody = new NewPhysicsBody();

        // Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                (parent.getX() + parent.getWidth() / 2.0f) / Constants.PTM,
                (parent.getY() + parent.getHeight() / 2.0f) / Constants.PTM
        );
        bodyDef.fixedRotation = true;

        newPhysicsBody.setBodyDef(bodyDef, parent);

        // Fixtures

        // Utility unit = one player radius expressed in Box2D units
        float b2Unit = Constants.PLAYER_RADIUS / Constants.PTM;

        // Circle base for walking (one unit radius, centre point is centre of body)
        {
            CircleShape shape = new CircleShape();
            shape.setRadius(b2Unit);
            shape.setPosition(new Vector2(0, 0));

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.friction = Constants.PLAYER_GROUND_FRICTION;
            fixtureDef.isSensor = false;

            newPhysicsBody.addFixtureDef(fixtureDef, Enums.PlayerFixtures.BASE);
            newPhysicsBody.addShapeToDispose(shape);
        }

        // Rectangle body for normal mass (2x4 units, offset 2 units up)
        {
            PolygonShape shape = new PolygonShape();

            shape.set(new float[]{
                    b2Unit, 0f,
                    b2Unit, b2Unit * 4f,
                    -b2Unit, b2Unit * 4f,
                    -b2Unit, 0f
            });

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.friction = Constants.PLAYER_GROUND_FRICTION;
            fixtureDef.isSensor = false;

            newPhysicsBody.addFixtureDef(fixtureDef, Enums.PlayerFixtures.BODY);
            newPhysicsBody.addShapeToDispose(shape);
        }

        // Rectangle body for crouched mass (4x2 units, offset 1 unit up)
        {
            PolygonShape shape = new PolygonShape();

            shape.set(new float[]{
                    2f * b2Unit, 0f,
                    2f * b2Unit, b2Unit * 2f,
                    -2f * b2Unit, b2Unit * 2f,
                    -2f * b2Unit, 0f
            });

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.friction = Constants.PLAYER_GROUND_FRICTION;
            fixtureDef.isSensor = false;

            newPhysicsBody.addFixtureDef(fixtureDef, Enums.PlayerFixtures.CROUCH_BODY);
            newPhysicsBody.addShapeToDispose(shape);
        }

        // Square sensor for floor contact (1x1, offset 1 unit down)
        {
            PolygonShape shape = new PolygonShape();

            shape.set(new float[]{
                    b2Unit * 0.5f, -b2Unit * 1.5f,
                    b2Unit * 0.5f, -b2Unit * 0.5f,
                    -b2Unit * 0.5f, -b2Unit * 0.5f,
                    -b2Unit * 0.5f, -b2Unit * 1.5f,
            });

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.isSensor = true;

            newPhysicsBody.addFixtureDef(fixtureDef, Enums.PlayerFixtures.GROUND_SENSOR);
            newPhysicsBody.addShapeToDispose(shape);
        }

        GameScreen.level().queuePhysicsBodyToAdd(newPhysicsBody);
    }

}
