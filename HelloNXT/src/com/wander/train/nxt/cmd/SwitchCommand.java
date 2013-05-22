package com.wander.train.nxt.cmd;

import lejos.nxt.LCD;
import lejos.nxt.Motor;


/**
 * 变轨命令
 * 
 * @author wander
 * 
 */
class SwitchCommand implements Command {
	private static SwitchCommand instance = new SwitchCommand();

	private boolean switchToMain = false;

	private static int DEGREE = 35;

	private SwitchCommand() {
	}

	public static SwitchCommand getInstance(boolean isMain) {
		instance.setSwitchMain(isMain);
		return instance;
	}

	@Override
	public boolean execute() {
		if (switchToMain) {
			LCD.drawString("switch main  ", 0, 7);
			Motor.A.setSpeed(DEGREE * 3);
			Motor.A.rotate(-DEGREE - 15);
			Motor.A.rotate(5);
		} else {
			LCD.drawString("switch branch", 0, 7);
			Motor.A.setSpeed(DEGREE * 3);
			Motor.A.rotate(DEGREE + 10);
		}
		return true;
	}

	public void setSwitchMain(boolean switchMain) {
		this.switchToMain = switchMain;
	}

}
