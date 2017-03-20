package com.greenbatgames.lagoon.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Quiv on 15-03-2017.
 */
public class NewPhysicsBody {

    BodyDef bodyDef;
    Object bodyUserData;

    List<FixtureDef> fixtureDefList;
    List<Object> fixtureUserDataList;

    List<Shape> shapesToDispose;

    public NewPhysicsBody() {
        bodyDef = null;
        bodyUserData = null;
        fixtureDefList = new LinkedList<>();
        fixtureUserDataList = new LinkedList<>();
        shapesToDispose = new LinkedList<>();
    }

    public boolean isValid() {
        if (bodyDef == null) return false;
        if (fixtureDefList.isEmpty()) return false;

        return true;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        setBodyDef(bodyDef, null);
    }

    public void setBodyDef(BodyDef bodyDef, Object userData) {
        this.bodyDef = bodyDef;
        this.bodyUserData = userData;
    }

    public Object getBodyUserData() {
        return bodyUserData;
    }

    public void setBodyUserData(Object bodyUserData) {
        this.bodyUserData = bodyUserData;
    }

    public void addFixtureDef(FixtureDef def) {
        addFixtureDef(def, null);
    }

    public void addFixtureDef(FixtureDef def, Object userData) {
        fixtureDefList.add(def);
        fixtureUserDataList.add(userData);
    }

    public List<FixtureDef> getFixtureDefList() {
        return fixtureDefList;
    }

    public List<Object> getFixtureUserDataList() {
        return fixtureUserDataList;
    }

    public void addShapeToDispose(Shape shape) {
        shapesToDispose.add(shape);
    }

    public List<Shape> getShapesToDispose() { return shapesToDispose; }
}
