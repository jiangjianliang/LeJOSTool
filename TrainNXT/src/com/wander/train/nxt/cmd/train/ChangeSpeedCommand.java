package com.wander.train.nxt.cmd.train;

import com.wander.train.nxt.cmd.Command;

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
		
		motor.setSpeed(Math.abs(cmd) * 100);
		if(cmd > 0){
			motor.forward();			
		}
		else{
			motor.backward();
		}
		return true;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

}

