package com.testroom.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.testroom.components.GrapnelComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TransformComponent;
import com.testroom.physics.PhysicsManager;

public class GrapnelSystem extends EntitySystem {	
	private Array<WeldJointDef> jointsDef = null;
	private Array<WeldJoint> joints = null;
	
	public GrapnelSystem() {		
		jointsDef = new Array<WeldJointDef>();
		joints = new Array<WeldJoint>();
	}
	
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		for (WeldJointDef jointDef : jointsDef)		
			joints.add((WeldJoint) PhysicsManager.getInstance().createJoint(jointDef));	
		
		jointsDef.clear();
	}
	
	public void grab(Entity grapnel, Body grabbed, Vector2 grabbedPos) {
		StateComponent sComp = grapnel.getComponent(StateComponent.class);
		
		Body body = grapnel.getComponent(TransformComponent.class).body;
		
		if(sComp.get() == GrapnelComponent.STATE_THROW) {
			sComp.set(GrapnelComponent.STATE_GRAB);
			body.setAngularVelocity(0);
			float angle = MathUtils.atan2(body.getWorldCenter().y - grabbedPos.y,
										  body.getWorldCenter().x - grabbedPos.x) -
										  MathUtils.atan2(1, 0);
			body.setTransform(body.getWorldCenter(), angle);

			WeldJointDef jointDef = new WeldJointDef();
			jointDef.initialize(body, grabbed, grabbedPos);	
			
			jointsDef.add(jointDef);
		}
	}
	
	public void release(Entity grapnel) {
		TransformComponent tm = grapnel.getComponent(TransformComponent.class);
		
		for ( WeldJoint j : joints) {
			if(j.getBodyA() == tm.body || j.getBodyB() == tm.body) {
				joints.removeValue(j, true);
				PhysicsManager.getInstance().destroyJoint(j);
				break;
			}				
		}
	}

}
