package com.wander.train.nxt.station;

import com.wander.train.nxt.common.BluetoothWriter;
import com.wander.train.nxt.common.ControlData;

import lejos.nxt.LCD;

public class SensorReporter extends Thread {

	private BluetoothWriter writer;
	private ControlData ca;
	private int period = 200;

	public SensorReporter(BluetoothWriter writer,
			ControlData ca, int period) {
		this.writer = writer;
		this.ca = ca;
		this.period = period;
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			if (ca.isStart()) {
				int result = SensorData.getDistance()*100 + SensorData.getColor();
				writer.synWriteIntAndFlush(result, "write SensorInfo");
			}
			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
		LCD.drawString("exit SensorReporter", 0, 3);
	}
}
