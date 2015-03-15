package com.testroom.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
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
	
	private DistanceJoint joint = null;
	private Body grabbed = null;
	private Vector2 grabbedPos = null;
	
	public PlayerSystem(Entity entity) {
		player = entity;
		
		TransformComponent tComp = entity.getComponent(TransformComponent.class);
		MovementComponent mComp = entity.getComponent(MovementComponent.class);
		
		PhysicsDataStructure s = new PhysicsDataStructure(new PhysicsCharacter(this),
				PhysicsObjectType.PLAYER);
		this.body = PhysicsManager.getInstance().createDynamicRectangle(
				tComp.pos.cpy(), new Vector2(PlayerComponent.WIDTH,
											 PlayerComponent.HEIGHT), s);
		body.setLinearVelocity(mComp.velocity);
	}

	public void jump(float axis1, float axis2) {
		StateComponent sComp = player.getComponent(StateComponent.class);
		MovementComponent mComp = player.getComponent(MovementComponent.class);
		if(sComp.get() == PlayerComponent.STATE_GRAB) {
			PhysicsManager.getInstance().destroyJoint(joint);
			joint = null;
			mComp.velocity.set(axis1 * PlayerComponent.MOVE_VELOCITY,
							   axis2 * PlayerComponent.MOVE_VELOCITY);
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
			DistanceJointDef jointDef = new DistanceJointDef();
			jointDef.initialize(this.body, grabbed, this.body.getWorldCenter().cpy(), this.grabbedPos);
			joint = (DistanceJoint) PhysicsManager.getInstance().createJoint(jointDef);
		}
		//TODO Update state, transform, etc ...
	}
	
	

}
