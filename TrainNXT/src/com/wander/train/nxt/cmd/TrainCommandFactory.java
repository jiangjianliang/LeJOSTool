package com.wander.train.nxt.cmd;

import com.wander.train.nxt.cmd.station.StartCommand;
import com.wander.train.nxt.cmd.train.ChangeSpeedCommand;
import com.wander.train.nxt.cmd.train.TrainExitCommand;
import com.wander.train.nxt.cmd.train.TrainStopCommand;
import com.wander.train.nxt.common.Config;
import com.wander.train.nxt.common.ControlData;
import com.wander.train.nxt.train.HeartData;

import lejos.nxt.LCD;

/**
 * 命令工厂类
 * 
 * @author wander
 * 
 */
public class TrainCommandFactory implements CommandFactory {

	private static TrainCommandFactory instance = new TrainCommandFactory();

	private ControlData ca;

	private TrainCommandFactory() {

	}

	public static TrainCommandFactory getInstance(ControlData ca) {
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
	 * @return
	 */
	public Command parseCommand(int cmd) {
		Command result = null;
		switch (cmd) {
		case Command.PROGRAM_START:
			result = StartCommand.getInstance(ca);
			break;
		case Command.EXIT:
			result = TrainExitCommand.getInstance(ca);
			break;
		case Command.TRAIN_STOP:
			result = TrainStopCommand.getInstance();
			break;
		default:
			if (cmd == Config.HEART_BEAT) {
				HeartData.setBeatFlag(true);
				result = NullCommand.getInstance();
			} else {
				boolean dir = cmd > 0; // true : forward; false : backward
				cmd = Math.abs(cmd);
				int speed = cmd % Command.SPEED_MARK % Command.TrainSpeedCount;
				int newSpeed;
				if (dir) {
					newSpeed = speed;
				} else {
					newSpeed = -speed;
				}
				LCD.drawInt(newSpeed, 0, 5);
				result = ChangeSpeedCommand.getInstance(newSpeed);
			}

		}
		return result;
	}
}
