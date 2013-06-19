package com.wander.train.nxt.station;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.robotics.Color;

public class ColorDetector extends Thread {
	
	private boolean flag = false;
	
	private ColorSensor color;
	public ColorDetector(ColorSensor color) {
		this.color = color;
		setDaemon(true);
		color.setFloodlight(Color.NONE);
	}

	@Override
	public void run() {
		while (true) {
			int colorIndex = color.getColor().getColor();
			//LCD.drawInt(colorIndex, 2, 9, 2);
			LCD.drawString(colorIndex + "|" + SensorData.getColor(), 6, 2);
			if(colorIndex == 3 || colorIndex == 2){
				SensorData.setColor(colorIndex);
				flag = false;
			}
			else{
				int preColorIndex = SensorData.getColor();
				
				if(flag == false && isValid(preColorIndex)){
					flag = true;
				}
				else if(flag){
					SensorData.setColor(colorIndex);
					flag = false;
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
	}
	
	private boolean isValid(int cIndex){
		return cIndex == 3 || cIndex == 2;
	}
}
