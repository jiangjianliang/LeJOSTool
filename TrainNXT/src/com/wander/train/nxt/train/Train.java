package com.wander.train.nxt.train;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.wander.train.nxt.cmd.TrainCommandFactory;
import com.wander.train.nxt.common.BluetoothWriter;
import com.wander.train.nxt.common.CommandExecutor;
import com.wander.train.nxt.common.CommandReceiver;
import com.wander.train.nxt.common.Config;
import com.wander.train.nxt.common.ControlData;
import com.wander.train.nxt.common.HeartBeat;

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

		ControlData cdata = new ControlData();
		TrainCommandFactory cmdFactory = TrainCommandFactory.getInstance(cdata);
		BluetoothWriter writer = BluetoothWriter.getInstance(pcDout);

		CommandExecutor cmdExecutor = new CommandExecutor(cdata, Config.CommandExecutorPeriod);
		cmdExecutor.start();

		CommandReceiver cmdReceiver = new CommandReceiver(pcDin, cdata,
				cmdFactory, cmdExecutor);
		cmdReceiver.start();
		
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
		
		LCD.drawString("good bye", 5, 4);
		Button.waitForAnyPress();

	}
}
