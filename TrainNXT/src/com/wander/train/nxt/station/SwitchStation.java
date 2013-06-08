package com.wander.train.nxt.station;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.wander.train.nxt.cmd.StationCommandFactory;
import com.wander.train.nxt.common.BluetoothWriter;
import com.wander.train.nxt.common.CommandExecutor;
import com.wander.train.nxt.common.CommandReceiver;
import com.wander.train.nxt.common.Config;
import com.wander.train.nxt.common.ControlData;
import com.wander.train.nxt.common.HeartBeat;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
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
		LCD.drawString("color", 0, 2);
		LCD.drawString("cmd:", 0, 3);

		UltrasonicSensor sonic = new UltrasonicSensor(UltrasonicPort);
		ColorSensor color = new ColorSensor(ColorPort);
		
		ControlData cdata = new ControlData();
		StationCommandFactory cmdFactory = StationCommandFactory.getInstance(cdata);
		BluetoothWriter writer = BluetoothWriter.getInstance(pcDout);

		CommandExecutor cmdExecutor = new CommandExecutor(cdata, Config.CommandExecutorPeriod);
		cmdExecutor.start();

		CommandReceiver cmdReceiver = new CommandReceiver(pcDin, cdata,
				cmdFactory, cmdExecutor);
		cmdReceiver.start();
		
		
		UltrasonicDetector ultrasonicDector = new UltrasonicDetector(sonic);
		ultrasonicDector.start();
		
		ColorDetector colorDetector = new ColorDetector(color);
		colorDetector.start();
		
		SensorReporter report = new SensorReporter(writer, cdata, Config.SensorReporterPeriod);
		report.start();
		
		HeartBeat heart = HeartBeat.getInstance(writer, cdata, Config.HeartBeatPeriod);
		heart.start();
		
		while (!Button.ESCAPE.isDown() && cdata.isKeepOn()) {

		}
		cdata.setKeepOn(false);

		try {
			pcDin.close();
			pcDout.close();
			connection.close();
		} catch (IOException e) {
			LCD.drawString("CLOSE ERROR.", 0, 7);
		}
		//LCD.clear();
		LCD.drawString("good bye", 5, 4);
		//Button.waitForAnyPress();
	}
}
