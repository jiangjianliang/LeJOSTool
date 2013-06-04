package com.wander.train.pc;

/**
 * 命令接口
 * 
 * @author wander
 * 
 */
public interface Command {

	// 程序退出
	final static int EXIT = -100;
	// 火车停止
	final static int TRAIN_STOP = 1;

	final static int PROGRAM_START = 0;
	
	// 换轨道
	final static int SWITCH_MAIN = 2;
	final static int SWITCH_BRANCH = -2;

	// 要求发送距离
	
	// 速度标记
	final static int SPEED_MARK = 1000;
	// 火车标记

	/**
	 * 执行命令
	 * 
	 * @return 执行结束是否成功，false时需要退出程序
	 */
	public boolean execute();

}


