package com.greenbatgames.lagoon.collision;

import com.badlogic.gdx.physics.box2d.*;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.util.Enums;

/**
 * Created by Quiv on 23-01-2017.
 */
public class LagoonContactListener implements ContactListener {

    /**
     * Simple inner class to quickly access similar parts of a used Contact
     */
    private class ContactObjects {
        PhysicsBody a;
        PhysicsBody b;
        Fixture fixA;
        Fixture fixB;

        public ContactObjects(Contact contact) {
            a = (PhysicsBody) contact.getFixtureA().getBody().getUserData();
            b = (PhysicsBody) contact.getFixtureB().getBody().getUserData();
            fixA = contact.getFixtureA();
            fixB = contact.getFixtureB();
        }
    }

    @Override
    public void beginContact(Contact contact) {
        ContactObjects co = new ContactObjects(contact);

        // Handle player interactions
        if (co.a instanceof Player || co.b instanceof Player) {
            if (co.a instanceof Player) {
                beginPlayerContact((Player) co.a, co.b, co.fixA, co.fixB, contact);
            } else {
                beginPlayerContact((Player) co.b, co.a, co.fixB, co.fixA, contact);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        ContactObjects co = new ContactObjects(contact);

        // Handle player interactions
        if (co.a instanceof Player || co.b instanceof Player) {
            if (co.a instanceof Player) {
                endPlayerContact((Player) co.a, co.b, co.fixA, co.fixB, contact);
            } else {
                endPlayerContact((Player) co.b, co.a, co.fixB, co.fixA, contact);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        ContactObjects co = new ContactObjects(contact);

        // Handle player interactions
        if (co.a instanceof Player || co.b instanceof Player) {
            if (co.a instanceof Player) {
                preSolvePlayerContact((Player) co.a, co.b, co.fixA, co.fixB, contact);
            } else {
                preSolvePlayerContact((Player) co.b, co.a, co.fixB, co.fixA, contact);
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    /*
        Custom handling methods
     */

    private void preSolvePlayerContact(Player player, PhysicsBody other, Fixture playerFix, Fixture otherFix, Contact contact) {
        if (!player.fixtureIsEnabled(playerFix)) {
            contact.setEnabled(false);
        }
    }

    private void beginPlayerContact(Player player, PhysicsBody other, Fixture playerFix, Fixture otherFix, Contact contact) {
        // toggle whether or not the player is grounded
        if (playerFix.getUserData() == Enums.PlayerFixtures.GROUND_SENSOR) {
            player.mover().incrementNumFootContacts();
        }
    }

    private void endPlayerContact(Player player, PhysicsBody other, Fixture playerFix, Fixture otherFix, Contact contact) {
        // toggle whether or not the player is grounded
        if (playerFix.getUserData() == Enums.PlayerFixtures.GROUND_SENSOR) {
            player.mover().decrementNumFootContacts();
        }
    }

}
