package com.wander.train.pc.state;

public class RailSwitchBranchState extends State {

	public RailSwitchBranchState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		if (context.isInCol() == false) {
			boolean colorResult = context.updateColor();
			context.incDelay();
			if (colorResult == false && context.isExpired()) {
				context.setState(new TrainStartState(context));
			} else if (colorResult && context.isExpired()) {
				context.enterCol();
				context.setState(new TrainStartState(context));
			} else if (colorResult) {
				context.enterCol();
			}
		}
		else {
			context.incDelay();
			if (context.isExpired()) {
				context.setState(new TrainStartState(context));
			}
		}
	}

	@Override
	public void doExtra() {
		context.resetDelay(Context.RailBranchDelay);
		// 发出换轨道命令
		context.commandSwitchMain(false);
	}

	@Override
	public String toString() {
		return "RailSwitchBranch";
	}
}
