package com.wander.train.nxt.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

import com.wander.train.nxt.CommandExecutor;
import com.wander.train.nxt.CommandReceiver;
import com.wander.train.nxt.ControlData;
import com.wander.train.nxt.IrLinkExt;
import com.wander.train.nxt.ReportDistance;
import com.wander.train.nxt.cmd.CommandFactory;

public class TestDataOutput {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LCD.drawString("waiting...", 0, 0);
		
		//NXTConnection connection = USB.waitForConnection();
		
		NXTConnection connection = Bluetooth.waitForConnection();
		DataInputStream pcDin = connection.openDataInputStream();
		DataOutputStream pcDout = connection.openDataOutputStream();
		
		LCD.drawString("connected.", 0, 0);
		LCD.drawString("distance:", 0, 1);
		LCD.drawString("cmd:", 0, 2);
		
		TouchSensor touch = new TouchSensor(SensorPort.S3);
		
		ControlData ca = new ControlData();
		ReportDistance report = new ReportDistance(touch, pcDout, ca);
		report.start();
		ca.setStart(true);
		while(!Button.ESCAPE.isDown()){
			
		}
		ca.setKeepOn(false);
		
		try {
			pcDin.close();
			pcDout.close();
			connection.close();
		} catch (IOException e) {
			LCD.drawString("CLOSE ERROR.", 0, 7);
		}
		//LCD.drawString("good "+ count, 5, 4);
		LCD.drawString("good bye", 5, 4);
		Button.waitForAnyPress();
	}

}
