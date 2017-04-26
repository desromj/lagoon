package com.greenbatgames.lagoon.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.greenbatgames.lagoon.entity.Terrain;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.screen.GameScreen;
import com.greenbatgames.lagoon.util.Constants;

import java.util.Iterator;

/**
 * Created by Quiv on 23-02-2017.
 */

public class LevelLoader {

    private LevelLoader() {}

    public static final String TAG = LevelLoader.class.getSimpleName();

    public static Level loadLevel(String filename) {

        // Initialize the level to load
        Level loadedLevel = new Level();
        GameScreen.getInstance().setLevel(loadedLevel);

        // Load the tile map and iterate through the layers to populate the level with objects
        TiledMap tiledMap = new TmxMapLoader().load(filename);
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

                    // Generate Terrain for polyline objects
                    if (mapObject instanceof PolylineMapObject) {
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

                    // Handle single player spawn... for now
                    // TODO: Replace with entry and exit points, which are named and linked across maps
                    if (mapObject.getName().equals("player-spawn")) {
                        Player player = new Player(
                                props.get("x", Float.class),
                                props.get("y", Float.class),
                                Constants.PLAYER_RADIUS,
                                Constants.PLAYER_RADIUS * 2f
                        );

                        loadedLevel.stage.addActor(player);
                        loadedLevel.setPlayer(player);
                    }
                }
            }
        }

        return loadedLevel;
    }
}
