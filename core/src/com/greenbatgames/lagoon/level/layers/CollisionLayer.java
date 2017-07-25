package com.greenbatgames.lagoon.level.layers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.greenbatgames.lagoon.entity.Terrain;
import com.greenbatgames.lagoon.entity.Water;
import com.greenbatgames.lagoon.level.Level;
import com.greenbatgames.lagoon.level.LevelMapLayer;

/**
 * Collision and Terrain generation layer
 */
public class CollisionLayer implements LevelMapLayer {
    @Override
    public String getLayerName() {
        return "collision";
    }

    @Override
    public void processLayer(Level level, MapLayer mapLayer, String mapName, String transitionPointName) {
        // Loop through all map objects on the layer
        for (MapObject mapObject: mapLayer.getObjects()) {

            // Grab the properties and type of the current object
            MapProperties props = mapObject.getProperties();

            // Load the water objects
            if (mapObject.getName() != null && mapObject.getName().equals("water")) {
                Water water = new Water(
                        props.get("x", Float.class),
                        props.get("y", Float.class),
                        props.get("width", Float.class),
                        props.get("height", Float.class)
                );

                level.getStage().addActor(water);
            }

            // Generate Terrain for polyline objects
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

                level.getStage().addActor(terrain);
            }
        }
    }
}
