package com.wander.train.pc.state;

public class RailSwitchBranchState extends State{

	public RailSwitchBranchState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		context.incDelay();
		
		if (context.isExpired()) {
			context.setState(new TrainStartState(context));
		}
	}

	@Override
	public void doExtra() {
		context.resetDelay();
		// 发出换轨道命令
		context.commandSwitchMain(false);
	}

	@Override
	public String toString() {
		return "RailSwitchBranch";
	}
}
