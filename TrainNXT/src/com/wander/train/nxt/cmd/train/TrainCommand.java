package com.wander.train.nxt.cmd.train;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import com.wander.train.nxt.cmd.Command;

public abstract class TrainCommand implements Command {
	// 预先设定使用A端口
	protected static NXTMotor motor = new NXTMotor(MotorPort.A);

	// 速度对照表
	public static int[] speedArr = new int[TrainSpeedCount];
	static {
		speedArr[0] = 0;
		speedArr[1] = 10;
		speedArr[2] = 30;
		speedArr[3] = 50;
		speedArr[4] = 70;
		speedArr[5] = 75;
		speedArr[6] = 80;
		speedArr[7] = 85;
		speedArr[8] = 90;
		speedArr[9] = 95;
		speedArr[10] = 100;
	}
}
