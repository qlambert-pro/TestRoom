package com.testroom.character;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import com.testroom.components.AnimationComponent;
import com.testroom.components.MovementComponent;
import com.testroom.components.PlayerComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TextureComponent;
import com.testroom.components.TransformComponent;
import com.testroom.controls.PlayerControls;
import com.testroom.rendering.GraphicsAsset;
import com.testroom.systems.PlayerSystem;
 
public class CharacterBuilder extends Component {
	private Engine engine;

	public CharacterBuilder(Engine engine) {
		this.engine = engine;
	}

	public Entity build(Controller c, Vector2 p) {
		Entity entity = new Entity();
		
		AnimationComponent animation = new AnimationComponent();
		PlayerComponent player = new PlayerComponent();
		MovementComponent movement = new MovementComponent();
		TransformComponent position = new TransformComponent();
		StateComponent state = new StateComponent();
		TextureComponent texture = new TextureComponent();
		
		animation.animations.put(PlayerComponent.STATE_GRAB, GraphicsAsset.characterGrab);
		animation.animations.put(PlayerComponent.STATE_GRABBING, GraphicsAsset.characterGrabbing);
		animation.animations.put(PlayerComponent.STATE_JUMP, GraphicsAsset.characterJump);
		
		position.pos.set(p);
		movement.velocity.set(1.0f,0.0f);
		 
		state.set(PlayerComponent.STATE_GRABBING);
		
		entity.add(animation);
		entity.add(player);
		entity.add(movement);		
		entity.add(position);
		entity.add(state);
		entity.add(texture);
		
		engine.addEntity(entity);
		
		PlayerSystem e = new PlayerSystem(entity);
		c.addListener(new PlayerControls(e));
		engine.addSystem(e);
		
		return entity;
	}

}
