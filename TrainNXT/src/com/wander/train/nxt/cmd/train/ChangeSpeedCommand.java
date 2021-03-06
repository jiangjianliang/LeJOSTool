package com.wander.train.nxt.cmd.train;

import lejos.nxt.LCD;



/**
 * 更改速度的命令
 * 
 * @author wander
 * 
 */
public class ChangeSpeedCommand extends TrainCommand {
	private static ChangeSpeedCommand instance = null;

	private int cmd;

	private ChangeSpeedCommand(int cmd) {
		this.cmd = cmd;
	}

	public static ChangeSpeedCommand getInstance(int cmd){
		if (instance == null) {
			instance = new ChangeSpeedCommand(cmd);
		}
		instance.setCmd(cmd);
		return instance;
	}

	@Override
	public boolean execute() {
		//LCD.drawString(channel + "] speed ", 0, 7);
		LCD.drawInt(cmd, 2, 9, 7);
		//TODO 速度区继续细化
		if(cmd > 0 ){
			motor.setPower(cmd * 10);
			motor.forward();						
		}
		else{
			motor.setPower((-cmd) * 10);
			motor.backward();		
		}
		
		return true;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

}

