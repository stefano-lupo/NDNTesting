package com.stefanolupo.ndngame.libgdx.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.stefanolupo.ndngame.libgdx.components.CollisionComponent;

@Singleton
public class GameContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        throwIfNotEntity(f1);
        throwIfNotEntity(f2);

        Vector2 approxCollisionPoint = contact.getWorldManifold().getPoints()[0];

        Entity e1 = (Entity) f1.getBody().getUserData();
        Entity e2 = (Entity) f2.getBody().getUserData();

        createCollisionComponentIfEntityHasOne(e1, e2, approxCollisionPoint);
        createCollisionComponentIfEntityHasOne(e2, e1, approxCollisionPoint);
    }

    private void throwIfNotEntity(Fixture fixture) {
        Preconditions.checkArgument(fixture.getBody().getUserData() instanceof Entity,
                "Collided fixture was not an entity");
    }

    private void createCollisionComponentIfEntityHasOne(Entity e1, Entity e2, Vector2 approxCollisionPoint) {
        CollisionComponent collisionComponent = e1.getComponent(CollisionComponent.class);
        if (collisionComponent == null) {
            return;
        }

        collisionComponent.setCollidedWith(e2);
        collisionComponent.setCollisionLocation(approxCollisionPoint);
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
