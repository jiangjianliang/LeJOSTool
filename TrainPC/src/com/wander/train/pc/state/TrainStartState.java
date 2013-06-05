package com.wander.train.pc.state;

import java.io.IOException;


public class TrainStartState extends State {

	public TrainStartState(Context context) {
		super(context);
	}

	@Override
	public void handle() throws IOException {
		//以后要将接口改一下
		int result = context.updateDistance();
		if (result == 2) {
			context.setState(new TrainLeaveState(context));
		}
	}

	@Override
	public void doExtra() throws IOException {
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
