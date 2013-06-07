package com.wander.train.nxt.station;

import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;

public class UltrasonicDetector extends Thread {

	private UltrasonicSensor sonic;
	public UltrasonicDetector(UltrasonicSensor sonic) {
		this.sonic = sonic;
		setDaemon(true);
	}

	@Override
	public void run() {
		while (true) {
				int dis = sonic.getDistance();
				LCD.drawInt(dis, 9, 1);
				SensorData.setDistance(dis);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
		//LCD.drawString("exit UltrasonicDector", 0, 2);
	}
}
