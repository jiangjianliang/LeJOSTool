package com.wander.train.nxt.cmd;

public interface CommandFactory {
	public Command parseCommand(int cmd);
}
