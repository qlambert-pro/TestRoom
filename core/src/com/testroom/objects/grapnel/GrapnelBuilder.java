package com.testroom.objects.grapnel;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.testroom.components.AnimationComponent;
import com.testroom.components.GrapnelComponent;
import com.testroom.components.MovementComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TextureComponent;
import com.testroom.components.TransformComponent;
import com.testroom.rendering.GraphicsAsset;
import com.testroom.systems.GrapnelSystem;

public class GrapnelBuilder {
	private Engine engine;
	
	public GrapnelBuilder(Engine engine) {
		this.engine = engine;
	}
	
	public Entity build(Vector2 p) {
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
		
		entity.add(animation);
		entity.add(grapnel);
		entity.add(movement);		
		entity.add(position);
		entity.add(state);
		entity.add(texture);
		
		engine.addEntity(entity);
				
		engine.getSystem(GrapnelSystem.class).setProcessing(true);
		
		return entity;
	}
}