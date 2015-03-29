package com.testroom.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class GraphicsAsset {
	public static Animation characterJump;
	public static Animation characterGrabbing;
	public static Animation characterGrab;
	
	public static Animation grapnel;
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void load() {
		Texture characAnim = loadTexture("character.png"); 
		characterGrab =     new Animation(0.2f, new TextureRegion(characAnim, 0,   0, 400, 400), new TextureRegion(characAnim, 400,   0, 400, 400));
		characterGrab.setPlayMode(PlayMode.LOOP);
		characterGrabbing = new Animation(0.2f, new TextureRegion(characAnim, 0, 400, 400, 400), new TextureRegion(characAnim, 400, 400, 400, 400));
		characterGrabbing.setPlayMode(PlayMode.LOOP);
		characterJump =     new Animation(0.2f, new TextureRegion(characAnim, 0, 800, 400, 400), new TextureRegion(characAnim, 400, 800, 400, 400));
		characterJump.setPlayMode(PlayMode.LOOP);
		
		grapnel = new Animation(0.2f, new TextureRegion(characAnim, 0, 1200, 400, 400), new TextureRegion(characAnim, 400, 1200, 400, 400));
	}
}
