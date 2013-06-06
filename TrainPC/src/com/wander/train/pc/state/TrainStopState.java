package com.wander.train.pc.state;


public class TrainStopState extends State {

	public TrainStopState(Context context) {
		super(context);
	}

	@Override
	public void handle() {

		if (context.isSwitch()) {
			// 转向ForwardSwitchRail
			context.setState(new RailSwitchBranchState(context));
		} else {
			// 转向TrainStart
			context.setState(new TrainStartState(context));
		}
	}

	@Override
	public void doExtra() {
		// 发出列车停止命令
		context.commandStop();
	}

	@Override
	public String toString() {
		return "TrainStop";
	}
}
