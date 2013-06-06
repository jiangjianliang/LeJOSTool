package com.wander.train.pc.state;


public class InProgressState extends State {
	
	public InProgressState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
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
	public void doExtra() {
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
