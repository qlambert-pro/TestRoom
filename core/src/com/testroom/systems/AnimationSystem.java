package com.testroom.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.testroom.components.AnimationComponent;
import com.testroom.components.StateComponent;
import com.testroom.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {
	private ComponentMapper<TextureComponent> tm;
	private ComponentMapper<AnimationComponent> am;
	private ComponentMapper<StateComponent> sm;
	
	public AnimationSystem() {
		super(Family.getFor(TextureComponent.class,
				AnimationComponent.class,
				StateComponent.class));
		tm = ComponentMapper.getFor(TextureComponent.class);
		am = ComponentMapper.getFor(AnimationComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
	}
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		long id = entity.getId();
		TextureComponent tex = tm.get(entity);
		AnimationComponent anim = am.get(entity);
		StateComponent state = sm.get(entity);
		
		Animation animation = anim.animations.get(state.get());
		if (animation != null) {
			tex.region = animation.getKeyFrame(state.time);
		}
		state.time += deltaTime;
	}

}
