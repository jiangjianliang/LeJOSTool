package com.wander.train.nxt.test;

import com.wander.train.nxt.Command;

import lejos.nxt.Motor;

public class Test {
	private static Test instance = new Test();
	
	public static Test getInstance(){
		return instance;
	}
	
	public void test(int cmd){
		int degree = 35;
		
		if (cmd == Command.SWITCH_BRANCH) {
			//LCD.drawString("LEFT", 0, 1);
			Motor.A.setSpeed(degree*3);
			Motor.A.rotate(degree+10);
		} else if (cmd == Command.SWITCH_MAIN) {
			//LCD.drawString("RIGHT", 0, 1);
			Motor.A.setSpeed(degree*5);
			Motor.A.rotate(-degree-10);
		}
	}
	
}
