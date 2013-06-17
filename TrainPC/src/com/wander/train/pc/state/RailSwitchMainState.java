package com.wander.train.pc.state;

public class RailSwitchMainState extends State {

	public RailSwitchMainState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		boolean colorResult = context.updateColor();
		context.incDelay();
		//TODO 参照TrainLeaveState
		if (colorResult == false && context.isExpired()) {
			// 转向InProgress
			context.setState(new InProgressState(context));
		}
		else if(colorResult && context.isExpired()){
			context.enterCol();
			context.exitCol();
			context.setState(new TrainEnterState(context));
		}
		else if(colorResult){
			context.enterCol();
		}
	}

	@Override
	public void doExtra() {
		context.resetDelay(Context.RailMainDelay);
		// 切换轨道
		context.commandSwitchMain(true);
	}

	@Override
	public String toString() {
		return "RailSwitchMain";
	}
}
