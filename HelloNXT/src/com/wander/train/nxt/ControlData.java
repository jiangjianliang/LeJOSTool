package com.wander.train.nxt;

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
	
}
