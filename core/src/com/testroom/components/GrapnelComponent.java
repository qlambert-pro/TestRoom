package com.testroom.components;

import com.badlogic.ashley.core.Component;

public class GrapnelComponent extends Component {
	public static final int STATE_THROW = 0;
	public static final int STATE_GRAB = 1;
	public static final int STATE_RECALL = 2;
	
	public static final float MOVE_VELOCITY = 2000f;
	public static final float RECALL_VELOCITY = 5000f;
	public static final float WIDTH = 0.4f;
	
	public static final float MAX_DISTANCE = 5f;
	public float distance = MAX_DISTANCE;
}
