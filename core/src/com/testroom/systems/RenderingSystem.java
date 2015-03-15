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
	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;
	static final float PIXELS_TO_METRES = 1.0f / 32.0f;
	
	private SpriteBatch batch;
	private Array<Entity> renderQueue;
	private Camera cam;
	private ComponentMapper<TextureComponent> textureM;
	private ComponentMapper<TransformComponent> transformM;

	public RenderingSystem(Camera cam) {
		super(Family.getFor(TransformComponent.class, TextureComponent.class));
	
		textureM = ComponentMapper.getFor(TextureComponent.class);
		transformM = ComponentMapper.getFor(TransformComponent.class);
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
			TextureComponent tex = textureM.get(entity);
			
			if (tex.region == null) {
				continue;
			}
			
			TransformComponent t = transformM.get(entity);
		
			float width = tex.region.getRegionWidth();
			float height = tex.region.getRegionHeight();
			float originX = width * 0.5f;
			float originY = height * 0.5f;
			
			batch.draw(tex.region,
					   t.pos.x - originX, t.pos.y - originY,
					   originX, originY,
					   width, height,
					   t.scale.x * PIXELS_TO_METRES, t.scale.y * PIXELS_TO_METRES,
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
