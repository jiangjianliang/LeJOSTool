package com.wander.train.nxt.common;

public class ControlData {
	/**
	 * 程序是否继续
	 */
	private boolean keepOn = true;
	/**
	 * 程序是否开始
	 * Executor -> ReportDistance
	 */
	private boolean start = false;
	
	/**
	 * 距离信息
	 */
	private int distance = 255;
	/**
	 * 颜色信息
	 */
	private int color = 7;
	
	public synchronized void setKeepOn(boolean keepOn){
		this.keepOn = keepOn;
	}

	public synchronized boolean isKeepOn() {
		return keepOn;
	}

	public synchronized boolean isStart() {
		return start;
	}

	public synchronized void setStart(boolean start) {
		this.start = start;
	}

	public synchronized int getDistance() {
		return distance;
	}

	public synchronized void setDistance(int distance) {
		this.distance = distance;
	}

	public synchronized int getColor() {
		return color;
	}

	public synchronized void setColor(int color) {
		this.color = color;
	}
	
}
