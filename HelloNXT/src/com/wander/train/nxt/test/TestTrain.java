package com.wander.train.nxt.test;

import com.wander.train.nxt.IrLinkExt;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class TestTrain {
	public static void main(String[] args){
		IrLinkExt link = new IrLinkExt(SensorPort.S1);
		int count = 0;
		while(true){
			if(Button.ENTER.isDown()){
				count++;
				link.sendPFSingleModePWM(0, IrLinkExt.PF_SINGLE_MODE_BLUE_PORT, 3);
			}
			else if(Button.ESCAPE.isDown()){
				count++;
				link.sendPFSingleModePWM(1, IrLinkExt.PF_SINGLE_MODE_BLUE_PORT, 0);
			}
			else if(Button.LEFT.isDown()){
				count++;
				link.sendPFSingleModePWM(2, IrLinkExt.PF_SINGLE_MODE_BLUE_PORT, 3);				
			}
			else if(Button.RIGHT.isDown()){
				count++;
				link.sendPFSingleModePWM(3, IrLinkExt.PF_SINGLE_MODE_BLUE_PORT, 0);
				
			}
			Button.waitForAnyPress();
		}
	}
}
