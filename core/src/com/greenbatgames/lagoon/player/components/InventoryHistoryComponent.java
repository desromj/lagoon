package com.greenbatgames.lagoon.player.components;

import com.greenbatgames.lagoon.entity.MapItem;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;

import java.util.LinkedList;
import java.util.List;

public class InventoryHistoryComponent extends PlayerComponent {

    public static final String TAG = InventoryHistoryComponent.class.getSimpleName();

    class PickedUpItem {
        Integer id;
        String name;

        public PickedUpItem(MapItem mapItem) {
            id = mapItem.getId();
            name = mapItem.getMapName();
        }

        public PickedUpItem(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PickedUpItem that = (PickedUpItem) o;

            if (!id.equals(that.id)) return false;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }

    List<PickedUpItem> pickedUpItems;

    public InventoryHistoryComponent(Player player) {
        super(player);
        pickedUpItems = new LinkedList<>();
    }

    public void record(MapItem mapItem) {
        pickedUpItems.add(new PickedUpItem(mapItem));
    }

    public void record(Integer id, String mapName) {
        pickedUpItems.add(new PickedUpItem(id, mapName));
    }

    public boolean isRecorded(MapItem mapItem) {
        PickedUpItem pui = new PickedUpItem(mapItem);
        return this.containsItem(pui);
    }

    public boolean isRecorded(Integer id, String mapName) {
        PickedUpItem pui = new PickedUpItem(id, mapName);
        return this.containsItem(pui);
    }

    public void reset() {
        pickedUpItems.clear();
    }

    private boolean containsItem(PickedUpItem pickedUpItem) {
        return pickedUpItems.stream()
                .anyMatch(pui -> pui.equals(pickedUpItem));
    }

    @Override
    public boolean update(float delta) {
        return true;
    }
}
