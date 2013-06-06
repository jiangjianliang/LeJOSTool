package com.wander.train.pc.common;

import java.io.DataOutputStream;
import java.io.IOException;

public class BluetoothWriter {
	private DataOutputStream out;
	
	public BluetoothWriter(DataOutputStream out){
		this.out = out;
	}
	
	public synchronized void synWriteIntAndFlush(int data, String des){
		try {
			out.writeInt(data);
			out.flush();
		} catch (IOException e) {
			System.err.println("Error: "+des);
			//e.printStackTrace();
		}
	}
	
	public void writeIntAndFlush(int data, String des){
		try {
			out.writeInt(data);
			out.flush();
		} catch (IOException e) {
			System.err.println("Error: "+des);
			//e.printStackTrace();
		}
	}
}
