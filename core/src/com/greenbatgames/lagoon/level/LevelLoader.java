package com.greenbatgames.lagoon.level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.greenbatgames.lagoon.level.layers.CollisionLayer;
import com.greenbatgames.lagoon.level.layers.ObjectLayer;
import com.greenbatgames.lagoon.screen.GameScreen;

import java.util.LinkedList;
import java.util.List;

public class LevelLoader {

    private LevelLoader() {}

    public static final String TAG = LevelLoader.class.getSimpleName();

    /**
     * List contains all layers, in insertion order, to be loaded by any given map
     */
    private static final List<LevelMapLayer> LAYERS_TO_LOAD = new LinkedList<>();

    static {
        LAYERS_TO_LOAD.add(new CollisionLayer());
        LAYERS_TO_LOAD.add(new ObjectLayer());
    }

    public static Level loadLevel(String mapName, String transitionPointName) {

        // Initialize the level to load
        Level loadedLevel = new Level();
        GameScreen.getInstance().setLevel(loadedLevel);

        // Load the tile map and iterate through the layers to populate the level with objects
        TiledMap tiledMap = new TmxMapLoader().load("maps/" + mapName + ".tmx");
        loadedLevel.setTiledMap(tiledMap);

        // Loop through all relevant object layers in the TiledMap
        for (MapLayer mapLayer: tiledMap.getLayers()) {
            LAYERS_TO_LOAD.stream()
                    .filter(layer -> layer.getLayerName().equals(mapLayer.getName()))
                    .forEach(layer -> layer.processLayer(
                            loadedLevel,
                            mapLayer,
                            mapName,
                            transitionPointName
                    ));
        }

        return loadedLevel;
    }
}
