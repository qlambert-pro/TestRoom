package com.testroom.mode;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.testroom.physics.PhysicsManager;
import com.testroom.rendering.CharacterCenteredCamera;
import com.testroom.systems.AnimationSystem;
import com.testroom.systems.RenderingSystem;
import com.testroom.TestRoom;
import com.testroom.character.CharacterBuilder;
import com.testroom.components.TransformComponent;
import com.testroom.map.Map;
import com.testroom.map.MapLoader;

public class GameMode extends ScreenAdapter{
	private TestRoom game;
	
	private Engine engine;
	private MapLoader mapLoader;
	private CharacterBuilder characterBuilder;
	private Map map;
	
	private CharacterCenteredCamera cam;
	
	public GameMode(TestRoom g) {
		game = g;
	
		engine = new Engine();		
		mapLoader = new MapLoader(engine);
		characterBuilder = new CharacterBuilder(engine);
		
		/* Init Map */
		map = mapLoader.load();
		
		/* Init Character */
		Controller c = Controllers.getControllers().first();
		Entity e = characterBuilder.build(c, map.getSpawn());
		cam = new  CharacterCenteredCamera(e.getComponent(TransformComponent.class));

		engine.addSystem(new AnimationSystem());
		engine.addSystem(new RenderingSystem(cam));
	}
	
	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		PhysicsManager.getInstance().update(dt);
		engine.update(dt);
		cam.follow();
		draw(dt);
	}

	private void draw(float dt) {
		map.render(cam); 
	}





}
