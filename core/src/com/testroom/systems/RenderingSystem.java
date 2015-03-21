package com.testroom.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.testroom.components.TextureComponent;
import com.testroom.components.TransformComponent;

public class RenderingSystem extends IteratingSystem {
	
	private SpriteBatch batch;
	private Array<Entity> renderQueue;
	private Camera cam;

	public RenderingSystem(Camera cam) {
		super(Family.getFor(TransformComponent.class, TextureComponent.class));
	
		renderQueue = new Array<Entity>();
	
		this.batch = new SpriteBatch();
		this.cam = cam;
	}
	

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		for (Entity entity : renderQueue) {
			TextureComponent tex = entity.getComponent(TextureComponent.class);
			
			if (tex.region == null) {
				continue;
			}
			
			TransformComponent t = entity.getComponent(TransformComponent.class);
		
			float width = tex.region.getRegionWidth();
			float height = tex.region.getRegionHeight();
			float originX = width * 0.5f;
			float originY = height * 0.5f;
			
			batch.draw(tex.region,
					   t.pos.x - originX, t.pos.y - originY,
					   originX, originY,
					   width, height,
					   t.scale.x, t.scale.y,
					   MathUtils.radiansToDegrees * t.rotation);
		}
		
		batch.end();
		renderQueue.clear();
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) { 
		renderQueue.add(entity);
	}

}
