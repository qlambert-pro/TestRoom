package com.testroom.objects.grapnel;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.testroom.components.AnimationComponent;
import com.testroom.components.GrapnelComponent;
import com.testroom.components.MovementComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TextureComponent;
import com.testroom.components.TransformComponent;
import com.testroom.physics.PhysicsDataStructure;
import com.testroom.physics.PhysicsManager;
import com.testroom.physics.PhysicsObjectType;
import com.testroom.rendering.GraphicsAsset;
import com.testroom.systems.GrapnelSystem;

public class GrapnelBuilder {
	private Engine engine;
	
	public GrapnelBuilder(Engine engine) {
		this.engine = engine;
	}
	
	public Entity build(Body anchorPlayer, Vector2 p) {
		Entity entity = new Entity();

		AnimationComponent animation = new AnimationComponent();
		GrapnelComponent grapnel = new GrapnelComponent();
		MovementComponent movement = new MovementComponent();
		TransformComponent position = new TransformComponent();
		StateComponent state = new StateComponent();
		TextureComponent texture = new TextureComponent();
		
		animation.animations.put(GrapnelComponent.STATE_GRAB, GraphicsAsset.grapnel);
		animation.animations.put(GrapnelComponent.STATE_RECALL, GraphicsAsset.grapnel);
		animation.animations.put(GrapnelComponent.STATE_THROW, GraphicsAsset.grapnel);
		
		state.set(GrapnelComponent.STATE_THROW);
		
		position.pos.set(p);
		position.scale.set(0.5f,0.5f);
		
		entity.add(animation);
		entity.add(grapnel);
		entity.add(movement);		
		entity.add(position);
		entity.add(state);
		entity.add(texture);
		
		engine.addEntity(entity);
		
		GrapnelSystem grapnelSystem = engine.getSystem(GrapnelSystem.class);
				
		grapnelSystem.setProcessing(true);
		
		PhysicsDataStructure s = new PhysicsDataStructure(new PhysicsGrapnel(entity, grapnelSystem),
				  										  PhysicsObjectType.GRAPNEL);
		position.body = PhysicsManager.getInstance().createDynamicCircle(
		position.pos.cpy(), GrapnelComponent.WIDTH/2, s);

		movement.velocity.scl(position.body.getMass());		
		position.body.setAngularDamping(0);
		
		RopeJointDef jointDef = new RopeJointDef();
		jointDef.bodyA = position.body;
		jointDef.bodyB = anchorPlayer;
		jointDef.collideConnected = true;
		jointDef.maxLength = GrapnelComponent.MAX_DISTANCE;
		
		grapnel.grapnelJoint = (RopeJoint) PhysicsManager.getInstance().createJoint(jointDef);
		
		return entity;
	}
}
