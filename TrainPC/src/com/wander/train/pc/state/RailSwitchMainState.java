package com.wander.train.pc.state;

public class RailSwitchMainState extends State {
	
	
	public RailSwitchMainState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		// 转向InProgress
		context.setState(new InProgressState(context));

	}

	@Override
	public void doExtra() {
		//切换轨道
		context.commandSwitchMain(true);
	}

	@Override
	public String toString() {
		return "RailSwitchMain";
	}
}
