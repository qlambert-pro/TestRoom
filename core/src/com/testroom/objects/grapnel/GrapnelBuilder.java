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
		animation.animations.put(GrapnelComponent.STATE_UNGRAB, GraphicsAsset.grapnel);
		
		state.set(GrapnelComponent.STATE_UNGRAB);
		
		position.pos.set(p);
		position.scale.set(0.2f, 0.2f);
		
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
		position.body = PhysicsManager.getInstance().createGrapnel(
						position.pos.cpy(), GrapnelComponent.WIDTH/2, s);
		
		position.body.setAngularDamping(0);

		grapnel.player = anchorPlayer;
		
		
		return entity;
	}
}
