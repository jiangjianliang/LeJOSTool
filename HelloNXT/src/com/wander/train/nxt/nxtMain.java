package com.wander.train.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
//import lejos.nxt.comm.USB;

public class nxtMain {
	public static void main(String[] args) {
		LCD.drawString("waiting...", 0, 0);
		
		//NXTConnection connection = USB.waitForConnection();
		
		NXTConnection connection = Bluetooth.waitForConnection();
		DataInputStream pcDin = connection.openDataInputStream();
		DataOutputStream pcDout = connection.openDataOutputStream();
		
		LCD.drawString("connected.", 0, 0);
		LCD.drawString("distance:", 0, 1);
		LCD.drawString("cmd:", 0, 2);
		
		IrLinkExt link = new IrLinkExt(SensorPort.S1);
		//TouchSensor touch = new TouchSensor(SensorPort.S3);
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S4);
		
		CommandFactory cmdFactory = CommandFactory.getInstance();
		//Test test = Test.getInstance();
		int count = 0;
		while (true) {
			try {
				
				int cmd =  pcDin.readInt();
				LCD.drawInt(cmd, 6, 5, 2);
				
				Command concreteCommand = cmdFactory.parseCommand(cmd, link, sonic, pcDout);
				boolean result = concreteCommand.execute();
				if( !result){
					break;
				}
				
			}
			 catch (Exception e) {
				LCD.drawString("ERROR READ!", 0, 7);
				break;
			}
			
		}
		LCD.drawString("good bye", 5, 4);
		Button.waitForAnyPress();
		 
	}
}
















