package com.wander.train.pc.common;

import java.io.DataInputStream;
/**
	 * 从蓝牙连接中读取数据
	 * @author wander
	 *
	 */
public abstract class BluetoothReader extends Thread {
	
	protected DataInputStream in;
	protected HeartDetectable nxt;
	
	public BluetoothReader(DataInputStream in, HeartDetectable nxt){
		this.in = in;
		this.nxt = nxt;
	}

	@Override
	public void run() {
		while(true){
			receiveAndAct();
		}
	}
	
	protected abstract void receiveAndAct();
}
