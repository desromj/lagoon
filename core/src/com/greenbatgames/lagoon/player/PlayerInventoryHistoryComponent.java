package com.greenbatgames.lagoon.player;

import com.badlogic.gdx.Gdx;
import com.greenbatgames.lagoon.entity.Item;

import java.util.LinkedList;
import java.util.List;

public class PlayerInventoryHistoryComponent extends PlayerComponent {

    public static final String TAG = PlayerInventoryHistoryComponent.class.getSimpleName();

    class PickedUpItem {
        Integer id;
        String name;

        public PickedUpItem(Item item) {
            id = item.getId();
            name = item.getMapName();
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

    public PlayerInventoryHistoryComponent(Player player) {
        super(player);
        pickedUpItems = new LinkedList<>();
    }

    public void record(Item item) {
        pickedUpItems.add(new PickedUpItem(item));
    }

    public void record(Integer id, String mapName) {
        pickedUpItems.add(new PickedUpItem(id, mapName));
    }

    public boolean isRecorded(Item item) {
        PickedUpItem pui = new PickedUpItem(item);
        return this.containsItem(pui);
    }

    public boolean isRecorded(Integer id, String mapName) {
        PickedUpItem pui = new PickedUpItem(id, mapName);
        return this.containsItem(pui);
    }

    private boolean containsItem(PickedUpItem pickedUpItem) {
        for (PickedUpItem pui: pickedUpItems) {
            if (pui.equals(pickedUpItem)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean update(float delta) {
        return true;
    }
}
