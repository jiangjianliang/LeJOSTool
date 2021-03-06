package com.wander.train.nxt.cmd;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;


/**
 * 变轨命令
 * 
 * @author wander
 * 
 */
class SwitchCommand implements Command {
	private static SwitchCommand instance = new SwitchCommand();
	private NXTRegulatedMotor motor = Motor.A;
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
			//LCD.drawString("switch main  ", 0, 7);
			motor.setSpeed(DEGREE * 3);
			motor.rotate(-DEGREE - 20);
			motor.rotate(10);
		} else {
			//LCD.drawString("switch branch", 0, 7);
			motor.setSpeed(DEGREE * 3);
			motor.rotate(DEGREE + 10);
		}
		return true;
	}

	public void setSwitchMain(boolean switchMain) {
		this.switchToMain = switchMain;
	}

}
