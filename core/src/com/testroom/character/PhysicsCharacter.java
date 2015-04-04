package com.testroom.character;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.testroom.components.GrapnelComponent;
import com.testroom.components.StateComponent;
import com.testroom.map.Edge;
import com.testroom.objects.grapnel.PhysicsGrapnel;
import com.testroom.physics.PhysicsDataStructure;
import com.testroom.physics.PhysicsObject;
import com.testroom.physics.PhysicsObjectType;
import com.testroom.systems.PlayerSystem;

public class PhysicsCharacter implements PhysicsObject {
	private PlayerSystem system;
	long id;
	
	public PhysicsCharacter(long id, PlayerSystem playerSystem) {
		system = playerSystem;
		this.id = id;
	}

	@Override
	public void BeginContactHandler(PhysicsDataStructure struct, Contact contact) {
		if(struct.type == PhysicsObjectType.GRAPNEL) {
			Entity grapnel = ((PhysicsGrapnel) struct.obj).getGrapnel();
			StateComponent sComp = grapnel.getComponent(StateComponent.class);
			if (system.isGrapnel(id, grapnel) && sComp.get() == GrapnelComponent.STATE_RECALL) {
				system.destroyGrapnel(id);
				((PhysicsGrapnel) struct.obj).destroyGrapnel();
			}
			return;
		} else if (struct.type == PhysicsObjectType.SOLID) {
			system.grab(id, ((Edge) struct.obj).getBody(),
					contact.getWorldManifold().getPoints()[0]);	
		}
		
		
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
