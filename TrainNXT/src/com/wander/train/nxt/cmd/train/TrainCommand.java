package com.wander.train.nxt.cmd.train;

import lejos.nxt.Motor;
import lejos.robotics.RegulatedMotor;

import com.wander.train.nxt.cmd.Command;

public abstract class TrainCommand implements Command{
	//TODO 先这么使用
	protected RegulatedMotor motor = Motor.A;
}
