package com.testroom.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.testroom.physics.PhysicsManager;

public class GrapnelComponent extends Component {
	public static final int STATE_GRAB = 0;
	public static final int STATE_UNGRAB = 1;

	public static final int STATE_THROW = 0;
	public static final int STATE_RECALL = 1;
	
	public static final float MOVE_VELOCITY = 2000f;
	public static final float RECALL_VELOCITY = 2000f;
	public static final float WIDTH = 0.4f;
	
	public static final float MAX_DISTANCE = 500f;
	
	// RopeSpring
	public float length0 = MAX_DISTANCE * PhysicsManager.WORLD_TO_BOX;
	public Body player = null;
	public int RopeSpringState = STATE_THROW;
	//
	
	public WeldJointDef jointDef = null;
	public WeldJoint joint = null;
	
	public boolean isDestroyed = false;
}
