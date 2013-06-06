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
	public final static int DISTANCE_TYPE = 1;
	
	public final static I2CPort IrLinkPort = SensorPort.S1;
	@Deprecated
	public final static ADSensorPort TouchPort = SensorPort.S3;
	public final static SensorPort ColorPort = SensorPort.S3;
	public final static I2CPort UltrasonicPort = SensorPort.S4;
	//睡眠周期
	public final static int CommandExecutorPeriod = 100;
	public final static int SensorReporterPeriod  = 200;
	public final static int HeartBeatPeriod = 500;
	
	//心跳标记
	public final static int HEARTBEAT = 99999;
	
}
