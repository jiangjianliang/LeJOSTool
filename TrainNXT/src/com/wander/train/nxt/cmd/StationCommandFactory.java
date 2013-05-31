package com.wander.train.nxt.cmd;

import com.wander.train.nxt.cmd.station.*;
import com.wander.train.nxt.common.ControlData;

/**
 * 命令工厂类
 * 
 * @author wander
 * 
 */
public class StationCommandFactory implements CommandFactory{

	private static StationCommandFactory instance = new StationCommandFactory();

	private ControlData ca;

	private StationCommandFactory() {

	}

	public static StationCommandFactory getInstance(
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
		case Command.SWITCH_MAIN:
			result = SwitchCommand.getInstance(true);
			break;
		case Command.SWITCH_BRANCH:
			result = SwitchCommand.getInstance(false);
			break;
		default:
			result = NullCommand.getInstance();
		}
		return result;
	}
}
