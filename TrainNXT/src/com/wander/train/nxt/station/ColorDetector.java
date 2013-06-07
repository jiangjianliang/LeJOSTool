package com.wander.train.nxt.station;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.robotics.Color;

public class ColorDetector extends Thread {

	private ColorSensor color;
	public ColorDetector(ColorSensor color) {
		this.color = color;
		setDaemon(true);
		color.setFloodlight(Color.NONE);
		// color.setFloodlight(Color.WHITE);
		// color.setFloodlight(true);
	}

	@Override
	public void run() {
		while (true) {
			int colorIndex = color.getColor().getColor();
			LCD.drawInt(colorIndex, 2, 9, 2);
			SensorData.setColor(colorIndex);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}

		// LCD.drawString("exit ColorDetector", 0, 3);
	}
}
