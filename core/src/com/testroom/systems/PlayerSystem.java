package com.testroom.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.testroom.character.PhysicsCharacter;
import com.testroom.components.MovementComponent;
import com.testroom.components.PlayerComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TransformComponent;
import com.testroom.physics.PhysicsDataStructure;
import com.testroom.physics.PhysicsManager;
import com.testroom.physics.PhysicsObjectType;

public class PlayerSystem extends EntitySystem{
	private Entity player;
	private Body body;
	
	private WeldJoint joint = null;
	private Body grabbed = null;
	private Vector2 grabbedPos = null;
	
	public PlayerSystem(Entity entity) {
		player = entity;
		
		TransformComponent tComp = entity.getComponent(TransformComponent.class);
		MovementComponent mComp = entity.getComponent(MovementComponent.class);
		
		PhysicsDataStructure s = new PhysicsDataStructure(new PhysicsCharacter(this),
				PhysicsObjectType.PLAYER);
		this.body = PhysicsManager.getInstance().createDynamicCircle(
				tComp.pos.cpy(), PlayerComponent.WIDTH/2, s);
		
		mComp.velocity.scl(body.getMass());
		body.setLinearVelocity(mComp.velocity);
		body.setAngularDamping(0);
		
		tComp.body = body;
	}

	public void jump(float axis1, float axis2) {
		StateComponent sComp = player.getComponent(StateComponent.class);
		MovementComponent mComp = player.getComponent(MovementComponent.class);
		if(sComp.get() == PlayerComponent.STATE_GRAB) {
			PhysicsManager.getInstance().destroyJoint(joint);
			joint = null;
						
			mComp.velocity.set(axis2 * PlayerComponent.MOVE_VELOCITY,
					   -axis1 * PlayerComponent.MOVE_VELOCITY);
			mComp.velocity.scl(body.getMass());
			
			body.setAngularVelocity(0);
			float angle = MathUtils.atan2(mComp.velocity.y, mComp.velocity.x) - MathUtils.atan2(1, 0);
			body.setTransform(body.getWorldCenter(), angle);
			this.body.applyForceToCenter(mComp.velocity.cpy(), true);
		}
		

		
		sComp.set(PlayerComponent.STATE_JUMP);
	}
	
	public void grabbing() {
		StateComponent sComp = player.getComponent(StateComponent.class);
		sComp.set(PlayerComponent.STATE_GRABBING); 
	}

	public void grab(Body grabbed, Vector2 pos) { 
		StateComponent sComp = player.getComponent(StateComponent.class);
		
		if(sComp.get() == PlayerComponent.STATE_GRABBING) {
			sComp.set(PlayerComponent.STATE_GRAB);
			this.grabbed  = grabbed;
			this.grabbedPos = pos;
		}
	}
	
	@Override
	public void update(float dt){
		super.update(dt);
		
		StateComponent sComp = player.getComponent(StateComponent.class);
		if(sComp.get() == PlayerComponent.STATE_GRAB && joint == null) {
			body.setAngularVelocity(0);
			float angle = MathUtils.atan2(body.getWorldCenter().y - grabbedPos.y,
										  body.getWorldCenter().x - grabbedPos.x) -
										  MathUtils.atan2(1, 0);
			body.setTransform(body.getWorldCenter(), angle);

			WeldJointDef jointDef = new WeldJointDef();
			jointDef.initialize(this.body, grabbed,this.grabbedPos);
			joint = (WeldJoint) PhysicsManager.getInstance().createJoint(jointDef);
			
		}
	}
	
	

}
