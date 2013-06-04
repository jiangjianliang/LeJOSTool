package com.wander.train.nxt.cmd.train;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import com.wander.train.nxt.cmd.Command;

public abstract class TrainCommand implements Command{
	//预先设定使用A端口
	protected NXTMotor motor = new NXTMotor(MotorPort.A);
	//protected 速度对照表
}
