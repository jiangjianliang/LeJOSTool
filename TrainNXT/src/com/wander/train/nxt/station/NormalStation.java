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
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

//import lejos.nxt.comm.USB;

public class NormalStation implements Config {
	
	
	
	public static void main(String[] args) {
		LCD.drawString("waiting...", 0, 0);

		// NXTConnection connection = USB.waitForConnection();

		NXTConnection connection = Bluetooth.waitForConnection();
		DataInputStream pcDin = connection.openDataInputStream();
		DataOutputStream pcDout = connection.openDataOutputStream();

		LCD.drawString("connected.", 0, 0);
		LCD.drawString("distance:", 0, 1);
		LCD.drawString("color", 0, 2);
		LCD.drawString("cmd:", 0, 3);

		UltrasonicSensor sonic = new UltrasonicSensor(UltrasonicPort);
		ColorSensor color = new ColorSensor(ColorPort);
		
		ControlData ca = new ControlData();
		StationCommandFactory cmdFactory = StationCommandFactory.getInstance( ca);

		CommandExecutor cmdExecutor = new CommandExecutor(ca);
		cmdExecutor.start();

		CommandReceiver cmdReceiver = new CommandReceiver(pcDin, ca,
				cmdFactory, cmdExecutor);
		cmdReceiver.start();
		
		UltrasonicDetector ultrasonicDector = new UltrasonicDetector(sonic, ca);
		ultrasonicDector.start();
		
		ColorDetector colorDetector = new ColorDetector(color, ca);
		colorDetector.start();
		
		SensorReporter report = new SensorReporter(pcDout, ca);
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
