package com.wander.train.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.wander.train.nxt.cmd.Command;
import com.wander.train.nxt.cmd.CommandFactory;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
//import lejos.nxt.comm.USB;

public class nxtSub {
	public static void main(String[] args) {
		LCD.drawString("waiting...", 0, 0);
		
//		NXTConnection connection = USB.waitForConnection();
		NXTConnection connection = Bluetooth.waitForConnection();
		DataInputStream pcDin = connection.openDataInputStream();
		DataOutputStream pcDout = connection.openDataOutputStream();
		LCD.drawString("connected.", 0, 0);
		LCD.drawString("distance:", 0, 1);
		LCD.drawString("cmd:", 0, 2);
		
		TouchSensor touch = new TouchSensor(SensorPort.S3);
		//UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S4);
		CommandFactory cmdFactory = CommandFactory.getInstance();
		CommandExecutor cmdExecutor = new CommandExecutor();
		cmdExecutor.start();
		while (true) {
			try {
				int cmd = pcDin.readInt();
				LCD.drawInt(cmd, 4, 5, 2);
				Command concreteCommand = cmdFactory.parseCommand(cmd, null, null, pcDout, touch);
				cmdExecutor.addCommand(concreteCommand);
				/*
				if(!concreteCommand.execute()){
					break;
				}
				*/
			} catch (IOException e) {
				LCD.drawString("ERROR READ!", 0, 7);
				break;
			}
		}
		try {
			pcDin.close();
			pcDout.close();
			connection.close();
		} catch (IOException e) {
			LCD.drawString("CLOSE ERROR.", 0, 7);
		}
		LCD.drawString("good bye", 5, 4);
		Button.waitForAnyPress();
	}
}
















