package com.stefanolupo.ndngame.libgdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.stefanolupo.ndngame.protos.GameObject;

@Singleton
public class BodyFactory {

    public static final int STEEL = 0;
    public static final int WOOD = 1;
    public static final int RUBBER = 2;
    public static final int STONE = 3;

    private final World world;

    @Inject
    private BodyFactory(World world) {
        this.world = world;
    }

    public Body makeCirclePolyBody(float x, float y, float radius, int material, BodyDef.BodyType bodyType, boolean fixedRotation) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.x = x;
        bodyDef.position.y = y;
        bodyDef.fixedRotation = fixedRotation;

        Body body = world.createBody(bodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        body.createFixture(makeFixture(material, circleShape));
        circleShape.dispose();

        return body;
    }

    public Body makeBoxPolyBody(GameObject gameObject, int material, BodyDef.BodyType bodyType) {
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = gameObject.getX();
        boxBodyDef.position.y = gameObject.getY();
        boxBodyDef.angle = gameObject.getAngle();
        boxBodyDef.fixedRotation = gameObject.getIsFixedRotation();

        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(gameObject.getWidth() / 2, gameObject.getHeight() / 2);
        boxBody.createFixture(makeFixture(material,poly));
        poly.dispose();

        return boxBody;
    }

    public Body makePolygonBody(Vector2[] vertices, float x, float y, int material, BodyDef.BodyType bodyType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.x = x;
        bodyDef.position.y = y;
        Body body =  world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);
        body.createFixture(makeFixture(material, polygonShape));
        polygonShape.dispose();

        return body;

    }

    public Body makeBoundary() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0);
        Body body = world.createBody(bodyDef);
        EdgeShape edgeShape = new EdgeShape();

        edgeShape.set(0,0, EntityCreator.WORLD_WIDTH, 0);
        body.createFixture(edgeShape, 0);

        edgeShape.set(0,0, 0, EntityCreator.WORLD_HEIGHT);
        body.createFixture(edgeShape, 0);

        edgeShape.set(EntityCreator.WORLD_WIDTH, EntityCreator.WORLD_HEIGHT, EntityCreator.WORLD_WIDTH, 0);
        body.createFixture(edgeShape, 0);

        edgeShape.set(EntityCreator.WORLD_WIDTH, EntityCreator.WORLD_HEIGHT, 0, EntityCreator.WORLD_HEIGHT);
        body.createFixture(edgeShape, 0);

        return body;
    }


    private static FixtureDef makeFixture(int material, Shape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        switch(material){
            case 0:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.3f;
                fixtureDef.restitution = 0.1f;
                break;
            case 1:
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.7f;
                fixtureDef.restitution = 0.3f;
                break;
            case 2:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0f;
                fixtureDef.restitution = 1f;
                break;
            case 3:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.9f;
                fixtureDef.restitution = 0f;
            default:
                fixtureDef.density = 7f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.3f;
        }
        return fixtureDef;
    }
}
