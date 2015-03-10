package com.testroom.physics;

import com.badlogic.gdx.physics.box2d.Contact;

public interface PhysicsObject {
	public void BeginContactHandler(PhysicsDataStructure struct, Contact contact);
	public void EndContactHandler(PhysicsDataStructure struct, Contact contact);
	public void PreContactHandler(PhysicsDataStructure b, Contact contact);
}
