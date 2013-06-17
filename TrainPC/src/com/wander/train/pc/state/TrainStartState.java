package com.wander.train.pc.state;


public class TrainStartState extends State {

	public TrainStartState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		if(context.isInCol() == false){
			
		boolean colorResult = context.updateColor();
		int distanceResult = context.updateDistance();
		if (colorResult == false && distanceResult == 2) {
			context.setState(new TrainLeaveState(context));
		}
		else if(colorResult && distanceResult == 2){
			context.enterCol();
			context.setState(new TrainLeaveState(context));
		}
		else if(colorResult){
			context.enterCol();
		}
		}
		else {
			int distanceResult = context.updateDistance();
			if (distanceResult == 2) {
				context.setState(new TrainLeaveState(context));
			}
		}
	}

	@Override
	public void doExtra() {
		// 发出列车出发命令
		if(context.isSwitch()){
			context.commandForward(2);
		}
		else{
			context.commandBackward(1);
		}
	}

	@Override
	public String toString() {
		return "TrainStart";
	}
	
}
