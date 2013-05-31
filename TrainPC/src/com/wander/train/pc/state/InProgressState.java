package com.wander.train.pc.state;

import java.io.IOException;


public class InProgressState extends State {
	
	public InProgressState(Context context) {
		super(context);
	}

	@Override
	public void handle() throws IOException {
		// 进行某些计算
		int result = context.updateDistance();
		if (result == 1) {
			context.setState(new TrainEnterState(context));
		}
	}

	@Override
	public void doExtra() throws IOException {
		// do nothing
	}

	@Override
	public String toString() {
		return "InProgress";
	}
}
