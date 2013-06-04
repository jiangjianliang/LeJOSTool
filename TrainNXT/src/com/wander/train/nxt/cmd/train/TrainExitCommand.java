package com.wander.train.nxt.cmd.train;

import com.wander.train.nxt.cmd.Command;
import com.wander.train.nxt.common.ControlData;


/**
 * 退出程序的命令
 * 
 * @author wander
 * 
 */
public class TrainExitCommand extends TrainCommand {
	private static TrainExitCommand instance = new TrainExitCommand();
	private ControlData ca;
	
	private TrainExitCommand() {
	}

	public static TrainExitCommand getInstance(ControlData ca) {
		instance.setCa(ca);
		return instance;
	}

	@Override
	public boolean execute() {
		motor.setPower(0);
		ca.setKeepOn(false);
		return false;
	}

	public void setCa(ControlData ca) {
		this.ca = ca;
	}
	
	
}
