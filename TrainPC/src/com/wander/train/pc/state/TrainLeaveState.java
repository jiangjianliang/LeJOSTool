package com.wander.train.pc.state;


public class TrainLeaveState extends State {

	public TrainLeaveState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		context.incDelay();
		if (context.isSwitch()) {
			if(context.isExpired()){
				// 转向BackwardSwitchRail
				context.setState(new RailSwitchMainState(context));
			}
		} else {
			if(context.isExpired()){
				// 转向InProgress
				context.setState(new InProgressState(context));
			}
		}
	}

	@Override
	public void doExtra() {
		// do nothing
		context.resetDelay(Context.TrainLeaveDelay);
	}

	@Override
	public String toString() {
		return "TrainLeave";
	}
	
}
