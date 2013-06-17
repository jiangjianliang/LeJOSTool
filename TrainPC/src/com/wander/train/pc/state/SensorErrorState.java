package com.wander.train.pc.state;
/**
 * 传感器数据错误状态
 * @author wander
 *
 */
public class SensorErrorState extends State {

	public SensorErrorState(Context context) {
		super(context);
	}

	@Override
	public void handle() {
		//TODO 转向哪一个状态？
		//System.err.println("in state SensorErrorState");
	}

	@Override
	public void doExtra() {
		context.enterCS();
		//TODO 让所有火车都停止？
		context.itrInit();
		while(context.itrNext()){
			context.commandStop();			
		}
	}

	@Override
	public String toString() {
		return "SensorErrorState";
	}

}