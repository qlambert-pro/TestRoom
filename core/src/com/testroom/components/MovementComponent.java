package com.testroom.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent extends Component {
	public final Vector2 velocity = new Vector2();
	public final Vector2 accel = new Vector2();
	public float rotation_speed = 0.0f;
}
