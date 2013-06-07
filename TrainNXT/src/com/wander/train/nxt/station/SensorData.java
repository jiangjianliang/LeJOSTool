package com.wander.train.nxt.station;
/**
 * 记录传感器数据
 * @author wander
 *
 */
public class SensorData {
	/**
	 * 距离信息
	 */
	private static int distance = 255;
	/**
	 * 颜色信息
	 */
	private static int color = 7;
	
	public static synchronized int getDistance() {
		return distance;
	}

	public static synchronized void setDistance(int distance) {
		SensorData.distance = distance;
	}

	public static synchronized int getColor() {
		return color;
	}

	public static synchronized void setColor(int color) {
		SensorData.color = color;
	}
}
