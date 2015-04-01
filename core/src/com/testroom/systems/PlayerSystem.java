package com.testroom.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.testroom.components.GrapnelComponent;
import com.testroom.components.MovementComponent;
import com.testroom.components.PlayerComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TransformComponent;
import com.testroom.objects.grapnel.GrapnelBuilder;
import com.testroom.physics.PhysicsManager;


public class PlayerSystem extends EntitySystem{
	private Entity player;
	
	private WeldJoint joint = null;
	private Body grabbed = null;
	private Vector2 grabbedPos = null;
	
	private GrapnelBuilder grapnelBuilder;
	private Entity grapnel = null;
	private RopeJoint grapnelJoint = null;
	
	public PlayerSystem(Entity entity, GrapnelBuilder g) {
		player = entity;
		grapnelBuilder = g;
	}

	public void jump(float axis1, float axis2) {
		StateComponent sComp = player.getComponent(StateComponent.class);
		MovementComponent mComp = player.getComponent(MovementComponent.class);
		Body body = player.getComponent(TransformComponent.class).body;
		
		if(sComp.get() == PlayerComponent.STATE_GRAB) {
			PhysicsManager.getInstance().destroyJoint(joint);
			joint = null;
						
			mComp.velocity.set(axis2 * PlayerComponent.MOVE_VELOCITY,
					   -axis1 * PlayerComponent.MOVE_VELOCITY);
			mComp.velocity.scl(body.getMass());
			
			body.setAngularVelocity(0);
			float angle = MathUtils.atan2(mComp.velocity.y, mComp.velocity.x) - MathUtils.atan2(1, 0);
			body.setTransform(body.getWorldCenter(), angle);
			body.applyForceToCenter(mComp.velocity.cpy(), true);
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
		Body body = player.getComponent(TransformComponent.class).body;
		
		if(sComp.get() == PlayerComponent.STATE_GRAB && joint == null) {
			body.setAngularVelocity(0);
			float angle = MathUtils.atan2(body.getWorldCenter().y - grabbedPos.y,
										  body.getWorldCenter().x - grabbedPos.x) -
										  MathUtils.atan2(1, 0);
			body.setTransform(body.getWorldCenter(), angle);

			WeldJointDef jointDef = new WeldJointDef();
			jointDef.initialize(body, grabbed,this.grabbedPos);
			joint = (WeldJoint) PhysicsManager.getInstance().createJoint(jointDef);
			
		}
		
		if (grapnel == null)
			return;
		
		GrapnelComponent gComp = grapnel.getComponent(GrapnelComponent.class);
		float length = gComp.distance;
		
		grapnelJoint.setMaxLength(length);
		
		TransformComponent tComp = player.getComponent(TransformComponent.class);
		TransformComponent tCompGrap = grapnel.getComponent(TransformComponent.class);
		StateComponent sCompGrap = grapnel.getComponent(StateComponent.class);
		
		float distance = tComp.body.getWorldCenter().dst(tCompGrap.body.getWorldCenter());
		
		if ((sCompGrap.get() == GrapnelComponent.STATE_THROW ||
			 sCompGrap.get() == GrapnelComponent.STATE_GRAB) &&
			distance >= length){
			recallGrapnel();			
		}
	}
	
	public void shootGrapnel (float axis1, float axis2) {
		if (grapnel != null)
			return;
		
		TransformComponent tComp = player.getComponent(TransformComponent.class);
		
		grapnel = grapnelBuilder.build(tComp.pos.cpy());
		
		TransformComponent tCompGrap = grapnel.getComponent(TransformComponent.class);
		MovementComponent mCompGrap = grapnel.getComponent(MovementComponent.class);
		
		RopeJointDef jointDef = new RopeJointDef();
		jointDef.bodyA = tCompGrap.body;
		jointDef.bodyB = tComp.body;
		jointDef.collideConnected = true;
		jointDef.maxLength = GrapnelComponent.MAX_DISTANCE;
		
		grapnelJoint = (RopeJoint) PhysicsManager.getInstance().createJoint(jointDef);
		
		mCompGrap.velocity.set(axis2 * PlayerComponent.MOVE_VELOCITY,
				   -axis1 * PlayerComponent.MOVE_VELOCITY);
		mCompGrap.velocity.scl(tCompGrap.body.getMass());
				
		float angle = MathUtils.atan2(mCompGrap.velocity.y, mCompGrap.velocity.x) - MathUtils.atan2(1, 0);
		tCompGrap.body.setTransform(tCompGrap.body.getWorldCenter(), angle);
		tCompGrap.body.applyForceToCenter(mCompGrap.velocity.cpy(), true);
		
		tComp.body.applyForceToCenter(mCompGrap.velocity.cpy().scl(-1), true);
	}
	
	public void recallGrapnel () {
		if (grapnel == null)
			return;
		
		StateComponent gComp = grapnel.getComponent(StateComponent.class);
		gComp.set(GrapnelComponent.STATE_RECALL);
	}
	
	public void detachGrapnel () {
		// TODO
		//engine.getSystem(GrapnelSystem.class).setProcessing(true);
		//destroy joints and all		
	}

	public boolean isGrapnel(Entity grapnel) { 
		return grapnel == this.grapnel;
	}

	public void destroyGrapnel() {
		grapnel = null;		
	}
}
