package com.testroom.systems;

import java.util.HashMap;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.testroom.components.GrapnelComponent;
import com.testroom.components.MovementComponent;
import com.testroom.components.PlayerComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TransformComponent;
import com.testroom.configuration.ConfigManager;
import com.testroom.objects.grapnel.GrapnelBuilder;
import com.testroom.physics.PhysicsManager;


public class PlayerSystem extends IteratingSystem{	
	private GrapnelBuilder grapnelBuilder;
	private HashMap<Long, Integer> ids = new HashMap<Long, Integer>();
	
	public PlayerSystem(GrapnelBuilder g) {
		super(Family.getFor(PlayerComponent.class));	
				
		grapnelBuilder = g;
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		
		ImmutableArray<Entity> entities = getEntities();
		
		for(int i = 0; i < entities.size(); i++) {
			ids.put(entities.get(i).getId(), i);
		}
	}

	public void jump(long id, float axis1, float axis2) {
		ImmutableArray<Entity> entities = getEntities();
		Entity player = entities.get(ids.get(id));
			
		if (player == null)
			return;
		
		StateComponent sComp = player.getComponent(StateComponent.class);
		MovementComponent mComp = player.getComponent(MovementComponent.class);
		PlayerComponent pComp = player.getComponent(PlayerComponent.class);
		
		Body body = player.getComponent(TransformComponent.class).body;
		
		if(sComp.get() == PlayerComponent.STATE_GRAB) {
			PhysicsManager.getInstance().destroyJoint(pComp.joint);
			pComp.joint = null;
						
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
	
	public void grabbing(long id) {
		ImmutableArray<Entity> entities = getEntities();
		Entity player = entities.get(ids.get(id));
			
		if (player == null)
			return;
		
		StateComponent sComp = player.getComponent(StateComponent.class);
		sComp.set(PlayerComponent.STATE_GRABBING); 
	}

	public void grab(long id, Body grabbed, Vector2 pos) {
		ImmutableArray<Entity> entities = getEntities();
		Entity player = entities.get(ids.get(id));
			
		if (player == null)
			return;
		
		StateComponent sComp = player.getComponent(StateComponent.class);
		PlayerComponent pComp = player.getComponent(PlayerComponent.class);
		
		if(sComp.get() == PlayerComponent.STATE_GRABBING) {
			sComp.set(PlayerComponent.STATE_GRAB);
			pComp.grabbed  = grabbed;
			pComp.grabbedPos = pos;
		}
	}
	
	public void shootGrapnel (long id, float axis1, float axis2) {
		ImmutableArray<Entity> entities = getEntities();
		Entity player = entities.get(ids.get(id));
			
		if (player == null)
			return;
		
		PlayerComponent pComp = player.getComponent(PlayerComponent.class);
		
		if (pComp.grapnel != null) {
			GrapnelComponent gc = pComp.grapnel.getComponent(GrapnelComponent.class);
			gc.grapnelJoint.setMaxLength(GrapnelComponent.MAX_DISTANCE);
			return;
		}
		
		TransformComponent tComp = player.getComponent(TransformComponent.class);
		MovementComponent mComp = player.getComponent(MovementComponent.class);
		
		Vector2 tmpEpsilon = new Vector2(axis2 * ConfigManager.epsilon,
										-axis1 * ConfigManager.epsilon);
		
		pComp.grapnel = grapnelBuilder.build(tComp.body, tComp.pos.cpy().add(tmpEpsilon));
		
		TransformComponent tCompGrap = pComp.grapnel.getComponent(TransformComponent.class);
		MovementComponent mCompGrap = pComp.grapnel.getComponent(MovementComponent.class);
		
		mCompGrap.velocity.set(axis2 * PlayerComponent.MOVE_VELOCITY,
							-axis1 * PlayerComponent.MOVE_VELOCITY);
		mCompGrap.velocity.scl(tCompGrap.body.getMass());
		mCompGrap.velocity.add(mComp.velocity);
				
		float angle = MathUtils.atan2(mCompGrap.velocity.y, mCompGrap.velocity.x) - MathUtils.atan2(1, 0);
		tCompGrap.body.setTransform(tCompGrap.body.getWorldCenter(), angle);
		tCompGrap.body.applyForceToCenter(mCompGrap.velocity.cpy(), true);
		
		tComp.body.applyForceToCenter(mCompGrap.velocity.cpy().scl(-1), true);
	}
	
	public void recallGrapnel (long id) {
		ImmutableArray<Entity> entities = getEntities();
		Entity player = entities.get(ids.get(id));
			
		if (player == null)
			return;
		
		PlayerComponent pComp = player.getComponent(PlayerComponent.class);
		
		if (pComp.grapnel == null)
			return;
		
		StateComponent gComp = pComp.grapnel.getComponent(StateComponent.class);
		gComp.set(GrapnelComponent.STATE_RECALL);
	}
	
	public void detachGrapnel (long id) {
		ImmutableArray<Entity> entities = getEntities();
		Entity player = entities.get(ids.get(id));
		PlayerComponent pComp = player.getComponent(PlayerComponent.class);
		
		
	}

	public boolean isGrapnel(long id, Entity grapnel) {
		ImmutableArray<Entity> entities = getEntities();
		Entity player = entities.get(ids.get(id));
			
		if (player == null)
			return false;
		
		PlayerComponent pComp = player.getComponent(PlayerComponent.class);
		
		return grapnel == pComp.grapnel;
	}

	public void destroyGrapnel(long id) {
		ImmutableArray<Entity> entities = getEntities();
		Entity player = entities.get(ids.get(id));
			
		if (player == null)
			return;
		
		PlayerComponent pComp = player.getComponent(PlayerComponent.class);
		
		pComp.grapnel = null;		
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		StateComponent sComp = entity.getComponent(StateComponent.class);
		PlayerComponent pComp = entity.getComponent(PlayerComponent.class);
		
		Body body = entity.getComponent(TransformComponent.class).body;
		
		if(sComp.get() == PlayerComponent.STATE_GRAB && pComp.joint == null) {
			body.setAngularVelocity(0);
			float angle = MathUtils.atan2(body.getWorldCenter().y - pComp.grabbedPos.y,
										  body.getWorldCenter().x - pComp.grabbedPos.x) -
										  MathUtils.atan2(1, 0);
			body.setTransform(body.getWorldCenter(), angle);

			WeldJointDef jointDef = new WeldJointDef();
			jointDef.initialize(body, pComp.grabbed, pComp.grabbedPos);
			pComp.joint = (WeldJoint) PhysicsManager.getInstance().createJoint(jointDef);
		}
	}
}
