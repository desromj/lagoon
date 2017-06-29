package com.greenbatgames.lagoon.collision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.greenbatgames.lagoon.animation.FadeOutText;
import com.greenbatgames.lagoon.enemy.Enemy;
import com.greenbatgames.lagoon.entity.MapItem;
import com.greenbatgames.lagoon.entity.Transition;
import com.greenbatgames.lagoon.physics.Climbable;
import com.greenbatgames.lagoon.physics.PhysicsBody;
import com.greenbatgames.lagoon.physics.Swimmable;
import com.greenbatgames.lagoon.player.Player;
import com.greenbatgames.lagoon.util.Constants;
import com.greenbatgames.lagoon.util.Enums;

/**
 * Created by Quiv on 23-01-2017.
 */
public class LagoonContactListener implements ContactListener {

    public static final String TAG = LagoonContactListener.class.getSimpleName();

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

        // Handle item pickups
        if (other instanceof MapItem) {
            ((MapItem) other).pickUp(player);
            return;
        }

        // Handle player contact with an enemy
        if (player.fixtureIsEnabled(playerFix) && !playerFix.isSensor() && other instanceof Enemy) {
            player.health().damage(((Enemy) other).getContactDamage());
            return;
        }

        // toggle whether or not the player is grounded
        if (playerFix.getUserData() == Enums.PlayerFixtures.GROUND_SENSOR) {
            player.mover().incrementNumFootContacts();

            if (player.getBody().getLinearVelocity().y < Constants.FALL_VELOCITY_DAMAGE_THRESHOLD) {
                player.health().damage(Constants.FALL_DAMAGE);
            }
        }

        // Handle player climbing
        if (playerFix.getUserData() == Enums.PlayerFixtures.CLIMB_SENSOR
                && other instanceof Climbable
                && player.isClimbButtonHeld())
        {
            /*
                Each vert must be checked against the previous and next verts.
                If any before and after are higher than the matching point, we cannot climb on it.
                Maps should begin with any terrain as a bottom-left corner that optimally cannot
                be reached by the climbing sensor in the first place
              */
            float [] verts = ((Climbable) other).getVerts();
            Vector2 checkPoint = new Vector2();

            for (int i = 0; i < verts.length; i += 2) {
                checkPoint.set(
                        otherFix.getBody().getPosition().x + verts[i],
                        otherFix.getBody().getPosition().y + verts[i+1]);

                // If our checkpoint is within range, check the previous and next y values
                if (playerFix.testPoint(checkPoint)) {

                    // fail if any of the y values of adjacent points are taller than the current point
                    try {
                        if (verts[i - 1] > verts[i + 1] || verts[i + 3] > verts[i + 1]) {
                            continue;
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        continue;
                    }

                    // Otherwise scale and set our grip point
                    checkPoint.scl(Constants.PTM);
                    player.climber().startClimbing(checkPoint);
                    break;
                }
            }
        }

        // Handle player swimming
        if (playerFix.getUserData() == Enums.PlayerFixtures.SWIM_SENSOR
                && other instanceof Swimmable) {
            ((Swimmable) other).startSwimming(player);
        }

        // Display tooltip when player may transition between maps
        if (other instanceof Transition) {
            Transition trans = (Transition) other;

            if (trans.canBeUsed()) {
                player.tooltip().show();
            }
        }
    }

    private void endPlayerContact(Player player, PhysicsBody other, Fixture playerFix, Fixture otherFix, Contact contact) {

        // toggle whether or not the player is grounded
        if (playerFix.getUserData() == Enums.PlayerFixtures.GROUND_SENSOR) {
            player.mover().decrementNumFootContacts();
        }

        // toggle whether or not the player is still swimming
        if (playerFix.getUserData() == Enums.PlayerFixtures.SWIM_SENSOR
                && other instanceof Swimmable) {
            ((Swimmable) other).stopSwimming(player);
        }

        // Display tooltip when player may transition between maps
        if (other instanceof Transition) {
            player.tooltip().hide();
        }
    }

}
