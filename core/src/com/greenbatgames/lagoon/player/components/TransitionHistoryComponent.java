package com.greenbatgames.lagoon.player.components;

import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.player.PlayerComponent;

import java.util.HashSet;
import java.util.Set;

/**
 * Records transitions which have been unlocked in the past, and
 * edits them to remain unlocked when their map is reloaded
 */
public class TransitionHistoryComponent extends PlayerComponent {

    private class Transition {
        String mapName;
        String transitionName;

        public Transition(com.greenbatgames.lagoon.entity.Transition transition) {
            this.mapName = transition.getMapName();
            this.transitionName = transition.getName();
        }

        public String getMapName() { return mapName; }
        public String getTransitionName() { return transitionName; }

        public boolean equals(com.greenbatgames.lagoon.entity.Transition transition) {
            return mapName.equals(transition.getMapName())
                    && transitionName.equals(transition.getName());
        }
    }


    private Set<Transition> unlockedTransitions;


    public TransitionHistoryComponent(Player player) {
        super(player);
    }


    @Override
    public void init() {
        unlockedTransitions = new HashSet<>();
    }


    public boolean isUnlocked(com.greenbatgames.lagoon.entity.Transition transition) {
        return unlockedTransitions.stream().anyMatch(trans -> trans.equals(transition));
    }


    public void record(com.greenbatgames.lagoon.entity.Transition transition) {
        unlockedTransitions.add(new Transition(transition));
    }


    public void reset() {
        unlockedTransitions.clear();
    }


    @Override
    public boolean update(float delta) {
        return true;
    }
}
