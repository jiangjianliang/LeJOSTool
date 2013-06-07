package com.wander.train.nxt.station;

import com.wander.train.nxt.common.ControlData;

import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;

public class UltrasonicDetector extends Thread {

	private UltrasonicSensor sonic;
	private ControlData ca;

	public UltrasonicDetector(UltrasonicSensor sonic, ControlData ca) {
		this.sonic = sonic;
		this.ca = ca;
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			if (ca.isStart()) {
				int dis = sonic.getDistance();
				LCD.drawInt(dis, 9, 1);
				ca.setDistance(dis);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
		LCD.drawString("exit UltrasonicDector", 0, 2);
	}
}
