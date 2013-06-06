package com.wander.train.nxt.station;

import com.wander.train.nxt.common.ControlData;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;

public class ColorDetector extends Thread {

	private ColorSensor color;
	private ControlData ca;

	public ColorDetector(ColorSensor color, ControlData ca) {
		this.color = color;
		color.setFloodlight(ColorSensor.WHITE);
		this.ca = ca;
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			if (ca.isStart()) {
				int colorIndex = color.getColor().getColor();
				ca.setColor(colorIndex);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}

		LCD.drawString("exit ColorDetector", 0, 0);
	}
}
