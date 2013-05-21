package com.wander.train.pc.state;

import java.io.IOException;

public class RailSwitchBranchState extends State{

	public RailSwitchBranchState(Context context) {
		super(context);
	}

	@Override
	public void handle() throws IOException {
		context.incDelay();
		
		if (context.isExpired()) {
			context.setState(new TrainStartState(context));
		}
	}

	@Override
	public void doExtra() throws IOException {
		context.resetDelay();
		// 发出换轨道命令
		context.commandSwitchMain(false);
	}

	@Override
	public String toString() {
		return "RailSwitchBranch";
	}
}
