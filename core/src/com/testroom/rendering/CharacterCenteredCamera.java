package com.testroom.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.testroom.components.TransformComponent;
import com.testroom.configuration.ConfigManager;



public class CharacterCenteredCamera extends OrthographicCamera{
	private TransformComponent player;

	public CharacterCenteredCamera(TransformComponent p) {
		super(ConfigManager.camWidth  * ConfigManager.minBlockSize,
			  ConfigManager.camHeight * ConfigManager.minBlockSize);
		this.player = p;		
		
		update();
	}

	public void follow() {							
		position.set(player.pos.cpy(), 5);
		update();
	}
}
