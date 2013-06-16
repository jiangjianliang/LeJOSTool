package com.wander.train.pc.state;


public class InProgressState extends State {
	
	public InProgressState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		boolean colorResult = context.updateColor();
		int distanceResult = context.updateDistance();
		if(colorResult && distanceResult == 0){
			context.setState(new TrainEnterState(context));
		}
		else if(colorResult && distanceResult == 1){
			context.setState(new SensorErrorState(context));
		}
	}

	@Override
	public void doExtra() {
		//TODO 不同的速度
		if(context.isSwitch()){
			//context.commandSlowDown(2);
			context.commandSpeedUp(2);
		}
		else{
			context.commandSpeedUp(3);
			//context.commandSlowDown(1);
		}
	}

	@Override
	public String toString() {
		return "InProgress";
	}
}
