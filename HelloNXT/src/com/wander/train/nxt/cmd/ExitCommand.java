package com.wander.train.nxt.cmd;


/**
 * 退出程序的命令
 * 
 * @author wander
 * 
 */
class ExitCommand implements Command {
	private static ExitCommand instance = new ExitCommand();

	private ExitCommand() {
	}

	public static ExitCommand getInstance() {
		return instance;
	}

	@Override
	public boolean execute() {
		return false;
	}
}
