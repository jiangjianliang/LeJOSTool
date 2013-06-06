package com.wander.train.nxt.train;

import com.wander.train.nxt.common.ControlData;

import lejos.nxt.LCD;

public class HeartDetector extends Thread {
	private ControlData ca;

	public HeartDetector(ControlData ca) {
		this.ca = ca;
	}

	@Override
	public void run() {
		while (ca.isKeepOn()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}

			if (ca.isBeatFlag()) {
				ca.setBeatFlag(false);
			} else {
				LCD.drawString("heart fail", 0, 6);
			}
		}

	}

}
