package com.testroom.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;

public class PlayerComponent extends Component {
	public static final int STATE_JUMP = 0;
	public static final int STATE_GRAB = 1;
	public static final int STATE_GRABBING = 2;
	
	public static final float MOVE_VELOCITY = 1000f;
	public static final float WIDTH = 0.8f;
	
	public static final float MASS = 1.0f;
	
	public WeldJoint joint = null;
	public Body grabbed = null;
	public Vector2 grabbedPos = null;
}
