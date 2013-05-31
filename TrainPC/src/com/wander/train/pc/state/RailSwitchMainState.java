package com.wander.train.pc.state;

import java.io.IOException;

public class RailSwitchMainState extends State {
	
	
	public RailSwitchMainState(Context context) {
		super(context);
	}

	@Override
	public void handle() throws IOException {
		// 转向InProgress
		context.setState(new InProgressState(context));

	}

	@Override
	public void doExtra() throws IOException {
		//切换轨道
		context.commandSwitchMain(true);
	}

	@Override
	public String toString() {
		return "RailSwitchMain";
	}
}
