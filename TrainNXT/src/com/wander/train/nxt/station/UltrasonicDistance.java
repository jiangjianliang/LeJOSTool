package com.wander.train.nxt.station;

import java.io.DataOutputStream;
import java.io.IOException;

import com.wander.train.nxt.common.ControlData;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;

public class UltrasonicDistance extends Thread {

	private UltrasonicSensor sonic;
	private ColorSensor color;
	private DataOutputStream out;
	private ControlData ca;

	public UltrasonicDistance(UltrasonicSensor sonic, ColorSensor color, DataOutputStream out,
			ControlData ca) {
		this.sonic = sonic;
		this.color= color;
		this.out = out;
		this.ca = ca;
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			if (ca.isStart()) {

				int dis = sonic.getDistance();
				//TODO 正确使用ColorSensor
				color.setFloodlight(ColorSensor.WHITE);
				try {
					sleep(10);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
				int colorIndex = color.getColor().getColor();
				LCD.drawInt(colorIndex, 0, 7);
				int result = dis*100 + colorIndex;
				LCD.drawString(String.valueOf(dis), 10, 1);
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
		LCD.drawString("exit UltrasonicDistance", 0, 0);
	}
}
