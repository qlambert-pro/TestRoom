package com.testroom;


import com.badlogic.gdx.Game;
import com.testroom.mode.GameMode;
import com.testroom.rendering.GraphicsAsset;

public class TestRoom extends Game {
	private GameMode gameMode = null;
	
	@Override
	public void create () {
		GraphicsAsset.load();
		
		gameMode = new GameMode(this);
		setScreen(gameMode);
	}
}
