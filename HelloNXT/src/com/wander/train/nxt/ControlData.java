package com.wander.train.nxt;

public class ControlData {
	/**
	 * 是否持续进行更新"距离"
	 */
	private boolean keepOn = true;
	/**
	 * 程序是否退出
	 * Executor -> main
	 */
	private boolean exit = false;
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

	public synchronized boolean isExit() {
		return exit;
	}

	public synchronized void setExit(boolean exit) {
		this.exit = exit;
	}

	public synchronized boolean isStart() {
		return start;
	}

	public synchronized void setStart(boolean start) {
		this.start = start;
	}
	
}
