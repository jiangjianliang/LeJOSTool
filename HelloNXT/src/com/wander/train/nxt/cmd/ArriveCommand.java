package com.wander.train.nxt.cmd;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.TouchSensor;


/**
 * 到站命令
 * @deprecated
 */
class ArriveCommand implements Command {
	private static ArriveCommand instance = null;

	private TouchSensor touch;
	private DataOutputStream out;

	private ArriveCommand(TouchSensor touch, DataOutputStream out) {
		this.touch = touch;
		this.out = out;
	}

	public static ArriveCommand getInstance(TouchSensor touch, DataOutputStream out) {
		if (instance == null) {
			instance = new ArriveCommand(touch, out);
		}
		return instance;
	}

	@Override
	public boolean execute() {
		boolean isPressed = touch.isPressed();
		try {
			if (isPressed) {
				out.writeInt(1);
				out.flush();
			} else {
				out.writeInt(0);
				out.flush();
			}
		} catch (IOException e) {
			LCD.drawString("ERROR WRITE!", 0, 3);
		}
		return true;
	}

}