package com.wander.train.nxt.cmd;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;



/**
 * 更新距离的命令
 * @author wander
 * @deprecated
 */
class UpdateDistanceCommand implements Command {
	private static UpdateDistanceCommand instance = null;

	private UltrasonicSensor sonic;
	private DataOutputStream out;

	private UpdateDistanceCommand(UltrasonicSensor sonic, DataOutputStream out) {
		this.sonic = sonic;
		this.out = out;
	}

	public static UpdateDistanceCommand getInstance(UltrasonicSensor sonic,
			DataOutputStream out) {
		if (instance == null) {
			instance = new UpdateDistanceCommand(sonic, out);
		}
		return instance;
	}

	@Override
	public boolean execute() {
		int distance = sonic.getDistance();
		LCD.drawInt(distance, 3, 10, 1);
		try {
			out.writeInt(distance);
			// out.writeInt(isPressed);
			out.flush();
		} catch (IOException e) {
			LCD.drawString("ERROR WRITE!", 0, 3);
		}
		return true;
	}
}
