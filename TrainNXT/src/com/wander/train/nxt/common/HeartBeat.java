package com.wander.train.nxt.common;

public class HeartBeat extends Thread {
	private static HeartBeat instance = null;
	private BluetoothWriter writer;
	private ControlData ca;
	private int period = 500;

	private HeartBeat(BluetoothWriter writer, ControlData ca, int period) {
		this.writer = writer;
		this.ca = ca;
		this.period = period;
	}

	public static HeartBeat getInstance(BluetoothWriter writer, ControlData ca,
			int period) {
		if (instance == null) {
			instance = new HeartBeat(writer, ca, period);
		}
		return instance;
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			writer.synWriteIntAndFlush(Config.HEART_BEAT, "write heartbeat");
			try {
				sleep(period);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
	}

}
