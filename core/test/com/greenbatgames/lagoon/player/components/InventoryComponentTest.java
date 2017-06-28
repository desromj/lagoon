package com.greenbatgames.lagoon.player.components;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryComponentTest {

    InventoryComponent inv;

    @Before
    public void setUp() throws Exception {
        inv = new InventoryComponent(null);
        inv.add("Red Key");
        inv.add("Electric Powder");
        inv.add("Red Key");
        System.out.println(inv);
    }

    @Test
    public void isInInventory() throws Exception {
        assertTrue(inv.isInInventory("Electric Powder"));
    }

    @Test
    public void add() throws Exception {
        inv.add("Mind Plate");
        assertTrue(inv.isInInventory("Mind Plate"));
    }

    @Test
    public void getCount() throws Exception {
        assertEquals(2, inv.countOf("Red Key"));
    }

    @Test
    public void subtract() throws Exception {
        inv.subtract("Red Key");
        assertEquals(1, inv.countOf("Red Key"));
        inv.subtract("Electric Powder");
        assertFalse(inv.isInInventory("Electric Powder"));
    }

    @Test
    public void use() throws Exception {
        assertFalse(inv.use("An Inexistent Item"));
        assertTrue(inv.use("Electric Powder"));
        assertFalse(inv.use("Electric Powder"));
    }

    @Test
    public void get() throws Exception {
        InventoryComponent.Item item = inv.get("Red Key");
        assertTrue(item.getName().equals("Red Key") && item.getCount() == 2);
    }



}