package com.wander.train.nxt.cmd.station;

import com.wander.train.nxt.cmd.Command;
import com.wander.train.nxt.common.ControlData;


/**
 * 退出程序的命令
 * 
 * @author wander
 * 
 */
public class ExitCommand implements Command {
	private static ExitCommand instance = new ExitCommand();
	private ControlData ca;
	
	private ExitCommand() {
	}

	public static ExitCommand getInstance(ControlData ca) {
		instance.setCa(ca);
		return instance;
	}

	@Override
	public boolean execute() {
		ca.setKeepOn(false);
		return false;
	}

	public void setCa(ControlData ca) {
		this.ca = ca;
	}
	
	
}
