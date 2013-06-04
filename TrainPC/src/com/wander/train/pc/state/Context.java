package com.wander.train.pc.state;

import java.io.IOException;

public interface Context {
	
	public void setState(State state) throws IOException;
	
	public void resetDelay();
	public void incDelay();
	public boolean isExpired();
	
	/**
	 * 更新距离
	 * @return
	 */
	public int updateDistance();
	
	/**
	 * 更新是哪一辆火车
	 */
	public void updateWhich();
	
	/**
	 * 是否含有换轨控制
	 * 
	 * @return boolean
	 */
	public boolean isSwitch();
	
	/**
	 * 控制类命令
	 */
	/**
	 * 向前运行
	 * @param dest
	 */
	public void commandForward(int dest);
	/**
	 * 向后运行
	 * @param dest
	 */
	public void commandBackward(int dest);
	/**
	 * 停止火车
	 */
	public void commandStop();
	/**
	 * 变换轨道
	 * @param flag
	 */
	public void commandSwitchMain(boolean flag);
	
	
}
