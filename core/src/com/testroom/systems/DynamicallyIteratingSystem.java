package com.testroom.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;


public abstract class DynamicallyIteratingSystem extends EntitySystem {
	private Family family;
	private Engine engine;
	
	public DynamicallyIteratingSystem(Family f) {
		this(f, 0);
	}
	
	public DynamicallyIteratingSystem(Family family, int priority) {
		super(priority);
		
		this.family = family;
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void removedFromEngine(Engine engine) {
		this.engine = null;
	}
	
	@Override
	public void update (float dt) {
		ImmutableArray<Entity> entities = engine.getEntitiesFor(family);
		for (int i = 0; i < entities.size(); i++) 
			processEntity(entities.get(i), dt);
	}

	protected abstract void processEntity(Entity entity, float dt);
	
}
