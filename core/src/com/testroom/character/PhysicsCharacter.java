package com.testroom.character;

import com.badlogic.gdx.physics.box2d.Contact;
import com.testroom.map.Edge;
import com.testroom.physics.PhysicsDataStructure;
import com.testroom.physics.PhysicsObject;
import com.testroom.systems.PlayerSystem;

public class PhysicsCharacter implements PhysicsObject {
	private PlayerSystem system;
	
	public PhysicsCharacter(PlayerSystem playerSystem) {
		system = playerSystem;
	}

	@Override
	public void BeginContactHandler(PhysicsDataStructure struct, Contact contact) {
		//TODO beurk !!!
		if(!(struct.obj instanceof Edge))
			return;
		
		system.grab(((Edge) struct.obj).getBody(),
				contact.getWorldManifold().getPoints()[0]);
	}

	@Override
	public void EndContactHandler(PhysicsDataStructure struct, Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void PreContactHandler(PhysicsDataStructure b, Contact contact) {
		// TODO Auto-generated method stub

	}

}
