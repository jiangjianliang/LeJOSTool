package com.wander.train.nxt.common;

import lejos.nxt.ADSensorPort;
import lejos.nxt.I2CPort;
import lejos.nxt.SensorPort;

public interface Config {
	/**
	 * 距离的类型
	 * 0 TouchSensor
	 * 1 UltrasonicSensor
	 */
	public final static int DISTANCE_TYPE = 0;
	
	public final static I2CPort IrLinkPort = SensorPort.S1;
	public final static ADSensorPort TouchPort = SensorPort.S3;
	public final static I2CPort UltrasonicPort = SensorPort.S4;
}
