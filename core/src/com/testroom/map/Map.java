package com.testroom.map;

import java.util.Collection;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

import com.badlogic.gdx.math.Vector2;

public class Map {
	/*private Engine engine;*/
	/*private TiledMap tiledMap;*/
	private TiledMapRenderer tiledMapRenderer;
	/*private TiledMapTileLayer layer;*/
	private Vector2 spawn;
	private Vector2 size;
	/*private Collection<Edge> edges;*/

	public Map(Engine engine, TiledMap tiledMap,
			TiledMapRenderer tiledMapRenderer, Vector2 spawn,
			Vector2 size,
			Collection<Edge> edges) {
		/*this.engine = engine;*/
		/*this.tiledMap = tiledMap;*/
		this.tiledMapRenderer = tiledMapRenderer;
		this.spawn = spawn;
		this.size = size;
		/*this.edges = edges;*/
	}
	
	public void render(OrthographicCamera cam) {
		tiledMapRenderer.setView(cam);
		tiledMapRenderer.render();
	}
	
	public Vector2 getSpawn() {
		return spawn;
	}

	public Vector2 getSize() {
		return size;
	}
}
