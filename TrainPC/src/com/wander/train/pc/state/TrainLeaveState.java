package com.wander.train.pc.state;


public class TrainLeaveState extends State {

	public TrainLeaveState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		
		boolean colorResult = context.updateColor();
		context.incDelay();
		if (context.isSwitch()) {
			if(colorResult == false && context.isExpired()){
				// 转向BackwardSwitchRail
				context.setState(new RailSwitchMainState(context));
			}
			else if(colorResult && context.isExpired()){
				context.enterCol();
				context.setState(new RailSwitchMainState(context));
			}
			else if(colorResult ){
				context.enterCol();
			}
		} else {
			if(colorResult == false && context.isExpired()){
				// 转向InProgress
				//TODO 可能已经是在enterCol了
				context.setState(new InProgressState(context));
			}
			else if(colorResult && context.isExpired()){
				//TODO 这里需要部分状态下转向TrainEnter状态
				context.enterCol();
				context.exitCol();
				context.setState(new TrainEnterState(context));
			}
			else if(colorResult ){
				context.enterCol();
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
