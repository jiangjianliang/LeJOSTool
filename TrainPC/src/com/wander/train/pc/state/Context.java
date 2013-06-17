package com.wander.train.pc.state;

public interface Context {
	
	//延迟相关
	public static int TrainStopDelay = 18;
	public static int RailBranchDelay = 18;
	public static int RailMainDelay = 18;
	public static int TrainLeaveDelay = 15;
	public void resetDelay(int initDelay);	
	public void incDelay();
	public boolean isExpired();
	
	public void setState(State state);
	//遍历火车
	public void itrInit();
	public boolean itrNext();
	
	//临界区
	public void enterCS();
	public void exitCS();
	public void enterCol();
	public void exitCol();
	public boolean isInCol();
	
	/**
	 * 更新距离
	 * @return
	 */
	public int updateDistance();
	
	/**
	 * 更新颜色
	 * @return boolean
	 */
	public boolean updateColor();
	
	/**
	 * 是否含有换轨控制
	 * 
	 * @return boolean
	 */
	public boolean isSwitch();
	
	// 控制类命令
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
	 * 减速
	 * @param delta
	 */
	public void commandSlowDown(int delta);
	/**
	 * 加速
	 * @param delta
	 */
	public void commandSpeedUp(int delta);
	
	/**
	 * 停止火车
	 */
	public void commandStop();
	
	public void commandColForward(int dest);
	public void commandColBackward(int dest);
	public void commandColStop();
	
	
	
	/**
	 * 变换轨道
	 * @param flag
	 * true 换轨到主干道
	 * false 换轨道到分支道
	 */
	public void commandSwitchMain(boolean flag);
	
	
}
