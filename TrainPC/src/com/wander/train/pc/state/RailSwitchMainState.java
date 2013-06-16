package com.wander.train.pc.state;

public class RailSwitchMainState extends State {
	
	
	public RailSwitchMainState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		context.incDelay();
		// 转向InProgress
		if(context.isExpired()){
			context.setState(new InProgressState(context));			
		}
	}

	@Override
	public void doExtra() {
		context.resetDelay(Context.RailMainDelay);
		//切换轨道
		context.commandSwitchMain(true);
	}

	@Override
	public String toString() {
		return "RailSwitchMain";
	}
}
