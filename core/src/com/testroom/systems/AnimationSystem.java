package com.testroom.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.testroom.components.AnimationComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {
	
	public AnimationSystem() {
		super(Family.getFor(TextureComponent.class,
				AnimationComponent.class,
				StateComponent.class));
	}
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		long id = entity.getId();
		TextureComponent tex = entity.getComponent(TextureComponent.class);
		AnimationComponent anim = entity.getComponent(AnimationComponent.class);
		StateComponent state = entity.getComponent(StateComponent.class);
		
		Animation animation = anim.animations.get(state.get());
		if (animation != null) {
			tex.region = animation.getKeyFrame(state.time);
		}
		state.time += deltaTime;
	}

}
