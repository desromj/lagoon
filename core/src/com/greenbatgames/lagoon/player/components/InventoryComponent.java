package com.greenbatgames.lagoon.player.components;

import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;

import java.util.LinkedHashSet;
import java.util.Set;

/*
    Need to allow:
        - adding items
        - checking if an item exists by name
        - getting a count of the requested item
        - getting an item by name
        - using an item (decrement and remove if count == 0)
 */
public class InventoryComponent extends PlayerComponent {

    class Item {
        String name;
        Integer count;

        public Item(String name, Integer count) {
            this.name = name;
            this.count = count;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
    }

    private Set<Item> items;

    public InventoryComponent(Player player) {
        super(player);
        items = new LinkedHashSet<>();
    }

    public int add(String itemName) {
        Item item;

        if (isInInventory(itemName)) {
            item = get(itemName);
            item.setCount(item.getCount() + 1);
        } else {
            item = new Item(itemName, 1);
            items.add(item);
        }

        return item.getCount();
    }

    public void subtract(String itemName) {
        items.stream()
                .filter(item -> item.getName().equals(itemName))
                .forEach(item -> {
                    item.setCount(item.getCount() - 1);
                    if (item.getCount() <= 0) {
                        items.remove(item);
                    }
                });
    }

    public int countOf(String itemName) {
        try {
            return items.stream()
                    .filter(item -> item.getName().equals(itemName))
                    .findFirst()
                    .get()
                    .getCount();
        } catch (Exception ex) {
            return 0;
        }
    }

    public boolean isInInventory(String itemName) {
        return countOf(itemName) > 0;
    }

    public boolean use(String itemName) {
        if (!isInInventory(itemName)) {
            return false;
        }

        // TODO: Possibly find the item and do any effects it has here

        subtract(itemName);
        return true;
    }

    public Item get(String itemName) {
        if (isInInventory(itemName)) {
            return items.stream()
                    .filter(item -> item.getName().equals(itemName))
                    .findFirst()
                    .get();
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Inventory Contents: \n");
        builder.append("--------------------\n");
        items.stream().forEach(item -> builder.append(item.getName() + ", Qty: " + item.getCount() + "\n"));
        return builder.toString();
    }

    @Override
    public boolean update(float delta) {
        return true;
    }
}
