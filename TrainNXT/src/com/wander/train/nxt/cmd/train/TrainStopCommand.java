package com.wander.train.nxt.cmd.train;

import com.wander.train.nxt.cmd.Command;

/**
 * 停止火车的命令
 * 
 * @author wander
 * 
 */
public class TrainStopCommand extends TrainCommand{
	private static TrainStopCommand instance = new TrainStopCommand();


	private TrainStopCommand() {
		
	}

	public static TrainStopCommand getInstance() {
		return instance;
	}

	@Override
	public boolean execute() {
		//停止马达
		motor.stop();
		return true;
	}
}