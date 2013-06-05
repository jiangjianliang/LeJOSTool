package com.wander.train.pc.state;

import java.io.IOException;


public class InProgressState extends State {
	
	public InProgressState(Context context) {
		super(context);
	}

	@Override
	public void handle() throws IOException {
		boolean result = context.updateColor();
		//TODO result的值需要重新规划一下
		if(result ){
			context.setState(new TrainEnterState(context));
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
