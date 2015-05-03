package com.testroom.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.testroom.configuration.ConfigManager;

public class PhysicsManager {
	/* Physics parameter */
	static private int velocityIterations = 8;
	static private int positionIterations = 3;
	static public final float WORLD_TO_BOX = 1f / ConfigManager.minBlockSize;
	static public final float BOX_TO_WORLD = 1f / WORLD_TO_BOX;
	static private PhysicsManager singleton;

	/* Attributs */
	private World world;
	private float updateCount;

	public static PhysicsManager getInstance() {
		if (singleton == null)
			singleton = new PhysicsManager();
		return singleton;
	}

	public PhysicsManager() {
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new PhysicsContactListener());

		updateCount = 0;
	}

	public void update(float dt) {
		updateCount += dt;
		while (updateCount > ConfigManager.physicsStepSize) {
			world.step(ConfigManager.physicsStepSize, velocityIterations,
					positionIterations);
			updateCount -= ConfigManager.physicsStepSize;
		}
	}

	public void clear() {
		Array<Body> bodies = new Array<Body>();
		Array<Joint> joints = new Array<Joint>();
		
		/* Clear World */		
		world.getJoints(joints);
		for (Joint j : joints)
			world.destroyJoint(j);
		
		world.getBodies(bodies);
		for (Body b : bodies)
			world.destroyBody(b);
	}

	public Body createPlayer(Vector2 pos, float size,
			PhysicsDataStructure s) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(WORLD_TO_BOX * pos.x, WORLD_TO_BOX * pos.y);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = false;
		Body b = world.createBody(bodyDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(size);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;

		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0f;

		b.createFixture(fixtureDef);
		b.setUserData(s);
		return b;
	}
	
	public Body createGrapnel(Vector2 pos, float size,
			PhysicsDataStructure s) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(WORLD_TO_BOX * pos.x, WORLD_TO_BOX * pos.y);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = false;
		Body b = world.createBody(bodyDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(size);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;

		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.5f;

		b.createFixture(fixtureDef);
		b.setUserData(s);
		return b;
	}

	public Body createEdge(Vector2 pos1, Vector2 pos2, PhysicsDataStructure s) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos1.x, pos1.y);
		Body b = world.createBody(bodyDef);
		EdgeShape edge = new EdgeShape();
		Vector2 p1 = new Vector2(0, 0);
		Vector2 p2 = new Vector2(pos2.x - pos1.x, pos2.y - pos1.y);

		edge.set(p1, p2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = edge;
		fixtureDef.friction = 0;
		fixtureDef.density = 0;		
		fixtureDef.restitution = 0.5f;

		b.createFixture(fixtureDef);
		b.setUserData(s);
		return b;
	}

	public void addSensor(Body b, float size) {
		CircleShape circle = new CircleShape();
		circle.setRadius(size);
		
		FixtureDef sensor = new FixtureDef();
		sensor.shape = circle;
		sensor.isSensor = true;
		sensor.density = 0;
		
		b.createFixture(sensor);
	}
	
	
	public Joint createJoint(JointDef jointDef) {
		return world.createJoint(jointDef);		
	}
	
	public void destroyJoint(Joint joint) {
		world.destroyJoint(joint);
	}
	
	public void destroyBody(Body body) {
		world.destroyBody(body);
	}
}
