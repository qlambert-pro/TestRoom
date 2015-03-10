package com.testroom.map;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.testroom.map.TileType;
import com.testroom.physics.PhysicsObjectType;
import com.testroom.configuration.ConfigManager;

public class MapLoader {
	private static final String LAYER = "layer";
	
	private Engine engine;
	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMapTileLayer layer;
	private Vector2 spawn;
	
	private Collection<Edge> edges;
	
	public MapLoader(Engine e) {
		engine = e;
	}

	public Map load() {
		// TODO Auto-generated method stub
		tiledMap = new TmxMapLoader().load("map.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		
		edges = new LinkedList<Edge>();
		
		TiledMapTileLayer layer  = getLayer(LAYER);
		
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell  = layer.getCell(x, y);
				
				String type = null;
				
				MapProperties properties= null; 
				if (cell != null) {
					properties  = cell.getTile().getProperties();
					if (properties != null)
						type = (String) properties.get(TilePropertieConstants.PROPERTY_NAME_TYPE);
				}
							
				if (type != null)
					addWorldTile(x, y, getTypeFromString(type), properties);											
			}
		}
		
		Vector2 size = new Vector2(getLayer(LAYER).getWidth()
				* ConfigManager.minBlockSize, getLayer(
				LAYER).getHeight()
				* ConfigManager.minBlockSize);
		
		return new Map(engine, tiledMap, tiledMapRenderer, spawn, size, edges);
	}
	
	private void addWorldTile(int x, int y, TileType type, MapProperties properties) {
		switch (type) {
		case WALL:
			extendCell(x, y, layer, type);
			break;
		case SPAWN:
			spawn = new Vector2(x * ConfigManager.minBlockSize,
								y * ConfigManager.minBlockSize);
			break;
		default:
			break;
		}
		
	}
	
	private void extendCell(int x, int y, TiledMapTileLayer layer,
			TileType type) {
		// Left
		if (shouldIAddEdge(x - 1, y, type, layer))
			createEdge(x, y, x, y + 1, type);

		// Right
		if (shouldIAddEdge(x + 1, y, type, layer))
			createEdge(x + 1, y, x + 1, y + 1, type);

		// Top
		if (shouldIAddEdge(x, y + 1, type, layer))
			createEdge(x, y + 1, x + 1, y + 1, type);

		// Bottom
		if (shouldIAddEdge(x, y - 1, type, layer))
			createEdge(x, y, x + 1, y, type);
	}

	private void createEdge(int x1, int y1, int x2, int y2,
			TileType type) {
		Vector2 beg = new Vector2(x1, y1);
		Vector2 end = new Vector2(x2, y2);
		Edge edge = new Edge(beg, end, getPhysicTypeFromTileType(type));
		edges.add(edge);
	}
	
	private boolean shouldIAddEdge(int x, int y, TileType type, TiledMapTileLayer layer) {
		Cell otherCell;
		if (x < 0 || x >= layer.getWidth() || y < 0 || y >= layer.getHeight()) {
			return true;
		}
		otherCell = layer.getCell(x, y);		
				
		String otherType = null;
		
		if (otherCell != null) {
			MapProperties otherProperties  = otherCell.getTile().getProperties();
			if (otherProperties != null)
				otherType = (String) otherProperties.get(TilePropertieConstants.PROPERTY_NAME_TYPE);
		}						
		
		return type != getTypeFromString(otherType);
	}

	private TileType getTypeFromString(String s) {
		if (s == null)
			return null;
		
		TileType type = null;
		
		if (s.equals(TilePropertieConstants.PROPERTY_VALUE_TYPE_WALL))
			type = TileType.WALL;
		else if (s.equals(TilePropertieConstants.PROPERTY_VALUE_TYPE_SPAWN))
			type = TileType.SPAWN;
		else if (s.equals(TilePropertieConstants.PROPERTY_VALUE_TYPE_NONE))
			type = TileType.NONE;
		
		return type;
	}
	
	private PhysicsObjectType getPhysicTypeFromTileType(TileType t){
		PhysicsObjectType type = null;
		
		switch(t) {
		case WALL:
			type = PhysicsObjectType.SOLID;
			break;
		default:
			break;
		}
		
		return type;
	}
	
	private TiledMapTileLayer getLayer(String layerName) {
		return (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
	}
}
