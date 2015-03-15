package com.testroom;


import com.badlogic.gdx.Game;
import com.testroom.mode.GameMode;

public class TestRoom extends Game {
	private GameMode gameMode = null;
	
	@Override
	public void create () {
		gameMode = new GameMode(this);
		setScreen(gameMode);
	}
}
