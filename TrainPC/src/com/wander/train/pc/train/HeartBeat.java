package com.wander.train.pc.train;

import com.wander.train.pc.common.BluetoothWriter;
import com.wander.train.pc.common.Config;

public class HeartBeat extends Thread {
	private BluetoothWriter writer;
	private int period = 500;

	public HeartBeat(BluetoothWriter writer, int period) {
		this.writer = writer;
		this.period = period;
	}
	
	@Override
	public void run() {
		while (true) {
			writer.synWriteIntAndFlush(Config.HEART_BEAT, "write heartbeat");
			try {
				sleep(period);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
	}

}
