package com.wander.nxt.test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.addon.RCXMotor;

public class TestRCXMotor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RCXMotor rcxMotor = new RCXMotor(MotorPort.A);
		//rcxMotor = new RCXMotor();
		LCD.drawString("run", 0, 2);
		while(true){
			if(Button.ESCAPE.isDown()){
				break;
			}
			else if(Button.ENTER.isDown()){
				LCD.drawString("STOP", 0, 0);
				rcxMotor.stop();
			}
			else if(Button.LEFT.isDown()){
				LCD.drawString("LEFT", 0, 0);
				rcxMotor.forward();
			}
			else if(Button.RIGHT.isDown()){
				LCD.drawString("RIGHT", 0, 0);
				rcxMotor.backward();
			}
			Button.waitForAnyPress();
		}
	}

}
