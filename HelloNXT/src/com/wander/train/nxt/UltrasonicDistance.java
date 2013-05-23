package com.wander.train.nxt;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;

public class UltrasonicDistance extends Thread {

	private UltrasonicSensor sonic;
	private DataOutputStream out;
	private ControlData ca;

	public UltrasonicDistance(UltrasonicSensor sonic, DataOutputStream out,
			ControlData ca) {
		this.sonic = sonic;
		this.out = out;
		this.ca = ca;
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			if (ca.isStart()) {

				int dis = sonic.getDistance();
				LCD.drawString(String.valueOf(dis), 10, 1);
				try {
					out.writeInt(dis);
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
	}
}
