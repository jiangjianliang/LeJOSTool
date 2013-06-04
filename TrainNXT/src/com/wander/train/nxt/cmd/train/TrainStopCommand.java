package com.wander.train.nxt.cmd.train;

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
		//motor.stop();
		//motor.flt();
		motor.setPower(0);
		return true;
	}
}