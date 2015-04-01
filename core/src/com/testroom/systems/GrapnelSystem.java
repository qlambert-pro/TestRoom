package com.testroom.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
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

public class GrapnelSystem extends IteratingSystem {
	Engine engine;
	
	private Array<WeldJointDef> jointsDef = null;
	private Array<WeldJoint> joints = null;

	private Array<Entity> destroyedQueue = null;
	
	public GrapnelSystem(Engine engine) {
		super(Family.getFor(GrapnelComponent.class));		
		
		jointsDef = new Array<WeldJointDef>();
		joints = new Array<WeldJoint>();
		destroyedQueue = new Array<Entity>();
		
		this.engine = engine;
	}
	
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		for (WeldJointDef jointDef : jointsDef)		
			joints.add((WeldJoint) PhysicsManager.getInstance().createJoint(jointDef));	
		
		jointsDef.clear();
		
		for (Entity e : destroyedQueue) {			
			PhysicsManager.getInstance().destroyBody(e.getComponent(TransformComponent.class).body);
			engine.removeEntity(e);
		}
		
		destroyedQueue.clear();
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


	public void destroy(Entity grapnel) {
		destroyedQueue.add(grapnel);		
	}


	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		GrapnelComponent gc = entity.getComponent(GrapnelComponent.class);
		StateComponent sc = entity.getComponent(StateComponent.class);
		
		if (sc.get() == GrapnelComponent.STATE_RECALL)
			gc.distance = gc.distance - GrapnelComponent.MOVE_VELOCITY * deltaTime;
	}

}
