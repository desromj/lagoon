package com.greenbatgames.lagoon.level;

import com.badlogic.gdx.maps.MapLayer;

public interface LevelMapLayer {
    String getLayerName();
    void processLayer(Level level, MapLayer mapLayer, String mapName, String transitionPointName);
}
