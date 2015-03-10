package com.testroom.configuration;

import com.badlogic.gdx.math.Vector2;

public class ConfigManager {
	public static int minBlockSize = 60;
	
	public static float moveSpeed = 10;
	public static float accTime = 0.2f;
	public static float jumpHeight = 4.8f;
	public static float gravity = 60;
	
	public static float friction = 0.6f;
	
	/* Player display size */
	public static float swanHeight = minBlockSize * 2;

	/* Player physics size */
	public static Vector2 swanPhysicsSize = new Vector2(minBlockSize * 0.875f,minBlockSize * 1.75f);

	public static float physicsStepSize = 1f/60f;
	
	
	/* Camera settings */
	public static float camWidth = 32;
	public static float camHeight = 18;	
}
