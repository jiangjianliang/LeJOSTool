package com.wander.train.nxt.station;

import com.wander.train.nxt.common.ControlData;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.robotics.Color;

public class ColorDetector extends Thread {

	private ColorSensor color;
	private ControlData ca;

	public ColorDetector(ColorSensor color, ControlData ca) {
		this.color = color;
		this.ca = ca;
		color.setFloodlight(Color.NONE);
		//color.setFloodlight(Color.WHITE);
		//color.setFloodlight(true);
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			if (ca.isStart()) {
				int colorIndex = color.getColor().getColor();
				LCD.drawInt(colorIndex, 2, 9, 2);
				ca.setColor(colorIndex);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}

		LCD.drawString("exit ColorDetector", 0, 3);
	}
}
