package com.testroom.controls;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.testroom.systems.PlayerSystem;

public class PlayerControls extends ControllerAdapter {
	private PlayerSystem system;
	private long id;
	
	public PlayerControls(PlayerSystem syst, long id) {
		this.system = syst;
		this.id = id;
	}
	
	@Override
	public boolean buttonDown (Controller controller, int buttonCode){
		switch (buttonCode) {
		case Xbox360Pad.BUTTON_A:
			system.jump(id,
					controller.getAxis(Xbox360Pad.AXIS_LEFT_X),
					controller.getAxis(Xbox360Pad.AXIS_LEFT_Y));
			return true;
		case Xbox360Pad.BUTTON_RB:
			system.shootGrapnel(id,
					controller.getAxis(Xbox360Pad.AXIS_LEFT_X),
					controller.getAxis(Xbox360Pad.AXIS_LEFT_Y));
			return true;
		}
		
		return false;
	}

	@Override
	public boolean buttonUp (Controller controller, int buttonCode){
		switch (buttonCode) {
		case Xbox360Pad.BUTTON_A:
			system.grabbing(id);
			return true;
		case Xbox360Pad.BUTTON_RB:
			system.recallGrapnel(id);
			return true;
		}
		
		return false;
	}
	
}
