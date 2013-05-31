package com.wander.train.nxt.station;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.wander.train.nxt.cmd.StationCommandFactory;
import com.wander.train.nxt.common.CommandExecutor;
import com.wander.train.nxt.common.CommandReceiver;
import com.wander.train.nxt.common.Config;
import com.wander.train.nxt.common.ControlData;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

//import lejos.nxt.comm.USB;

public class SwitchStation implements Config {
	
	
	
	public static void main(String[] args) {
		LCD.drawString("waiting...", 0, 0);

		// NXTConnection connection = USB.waitForConnection();

		NXTConnection connection = Bluetooth.waitForConnection();
		DataInputStream pcDin = connection.openDataInputStream();
		DataOutputStream pcDout = connection.openDataOutputStream();

		LCD.drawString("connected.", 0, 0);
		LCD.drawString("distance:", 0, 1);
		LCD.drawString("cmd:", 0, 2);

		UltrasonicSensor sonic = new UltrasonicSensor(UltrasonicPort);

		ControlData ca = new ControlData();
		StationCommandFactory cmdFactory = StationCommandFactory.getInstance(ca);

		CommandExecutor cmdExecutor = new CommandExecutor(ca);
		cmdExecutor.start();

		CommandReceiver cmdReceiver = new CommandReceiver(pcDin, ca,
				cmdFactory, cmdExecutor);
		cmdReceiver.start();
		
		UltrasonicDistance report = new UltrasonicDistance(sonic, pcDout, ca);
		report.start();
		
		while (!Button.ESCAPE.isDown() && ca.isKeepOn()) {

		}
		ca.setKeepOn(false);

		try {
			pcDin.close();
			pcDout.close();
			connection.close();
		} catch (IOException e) {
			LCD.drawString("CLOSE ERROR.", 0, 7);
		}
		//LCD.clear();
		LCD.drawString("good bye", 5, 4);
		Button.waitForAnyPress();

	}
}
