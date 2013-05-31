package com.wander.train.nxt.cmd;

import com.wander.train.nxt.cmd.station.ExitCommand;
import com.wander.train.nxt.cmd.station.StartCommand;
import com.wander.train.nxt.cmd.train.ChangeSpeedCommand;
import com.wander.train.nxt.cmd.train.TrainStopCommand;
import com.wander.train.nxt.common.ControlData;

import lejos.nxt.LCD;

/**
 * 命令工厂类
 * 
 * @author wander
 * 
 */
public class TrainCommandFactory implements CommandFactory{

	private static TrainCommandFactory instance = new TrainCommandFactory();

	private ControlData ca;

	private TrainCommandFactory() {

	}

	public static TrainCommandFactory getInstance(
			ControlData ca) {
		instance.setCa(ca);
		return instance;
	}

	public void setCa(ControlData ca) {
		this.ca = ca;
	}

	/**
	 * 格式化命令
	 * 
	 * @param cmd
	 * @param link
	 * @param sonic
	 * @param out
	 * @return
	 */
	public Command parseCommand(int cmd) {
		Command result = null;
		switch (cmd) {
		case Command.PROGRAM_START:
			result = StartCommand.getInstance(ca);
			break;
		case Command.EXIT:
			result = ExitCommand.getInstance(ca);
			break;
		//TODO 以后修改,只需要一个
		case Command.TRAIN_STOP_A:
			result = TrainStopCommand.getInstance();
			break;
		case Command.TRAIN_STOP_B:
			result = TrainStopCommand.getInstance();
			break;
		default:
			boolean dir = cmd > 0; // true : forward; false : backward
			cmd = Math.abs(cmd);
			int speed = (cmd % Command.TRAIN_MARK_A) % Command.SPEED_MARK;
			int newSpeed;
			if (dir) {
				newSpeed = speed;
			} else {
				newSpeed = 16 - speed;
			}
			LCD.drawInt(newSpeed, 0, 5);
			if (cmd > Command.TRAIN_MARK_A) {
				result = ChangeSpeedCommand.getInstance(newSpeed);
			} else {
				result = ChangeSpeedCommand.getInstance(newSpeed);
			}
		}
		return result;
	}
}
