package com.greenbatgames.lagoon.player;

import com.greenbatgames.lagoon.screen.GameScreen;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Quiv on 15-03-2017.
 */
public class PlayerTest {

    Player player;

    @Before
    public void setUp() {
        GameScreen.getInstance().init();
        player = new Player(0, 0, 50, 50);
    }

    @Test
    public void playerBodyIsNotNullAfterInstantiation() throws Exception {
        assertNotNull(player.getBody());
    }
}