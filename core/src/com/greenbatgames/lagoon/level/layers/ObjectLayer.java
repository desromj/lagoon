package com.greenbatgames.lagoon.level.layers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.greenbatgames.lagoon.enemy.enemies.Crawler;
import com.greenbatgames.lagoon.entity.MapItem;
import com.greenbatgames.lagoon.entity.Transition;
import com.greenbatgames.lagoon.level.Level;
import com.greenbatgames.lagoon.level.LevelMapLayer;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Utils;

public class ObjectLayer implements LevelMapLayer {
    @Override
    public String getLayerName() {
        return "object";
    }

    @Override
    public void processLayer(Level level, MapLayer mapLayer, String mapName, String transitionPointName) {

        Player player = null;

        try {
            player = Utils.player();
        } catch (Exception ex) {}

        // Loop through all map objects on the layer
        for (MapObject mapObject : mapLayer.getObjects()) {

            // Grab the properties and type of the current object
            MapProperties props = mapObject.getProperties();

            // Load items - checking if they have already been collected by the player
            if (props.get("type").equals("item")) {

                Integer id = props.get("id", Integer.class);

                // Skip creating the mapItem if it is already picked up
                if (player != null) {
                    if (player.inventoryHistory().isRecorded(id, mapName)) {
                        continue;
                    }
                }

                MapItem mapItem = new MapItem(
                        props.get("x", Float.class),
                        props.get("y", Float.class),
                        props.get("width", Float.class),
                        props.get("height", Float.class),
                        id,
                        mapName,
                        mapObject.getName()
                );

                level.getStage().addActor(mapItem);
            }

            // Load enemies
            if (props.get("type").equals("enemy")) {
                if (mapObject.getName().equals("crawler")) {
                    Crawler crawler = new Crawler(
                            props.get("x", Float.class),
                            props.get("y", Float.class),
                            props.get("width", Float.class),
                            props.get("height", Float.class)
                    );

                    level.getStage().addActor(crawler);
                }
            }

            // Load transition points
            if (props.get("type").equals("transition")) {

                // Add the transition points to the level
                Transition transition = new Transition(
                        props.get("x", Float.class),
                        props.get("y", Float.class),
                        props.get("width", Float.class),
                        props.get("height", Float.class),
                        mapObject.getName(),
                        mapName,
                        props.get("destMap", String.class),
                        props.get("destPoint", String.class),
                        props.containsKey("requires") ? props.get("requires", String.class) : ""
                );

                level.getStage().addActor(transition);

                // Make our player and set position to the passed transition point
                // If player already exists in a level, move that instance to the next map
                // Otherwise initialize the player
                if (mapObject.getName().equals(transitionPointName)) {

                    if (player == null) {
                        player = new Player(
                                props.get("x", Float.class) + props.get("width", Float.class) / 2.0f,
                                props.get("y", Float.class),
                                Constants.PLAYER_RADIUS,
                                Constants.PLAYER_RADIUS * 2f
                        );
                    } else {
                        player.setGamePosition(
                                props.get("x", Float.class) + props.get("width", Float.class) / 2.0f,
                                props.get("y", Float.class)
                        );
                        player.getPhysicsLoader().load(player);
                    }

                    level.getStage().addActor(player);
                    level.setPlayer(player);
                }

            }
        }
    }
}
