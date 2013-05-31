package com.wander.train.pc.state;

import java.io.IOException;


public class TrainLeaveState extends State {

	public TrainLeaveState(Context context) {
		super(context);
	}

	@Override
	public void handle() throws IOException {

		if (context.isSwitch()) {
			context.incDelay();
			if(context.isExpired()){
				// 转向BackwardSwitchRail
				context.setState(new RailSwitchMainState(context));
			}
		} else {
			// 转向InProgress
			context.setState(new InProgressState(context));
		}
	}

	@Override
	public void doExtra() throws IOException {
		// do nothing
		context.resetDelay();
	}

	@Override
	public String toString() {
		return "TrainLeave";
	}
	
}
