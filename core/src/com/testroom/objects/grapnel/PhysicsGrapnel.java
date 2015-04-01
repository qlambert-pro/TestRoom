package com.testroom.objects.grapnel;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.testroom.map.Edge;
import com.testroom.physics.PhysicsDataStructure;
import com.testroom.physics.PhysicsObject;
import com.testroom.physics.PhysicsObjectType;
import com.testroom.systems.GrapnelSystem;

public class PhysicsGrapnel implements PhysicsObject {
	private GrapnelSystem system;
	private Entity grapnel;
	
	public PhysicsGrapnel(Entity g, GrapnelSystem grapnelSystem) {
		system = grapnelSystem;
		grapnel = g;
	}

	@Override
	public void BeginContactHandler(PhysicsDataStructure struct, Contact contact) {			
		if(!(struct.type == PhysicsObjectType.GRAPNEL))
			return;

		system.grab(
				grapnel,
				((Edge) struct.obj).getBody(),
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
	
	public Entity getGrapnel(){
		return grapnel;
	}

	public void destroyGrapnel() {
		system.destroy(grapnel);		
	}

}
