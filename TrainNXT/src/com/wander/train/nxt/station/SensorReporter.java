package com.wander.train.nxt.station;

import java.io.DataOutputStream;
import java.io.IOException;

import com.wander.train.nxt.common.ControlData;

import lejos.nxt.LCD;

public class SensorReporter extends Thread {

	private DataOutputStream out;
	private ControlData ca;

	public SensorReporter(DataOutputStream out,
			ControlData ca) {
		this.out = out;
		this.ca = ca;
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			if (ca.isStart()) {

				int result = ca.getDistance()*100 + ca.getColor();
				try {
					out.writeInt(result);
					out.flush();

				} catch (IOException e1) {
					LCD.drawString("ERROR WRITE!", 0, 3);
					// e1.printStackTrace();
				}
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
		LCD.drawString("exit SensorReporter", 0, 3);
	}
}
