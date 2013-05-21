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
	 * @throws IOException
	 */
	public void commandForward(int dest)  throws IOException;
	/**
	 * 向后运行
	 * @param dest
	 * @throws IOException
	 */
	public void commandBackward(int dest)  throws IOException;
	/**
	 * 停止火车
	 * @throws IOException
	 */
	public void commandStop() throws IOException;
	/**
	 * 变换轨道
	 * @param flag
	 * @throws IOException
	 */
	public void commandSwitchMain(boolean flag) throws IOException;
	
	
}
