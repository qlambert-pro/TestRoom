package com.testroom.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class PhysicsContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		PhysicsDataStructure a = (PhysicsDataStructure) contact.getFixtureA()
				.getBody().getUserData();
		PhysicsDataStructure b = (PhysicsDataStructure) contact.getFixtureB()
				.getBody().getUserData();

		a.obj.BeginContactHandler(b, contact);
		b.obj.BeginContactHandler(a, contact);
	}

	@Override
	public void endContact(Contact contact) {
		PhysicsDataStructure a = (PhysicsDataStructure) contact.getFixtureA()
				.getBody().getUserData();
		PhysicsDataStructure b = (PhysicsDataStructure) contact.getFixtureB()
				.getBody().getUserData();

		a.obj.EndContactHandler(b, contact);
		b.obj.EndContactHandler(a, contact);
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		PhysicsDataStructure a = (PhysicsDataStructure) contact.getFixtureA()
				.getBody().getUserData();
		PhysicsDataStructure b = (PhysicsDataStructure) contact.getFixtureB()
				.getBody().getUserData();

		a.obj.PreContactHandler(b, contact);
		b.obj.PreContactHandler(a, contact);	
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
