package com.greenbatgames.lagoon.util;

import com.badlogic.gdx.math.Vector2;

public class Utils {

    public static boolean almostEqualTo (float first, float second, float variance) {
        return Math.abs(first - second) < variance;
    }


    public static float vectorTongle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }


    public static Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = (float) -Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }
}
