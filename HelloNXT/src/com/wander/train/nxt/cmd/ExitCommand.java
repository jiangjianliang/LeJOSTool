package com.wander.train.nxt.cmd;

import com.wander.train.nxt.ControlData;


/**
 * 退出程序的命令
 * 
 * @author wander
 * 
 */
class ExitCommand implements Command {
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
		return false;
	}

	public void setCa(ControlData ca) {
		this.ca = ca;
	}
	
	
}
