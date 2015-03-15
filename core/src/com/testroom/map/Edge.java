package com.testroom.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.testroom.physics.PhysicsDataStructure;
import com.testroom.physics.PhysicsManager;
import com.testroom.physics.PhysicsObject;
import com.testroom.physics.PhysicsObjectType;

public class Edge implements PhysicsObject {

	private Body body;
	
	public Edge(Vector2 beg, Vector2 end, PhysicsObjectType type) {
		PhysicsDataStructure pds = new PhysicsDataStructure(this, type);
		body = PhysicsManager.getInstance().createEdge(beg, end, pds);
	}

	@Override
	public void BeginContactHandler(PhysicsDataStructure struct, Contact contact) {
		// :D
	}

	@Override
	public void EndContactHandler(PhysicsDataStructure struct, Contact contact) {
		// :D
	}

	@Override
	public void PreContactHandler(PhysicsDataStructure b, Contact contact) {
		// :D	
	}

	public Body getBody() {
		return body;
	}

}
