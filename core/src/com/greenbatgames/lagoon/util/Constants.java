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

    public static final float TILE_WIDTH = 64f; // Tiles are 64x64 pixels


    // User controls - note they are not final
    public static int KEY_QUIT = Input.Keys.ESCAPE;
    public static int KEY_RESTART = Input.Keys.R;

    public static int KEY_UP = Input.Keys.UP;
    public static int KEY_DOWN = Input.Keys.DOWN;
    public static int KEY_RIGHT = Input.Keys.RIGHT;
    public static int KEY_LEFT = Input.Keys.LEFT;
    public static int KEY_JUMP = Input.Keys.Z;
    public static int KEY_ATTACK = Input.Keys.X;
    public static int KEY_WEDGE = Input.Keys.X;

    public static int KEY_CROUCH = Input.Keys.CONTROL_LEFT;

    // Physics Values
    public static final float PTM = 80f;
    public static final Vector2 GRAVITY = new Vector2(0f, -32f);


    // Player Values
    public static final float PLAYER_RADIUS = WORLD_WIDTH / 50f;
    public static final float PLAYER_JUMP_RECOVERY = 0.1f; // time before able to jump again
    public static final float PLAYER_DENSITY = 1000f;   // 1000 g / kg m^3
    public static final float PLAYER_GROUND_FRICTION = 0f;

    public static final float PLAYER_MIN_CLIMB_RATIO = 0.5f;    // climb between min and max percent
    public static final float PLAYER_MAX_CLIMB_RATIO = 1.0f;
    public static final float PLAYER_CLIMB_TIME = 0.5f;
    public static final float PLAYER_HOLD_BREATH_TIME = 20f;

    public static final float PLAYER_STROKE_PERIOD = 1f;
    public static final float PLAYER_WATER_MOVEMENT_DAMPEN = 0.96f;
    public static final float PLAYER_STROKE_MAGNITUDE = 15000f;

    public static final int PLAYER_STARTING_HEALTH = 6;
    public static final float PLAYER_DAMAGE_RECOVERY_TIME = 1.5f;

    public static final float FALL_VELOCITY_DAMAGE_THRESHOLD = -25f;    // negative Y velocity player can land from
    public static final int FALL_DAMAGE = 1;

    public static final float PLAYER_SWIM_FIXTURE_Y_OFFSET = TILE_WIDTH * 1.5f / PTM;
    public static final float PLAYER_WATER_ENTRY_VARIANCE = (TILE_WIDTH / 5f) / PTM;

    // Enemy Values
    public static final int CRAWLER_HEALTH = 2;
    public static final float CRAWLER_RADIUS = WORLD_WIDTH / 80f;
    public static final float CRAWLER_MAX_SPEED = 2f;
    public static final int CRAWLER_CONTACT_DAMAGE = 1;

    public static final int BAT_HEALTH = 1;
    public static final float BAT_RADIUS = WORLD_WIDTH / 120f;
    public static final float BAT_MAX_SPEED = 4f;
    public static final int BAT_CONTACT_DAMAGE = 1;

    // Physics Values
    public static final Vector2 PLAYER_MOVE_SPEED = new Vector2(5.0f, 0);
    public static final Vector2 PLAYER_CROUCH_MOVE_SPEED = new Vector2(2.0f, 0);
    public static final float PLAYER_WEDGE_MOVE_SPEED = 2.0f;

    // Allows player to jump vertically 3 tiles, and horizontally 4-5 tiles
    public static final Vector2 PLAYER_JUMP_IMPULSE = new Vector2(0, 35000.0f);
    public static final Vector2 PLAYER_KNOCKBACK_IMPULSE = new Vector2(16000f, 8000f);
    public static final float PLAYER_KNOCKBACK_TIME = 0.2f;

    public static final float HORIZONTAL_MOVE_DAMPEN = 0.5f;

    //Light values
    public static final float LIGHTS_AMBIENT_LEVEL = 0.2f;
    public static final float LIGHTS_FLICKER_OFF_TIME = 0.1f;
    public static final float LIGHTS_FLICKER_ON_TIME = 1f;

    // HUD values
    public static final float START_SCREEN_TITLE_SCALE = 2.0f;
    public static final float START_SCREEN_SUBTITLE_SCALE = 1.0f;

    public static final float FADE_OUT_TEXT_FADE_TIME = 2.0f;
    public static final float FADE_OUT_TEXT_Y_VELOCITY = 1.0f;

    // Camera controls
    public static final float CHASE_CAM_MAX_VELOCITY = WORLD_WIDTH / 160.0f;
    public static final float CHASE_CAM_THRESHOLD = WORLD_WIDTH / 200.0f;
    public static final float CHASE_CAM_MAX_VELOCITY_MANUAL = WORLD_WIDTH * 2.0f;
    public static final float CHASE_CAM_OFFSET = WORLD_WIDTH / 9.0f;
}
