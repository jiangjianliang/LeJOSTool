package com.wander.train.pc.state;

import java.io.IOException;


public class TrainEnterState extends State {

	public TrainEnterState(Context context) {
		super(context);
	}

	@Override
	public void handle() throws IOException {
		int result = context.updateDistance();
		if(result == 1){
			context.setState(new TrainStopState(context));			
		}
	}

	@Override
	public void doExtra() throws IOException {
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
