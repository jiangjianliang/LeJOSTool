package com.wander.train.nxt.train;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.wander.train.nxt.cmd.TrainCommandFactory;
import com.wander.train.nxt.common.CommandExecutor;
import com.wander.train.nxt.common.CommandReceiver;
import com.wander.train.nxt.common.Config;
import com.wander.train.nxt.common.ControlData;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

//import lejos.nxt.comm.USB;

public class Train implements Config {
	
	
	
	public static void main(String[] args) {
		
		LCD.drawString("waiting...", 0, 0);

		NXTConnection connection = Bluetooth.waitForConnection();
		DataInputStream pcDin = connection.openDataInputStream();
		DataOutputStream pcDout = connection.openDataOutputStream();

		LCD.drawString("connected.", 0, 0);
		
		LCD.drawString("cmd:", 0, 2);

		ControlData ca = new ControlData();
		TrainCommandFactory cmdFactory = TrainCommandFactory.getInstance(ca);

		CommandExecutor cmdExecutor = new CommandExecutor(ca);
		cmdExecutor.start();

		CommandReceiver cmdReceiver = new CommandReceiver(pcDin, ca,
				cmdFactory, cmdExecutor);
		cmdReceiver.start();
		
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
