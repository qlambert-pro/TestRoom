package com.testroom.mode;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.testroom.objects.grapnel.GrapnelBuilder;
import com.testroom.physics.PhysicsManager;
import com.testroom.systems.AnimationSystem;
import com.testroom.systems.CameraSystem;
import com.testroom.systems.GrapnelSystem;
import com.testroom.systems.PhysicsSystem;
import com.testroom.systems.PlayerSystem;
import com.testroom.systems.RenderingSystem;
import com.testroom.TestRoom;
import com.testroom.character.CharacterBuilder;
import com.testroom.components.CameraComponent;
import com.testroom.configuration.ConfigManager;
import com.testroom.map.Map;
import com.testroom.map.MapLoader;

public class GameMode extends ScreenAdapter{
	private TestRoom game;
	
	private Engine engine;
	private MapLoader mapLoader;
	private CharacterBuilder characterBuilder;
	private Map map;
	
	private OrthographicCamera cam;
	
	public GameMode(TestRoom g) {
		game = g;
	
		engine = new Engine();		
		mapLoader = new MapLoader(engine);
		characterBuilder = new CharacterBuilder(engine);
		
		/* Init Map */
		map = mapLoader.load();
		
		PlayerSystem ps = new PlayerSystem(new GrapnelBuilder(engine));
				
		/* Init Character */
		Controller c = Controllers.getControllers().first();
		Entity e = characterBuilder.build(c, ps, map.getSpawn());
		
		cam = new OrthographicCamera(ConfigManager.camWidth  * ConfigManager.minBlockSize,
									 ConfigManager.camHeight * ConfigManager.minBlockSize);
		
		createCamera(e);
		
		engine.addSystem(ps);
		engine.addSystem(new GrapnelSystem(engine));
		engine.getSystem(GrapnelSystem.class).setProcessing(false);
		engine.addSystem(new PhysicsSystem());
		engine.addSystem(new CameraSystem());
		engine.addSystem(new AnimationSystem());
		engine.addSystem(new RenderingSystem(map, cam));
	}
	
	private void createCamera(Entity target) {
		Entity entity = new Entity();
		CameraComponent camera = new CameraComponent();
		camera.camera = cam;
		camera.target = target;
		entity.add(camera);
		engine.addEntity(entity);
	}
	
	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		PhysicsManager.getInstance().update(dt);		
		engine.update(dt);
	}
}
