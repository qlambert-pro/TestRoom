package com.testroom.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GraphicsAsset {
	public static Animation characterJump;
	public static Animation characterGrabbing;
	public static Animation characterGrab;
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void load() {
		Texture characAnim = loadTexture("character.png"); 
		characterGrab =     new Animation(0.2f, new TextureRegion(characAnim, 0,   0, 400, 400), new TextureRegion(characAnim, 400,   0, 400, 400));
		characterGrabbing = new Animation(0.2f, new TextureRegion(characAnim, 0, 400, 400, 400), new TextureRegion(characAnim, 400, 400, 400, 400));
		characterJump =     new Animation(0.2f, new TextureRegion(characAnim, 0, 800, 400, 400), new TextureRegion(characAnim, 400, 800, 400, 400));
	}
}
