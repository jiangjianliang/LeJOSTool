package com.wander.train.pc.state;

import java.io.IOException;


public class TrainEnterState extends State {

	public TrainEnterState(Context context) {
		super(context);
	}

	@Override
	public void handle() throws IOException {
		context.setState(new TrainStopState(context));
	}

	@Override
	public void doExtra() throws IOException {
		// do nothing
	}

	@Override
	public String toString() {
		return "TrainEnter";
	}
	
	
}
