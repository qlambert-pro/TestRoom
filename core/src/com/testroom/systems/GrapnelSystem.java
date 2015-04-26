package com.testroom.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.testroom.components.GrapnelComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TransformComponent;
import com.testroom.configuration.ConfigManager;
import com.testroom.physics.PhysicsManager;

public class GrapnelSystem extends IteratingSystem {
	Engine engine;
	
	public GrapnelSystem(Engine engine) {
		super(Family.getFor(GrapnelComponent.class));		
		
		this.engine = engine;
	}
	
	
	public void grab(Entity grapnel, Body grabbed, Vector2 grabbedPos) {
		StateComponent sComp = grapnel.getComponent(StateComponent.class);
		GrapnelComponent gComp = grapnel.getComponent(GrapnelComponent.class);
		
		Body body = grapnel.getComponent(TransformComponent.class).body;
		
		if(sComp.get() != GrapnelComponent.STATE_GRAB) {
			sComp.set(GrapnelComponent.STATE_GRAB);
			body.setAngularVelocity(0);

			gComp.jointDef = new WeldJointDef();
			gComp.jointDef.initialize(body, grabbed, grabbedPos);	
		}
	}
	
	public void release(Entity grapnel) {
		GrapnelComponent gm = grapnel.getComponent(GrapnelComponent.class);
		StateComponent sComp = grapnel.getComponent(StateComponent.class);

		sComp.set(GrapnelComponent.STATE_UNGRAB);

		if (gm.joint == null)
			return;
		
		PhysicsManager.getInstance().destroyJoint(gm.joint);
		gm.joint = null;
	}


	public void destroy(Entity grapnel) {
		GrapnelComponent gc = grapnel.getComponent(GrapnelComponent.class);
		gc.isDestroyed = true;
	}


	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		GrapnelComponent gc = entity.getComponent(GrapnelComponent.class);
		TransformComponent tc = entity.getComponent(TransformComponent.class);
		StateComponent sc = entity.getComponent(StateComponent.class);
		
		if (gc.RopeSpringState == GrapnelComponent.STATE_RECALL) {
			gc.length0 = gc.length0 - PhysicsManager.WORLD_TO_BOX * GrapnelComponent.RECALL_VELOCITY * deltaTime;
			if (gc.length0 < 0)
				gc.length0 = 0;
		}
		
		applyRopeSpringForce(tc.body, gc.player, gc.length0);

		if (gc.jointDef != null) {
			tc.body.setFixedRotation(true);
			gc.joint = (WeldJoint) PhysicsManager.getInstance().createJoint(gc.jointDef);
			gc.jointDef = null;
		}
		
		if (gc.isDestroyed) {
			if (gc.joint != null) {
				PhysicsManager.getInstance().destroyJoint(gc.joint);
				gc.joint = null;
			}
			PhysicsManager.getInstance().destroyBody(entity.getComponent(TransformComponent.class).body);
			
			engine.removeEntity(entity);
		}
	}


	public void applyRopeSpringForce (Body bodyA, Body bodyB, float length0) {
		Vector2 force = bodyA.getWorldCenter().cpy().sub(bodyB.getWorldCenter());
		float length = force.len();

		force.scl(ConfigManager.springStiffness * (length0 - length) /length);

		if (length > length0) {
			bodyA.applyForceToCenter(force, true);
			bodyB.applyForceToCenter(force.scl(-1), true);
		}
	}

}
