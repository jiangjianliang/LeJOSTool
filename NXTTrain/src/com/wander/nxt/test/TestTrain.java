package com.wander.nxt.test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class TestTrain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NXTRegulatedMotor forward = Motor.A;
		//NXTRegulatedMotor backward = Motor.B;
		int base = 80;
		int speed = 80;
		LCD.drawString("run", 0, 2);
		while(true){
			if(Button.ESCAPE.isDown()){
				break;
			}
			else if(Button.ENTER.isDown()){
				LCD.drawString("STOP", 0, 0);
				//forward.stop();
				forward.flt();
			}
			else if(Button.LEFT.isDown()){
				LCD.drawString("LEFT", 0, 0);
				LCD.drawInt(speed, 0, 1);
				forward.setSpeed(10);
				//speed += base;
				//backward.stop();
				forward.forward();
				//backward.backward();
			}
			else if(Button.RIGHT.isDown()){
				LCD.drawString("RIGHT", 0, 0);
				//forward.stop();
				forward.setSpeed(40);
				forward.forward();
				//backward.forward();
			}
			Button.waitForAnyPress();
		}
	}

}
