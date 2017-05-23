package com.greenbatgames.lagoon.level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.greenbatgames.lagoon.entity.Terrain;
import com.greenbatgames.lagoon.entity.Transition;
import com.greenbatgames.lagoon.entity.Water;
import com.greenbatgames.lagoon.enemy.enemies.Crawler;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

/**
 * Created by Quiv on 23-02-2017.
 */

public class LevelLoader {

    private LevelLoader() {}

    public static final String TAG = LevelLoader.class.getSimpleName();

    public static Level loadLevel(String mapName, String pointName) {

        Player player = null;

        try {
            player = GameScreen.level().getPlayer();
        } catch (Exception ex) {}

        // Initialize the level to load
        Level loadedLevel = new Level();
        GameScreen.getInstance().setLevel(loadedLevel);

        // Load the tile map and iterate through the layers to populate the level with objects
        TiledMap tiledMap = new TmxMapLoader().load("maps/" + mapName + ".tmx");
        loadedLevel.setTiledMap(tiledMap);

        // Loop through all relevant object layers in the TiledMap
        for (MapLayer mapLayer: tiledMap.getLayers()) {

            /*
                Collision and Terrain generation layer
            */

            if (mapLayer.getName().compareTo("collision") == 0) {

                // Loop through all map objects on the layer
                for (MapObject mapObject: mapLayer.getObjects()) {

                    // Grab the properties and type of the current object
                    MapProperties props = mapObject.getProperties();

                    /** Load the water objects */
                    if (mapObject.getName() != null && mapObject.getName().equals("water")) {
                        Water water = new Water(
                                props.get("x", Float.class),
                                props.get("y", Float.class),
                                props.get("width", Float.class),
                                props.get("height", Float.class)
                        );

                        loadedLevel.stage.addActor(water);
                    }

                    /** Generate Terrain for polyline objects */
                    else if (mapObject instanceof PolylineMapObject) {
                        PolylineMapObject plmo = (PolylineMapObject) mapObject;
                        float [] verts = plmo.getPolyline().getVertices();

                        // Determine the width and height, as these are not present in the Tiled program
                        float
                                xMin = verts[0],
                                xMax = verts[0],
                                yMin = verts[1],
                                yMax = verts[1];

                        for (int i = 2; i < verts.length; i += 2) {
                            if (verts[i] < xMin) xMin = verts[i];
                            if (verts[i] > xMax) xMax = verts[i];
                            if (verts[i+1] < yMin) yMin = verts[i+1];
                            if (verts[i+1] > yMax) yMax = verts[i+1];
                        }

                        Terrain terrain = new Terrain(
                                props.get("x", Float.class),
                                props.get("y", Float.class),
                                xMax - xMin,
                                yMax - yMin,
                                verts
                        );

                        loadedLevel.stage.addActor(terrain);
                    }
                }
            }

            /*
                Interactive object layer
            */

            if (mapLayer.getName().compareTo("object") == 0) {

                // Loop through all map objects on the layer
                for (MapObject mapObject : mapLayer.getObjects()) {

                    // Grab the properties and type of the current object
                    MapProperties props = mapObject.getProperties();

                    if (props.get("type").equals("enemy")) {
                        if (mapObject.getName().equals("crawler")) {
                            Crawler crawler = new Crawler(
                                    props.get("x", Float.class),
                                    props.get("y", Float.class),
                                    props.get("width", Float.class),
                                    props.get("height", Float.class)
                            );

                            loadedLevel.stage.addActor(crawler);
                        }
                    }

                    if (props.get("type").equals("transition")) {

                        // Add the transition points to the level
                        Transition transition = new Transition(
                                props.get("x", Float.class),
                                props.get("y", Float.class),
                                props.get("width", Float.class),
                                props.get("height", Float.class),
                                mapObject.getName(),
                                props.get("destMap", String.class),
                                props.get("destPoint", String.class)
                        );

                        loadedLevel.stage.addActor(transition);

                        // Make our player and set position to the passed transition point
                        // If player already exists in a level, move that instance to the next map
                        // Otherwise initialize the player
                        if (mapObject.getName().equals(pointName)) {

                            if (player == null) {
                                player = new Player(
                                        props.get("x", Float.class) + props.get("width", Float.class) / 2.0f,
                                        props.get("y", Float.class),
                                        Constants.PLAYER_RADIUS,
                                        Constants.PLAYER_RADIUS * 2f
                                );
                            } else {
                                player.setPosition(
                                        props.get("x", Float.class) + props.get("width", Float.class) / 2.0f,
                                        props.get("y", Float.class)
                                );
                                player.getPhysicsLoader().load(player);
                            }

                            loadedLevel.stage.addActor(player);
                            loadedLevel.setPlayer(player);
                        }

                    }
                }
            }
        }

        return loadedLevel;
    }
}
