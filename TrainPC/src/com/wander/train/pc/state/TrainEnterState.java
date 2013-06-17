package com.wander.train.pc.state;

public class TrainEnterState extends State {

	public TrainEnterState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		if (context.isInCol() == false) {
			boolean colorResult = context.updateColor();
			int distanceResult = context.updateDistance();
			if (colorResult == false && distanceResult == 1) {
				context.setState(new TrainStopState(context));
			} else if (colorResult && distanceResult == 1) {
				context.enterCol();
				context.setState(new TrainStopState(context));
			} else if (colorResult) {
				context.enterCol();
			}
		}
		else{
			int distanceResult = context.updateDistance();
			if (distanceResult == 1) {
				context.setState(new TrainStopState(context));
			}
		}
	}

	@Override
	public void doExtra() {
		context.enterCS();
		// 减速
		if (context.isSwitch()) {
			context.commandSlowDown(3);
		} else {
			context.commandSlowDown(2);
		}
	}

	@Override
	public String toString() {
		return "TrainEnter";
	}

}
