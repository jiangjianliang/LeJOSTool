package com.wander.train.pc.state;


public class TrainEnterState extends State {

	public TrainEnterState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		boolean colorResult = context.updateColor();
		int distanceResult = context.updateDistance();
		if(colorResult == false && distanceResult == 1){
			context.setState(new TrainStopState(context));			
		}
		else if(colorResult){
			//context.setState(new ColTrainStopState());
		}
	}

	@Override
	public void doExtra() {
		//减速
		if(context.isSwitch()){
			context.commandSlowDown(3);						
		}
		else{
			context.commandSlowDown(2);			
		}
	}

	@Override
	public String toString() {
		return "TrainEnter";
	}
	
	
}
