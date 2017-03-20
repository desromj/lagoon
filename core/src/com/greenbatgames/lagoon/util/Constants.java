package com.greenbatgames.lagoon.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Quiv on 23-01-2017.
 */

public class Constants {

    // Aspect ratios
    public static final float WORLD_WIDTH = 1600;
    public static final float WORLD_HEIGHT = WORLD_WIDTH * 4f / 5f;

    public static final float TILE_WIDTH = 32f; // Tiles are 32x32 pixels


    // User controls - note they are not final
    public static int KEY_QUIT = Input.Keys.ESCAPE;
    public static int KEY_RESTART = Input.Keys.R;

    public static int KEY_RIGHT = Input.Keys.RIGHT;
    public static int KEY_LEFT = Input.Keys.LEFT;
    public static int KEY_JUMP = Input.Keys.Z;
    public static int KEY_ATTACK = Input.Keys.X;

    // Physics Values
    public static final float PTM = 80f;
    public static final Vector2 GRAVITY = new Vector2(0f, -10f);


    // Player Values
    public static final float PLAYER_RADIUS = 16f;
    public static final float PLAYER_JUMP_RECOVERY = 0.25f; // time before able to jump again
    public static final float PLAYER_DENSITY = 1000f;   // 1000 g / kg m^3
    public static final float PLAYER_GROUND_FRICTION = 0f;

    public static final float PLAYER_MIN_CLIMB_RATIO = 0.5f;    // climb between min and max percent
    public static final float PLAYER_MAX_CLIMB_RATIO = 1.0f;
    public static final float PLAYER_CLIMB_TIME = 0.5f;

    public static final float PLAYER_MOVE_SPEED = WORLD_WIDTH / 160f;
    public static final float PLAYER_DASH_SPEED = PLAYER_MOVE_SPEED * 5.0f;
    public static final float PLAYER_DASH_DURATION = 0.1f;
    public static final float PLAYER_DASH_COOLDOWN = 1.3f;

    public static final Vector2 PLAYER_JUMP_IMPULSE = new Vector2(0f,
            2.2f * PLAYER_RADIUS * PLAYER_DENSITY);

    public static final float HORIZONTAL_MOVE_DAMPEN = 0.5f;

    //Light values
    public static final float LIGHTS_AMBIENT_LEVEL = 0.2f;
    public static final float LIGHTS_FLICKER_OFF_TIME = 0.1f;
    public static final float LIGHTS_FLICKER_ON_TIME = 1f;

    // HUD values
    public static final float START_SCREEN_TITLE_SCALE = 2.0f;
    public static final float START_SCREEN_SUBTITLE_SCALE = 1.0f;

    // Camera controls
    public static final float CHASE_CAM_MOVE_SPEED = WORLD_WIDTH * 2.0f;
    public static final float CHASE_CAM_X_LEEWAY = WORLD_WIDTH / 4f;
    public static final float CHASE_CAM_Y_LEEWAY = WORLD_WIDTH / 10f;


}
