package com.wander.train.nxt.cmd;

import com.wander.train.nxt.ControlData;
import com.wander.train.nxt.IrLinkExt;

import lejos.nxt.LCD;

/**
 * 命令工厂类
 * 
 * @author wander
 * 
 */
public class CommandFactory {

	private static CommandFactory instance = new CommandFactory();

	private IrLinkExt link;
	private ControlData ca;

	private CommandFactory() {

	}

	public static CommandFactory getInstance(IrLinkExt link,
			ControlData ca) {
		instance.setLink(link);
		instance.setCa(ca);
		return instance;
	}
	
	public void setLink(IrLinkExt link) {
		this.link = link;
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
		case Command.TRAIN_STOP_A:
			result = TrainStopCommand.getInstance(link, 1);
			break;
		case Command.TRAIN_STOP_B:
			result = TrainStopCommand.getInstance(link, 2);
			break;
		case Command.SWITCH_MAIN:
			result = SwitchCommand.getInstance(true);
			break;
		case Command.SWITCH_BRANCH:
			result = SwitchCommand.getInstance(false);
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
				result = ChangeSpeedCommand.getInstance(newSpeed, link, 1);
			} else {
				result = ChangeSpeedCommand.getInstance(newSpeed, link, 2);
			}
		}
		return result;
	}
}
