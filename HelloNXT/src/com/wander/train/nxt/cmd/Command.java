package com.wander.train.nxt.cmd;

import java.io.DataOutputStream;
import java.io.IOException;

import com.wander.train.nxt.IrLinkExt;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

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
	final static int TRAIN_STOP_A = 1;
	final static int TRAIN_STOP_B = -1;

	final static int PROGRAM_START = 0;
	// 换轨道
	final static int SWITCH_MAIN = 2;
	final static int SWITCH_BRANCH = -2;

	// 要求发送距离
	/**
	 * @deprecated
	 */
	final static int UPDATE_DISTANCE = 100;

	// 速度标记
	final static int SPEED_MARK = 1000;
	// 火车标记
	final static int TRAIN_MARK_A = 10000;

	// 控制类的常量
	final static int TRAIN_PORT = IrLinkExt.PF_SINGLE_MODE_BLUE_PORT;

	/**
	 * 执行命令
	 * 
	 * @return 执行结束是否成功，false时需要退出程序
	 */
	public boolean execute();

}


