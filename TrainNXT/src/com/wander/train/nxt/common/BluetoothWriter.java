package com.wander.train.nxt.common;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;

public class BluetoothWriter {
	private static BluetoothWriter instance = null;
	private DataOutputStream out;
	
	private BluetoothWriter(DataOutputStream out){
		this.out = out;
	}
	
	public static BluetoothWriter getInstance(DataOutputStream out){
		if(instance == null){
			instance = new BluetoothWriter(out);
		}
		return instance;
	}
	
	public synchronized void synWriteIntAndFlush(int data, String des){
		try {
			out.writeInt(data);
			out.flush();
		} catch (IOException e) {
			LCD.drawString("ERROR "+des, 0, 3);
		}
	}

	public void writeIntAndFlush(int data, String des){
		try {
			out.writeInt(data);
			out.flush();
		} catch (IOException e) {
			LCD.drawString("ERROR "+des, 0, 3);
		}
	}
}
