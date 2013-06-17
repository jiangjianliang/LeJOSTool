package com.wander.train.pc.state;

public class TrainStopState extends State {

	public TrainStopState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		if (context.isInCol() == false) {
			boolean colorResult = context.updateColor();
			// int distanceResult = context.updateDistance();
			if (context.isSwitch()) {
				if (colorResult == false) {
					// 转向ForwardSwitchRail
					context.setState(new RailSwitchBranchState(context));
				} else {
					context.enterCol();
					context.setState(new RailSwitchBranchState(context));
				}
			} else {
				context.incDelay();
				if (colorResult == false && context.isExpired()) {
					// 转向TrainStart
					context.setState(new TrainStartState(context));
				} else if (colorResult && context.isExpired()) {
					context.enterCol();
					context.setState(new TrainStartState(context));
				} else if (colorResult) {
					context.enterCol();
				}
			}
		}
		else{
			if (context.isSwitch()) {
					context.setState(new RailSwitchBranchState(context));
			} else {
				context.incDelay();
				if (context.isExpired()) {
					// 转向TrainStart
					context.setState(new TrainStartState(context));
				}
			}
		}
	}

	@Override
	public void doExtra() {
		context.resetDelay(Context.TrainStopDelay);
		// 发出列车停止命令
		context.commandStop();
	}

	@Override
	public String toString() {
		return "TrainStop";
	}
}
