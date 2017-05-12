package com.greenbatgames.lagoon.util;

/**
 * Created by Quiv on 02-05-2017.
 */
public class Utils {

    public static boolean almostEqualTo (float first, float second, float variance) {
        return Math.abs(first - second) < variance;
    }
}
