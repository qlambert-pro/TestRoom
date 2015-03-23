package com.testroom.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent extends Component {
	public static final int STATE_JUMP = 0;
	public static final int STATE_GRAB = 1;
	public static final int STATE_GRABBING = 2;
	
	public static final float MOVE_VELOCITY = 10f;
	public static final float WIDTH = 0.8f;
	
	public static final float MASS = 1.0f;
}
