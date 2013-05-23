package com.wander.train.nxt;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.TouchSensor;

public class TouchDistance extends Thread {

	private TouchSensor touch;
	private DataOutputStream out;
	private ControlData ca;

	public TouchDistance(TouchSensor touch, DataOutputStream out,
			ControlData ca) {
		this.touch = touch;
		this.out = out;
		this.ca = ca;
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			if (ca.isStart()) {

				boolean isPressed = touch.isPressed();

				LCD.drawString(String.valueOf(isPressed), 10, 1);
				try {
					if (isPressed) {
						out.writeInt(1);
						out.flush();
					} else {
						out.writeInt(0);
						out.flush();
					}
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
