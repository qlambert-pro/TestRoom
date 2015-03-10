package com.testroom.mode;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ScreenAdapter;
import com.testroom.rendering.CharacterCenteredCamera;
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
		//TODO get controller ref
		Entity e = characterBuilder.build(null, map.getSpawn());
		cam = new  CharacterCenteredCamera(e.getComponent(TransformComponent.class));

	}
	
	@Override
	public void render(float dt) {
		engine.update(dt);
		cam.follow();
		draw(dt);
	}

	private void draw(float dt) {
		map.render(cam); 
	}





}
