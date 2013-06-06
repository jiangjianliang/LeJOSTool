package com.wander.train.pc.state;

import java.io.IOException;


public class InProgressState extends State {
	
	public InProgressState(Context context) {
		super(context);
	}

	@Override
	public void handle() throws IOException {
		boolean result = context.updateColor();
		if(result){
			context.setState(new TrainEnterState(context));
		}
		else{
			int disType = context.updateDistance();
			if(disType == 1){
				//TODO 详细考虑
			}
		}
		
	}

	@Override
	public void doExtra() throws IOException {
		//TODO 不同的速度
		if(context.isSwitch()){
			context.commandSlowDown(2);
		}
		else{
			context.commandSlowDown(1);
		}
	}

	@Override
	public String toString() {
		return "InProgress";
	}
}
