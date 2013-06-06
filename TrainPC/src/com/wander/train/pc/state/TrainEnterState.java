package com.wander.train.pc.state;


public class TrainEnterState extends State {

	public TrainEnterState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		int result = context.updateDistance();
		if(result == 1){
			context.setState(new TrainStopState(context));			
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
