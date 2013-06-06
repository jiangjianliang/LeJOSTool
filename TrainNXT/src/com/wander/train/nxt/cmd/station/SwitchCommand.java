package com.wander.train.nxt.cmd.station;

import com.wander.train.nxt.cmd.Command;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;


/**
 * 变轨命令
 * 
 * @author wander
 * 
 */
public class SwitchCommand implements Command {
	private static SwitchCommand instance = new SwitchCommand();
	private NXTRegulatedMotor motor = Motor.A;
	private boolean switchToMain = false;

	private static int DEGREE = 720+360+240;

	private SwitchCommand() {
	}

	public static SwitchCommand getInstance(boolean isMain) {
		instance.setSwitchMain(isMain);
		return instance;
	}

	@Override
	public boolean execute() {
		if (switchToMain) {
			motor.setSpeed(DEGREE);		
			LCD.drawString("switch main  ", 0, 7);
			motor.rotate(DEGREE);
		} else {
			motor.setSpeed(DEGREE);		
			LCD.drawString("switch branch", 0, 7);
			motor.rotate(-DEGREE);
		}
		return true;
	}

	public void setSwitchMain(boolean switchMain) {
		this.switchToMain = switchMain;
	}

}
