package com.wander.train.nxt.cmd;

/**
 * 空命令
 * @author wander
 *
 */
public class NullCommand implements Command {
	private static NullCommand instance = new NullCommand();
	
	private NullCommand(){
		
	}
	
	public static NullCommand getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean execute() {
		//do nothing
		return true;
	}

}
