package com.wander.train.pc;

import java.io.IOException;

import com.wander.train.pc.state.Context;
import com.wander.train.pc.state.InProgressState;
import com.wander.train.pc.state.State;

public class StationInfo implements Context{
	/**
	 * 距离的类型
	 * 0 TouchSensor
	 * 1 UltrasonicSensor
	 */
	private static int DISTANCE_TYPE = 0;
	
	private MonitorModel mm;
	/**
	 * 前一次距离
	 */
	private int preDistance;
	/**
	 * 最新的距离
	 */
	private int distance;

	private TrainInfo[] trainList;
	/**
	 * 用于记录是哪一辆火车进站
	 */
	private int which;
	
	/**
	 * 本站台的索引
	 */
	private int stationIndex;
	/**
	 * 自动增长的编号
	 */
	private static int autoIndex = 1;
	/**
	 * UltrasonicSensor的临界值
	 */
	private final static int ULTRASONIC_THRESH = 15;
	/**
	 * TouchSensor的取值
	 */
	private final static int TOUCH_PRESS = 1;
	private final static int TOUCH_RELEASE = 0;
	
	/**
	 * 延迟
	 */
	private int delay = 0;
	private static int DELAY_COUNT = 15;
	/**
	 * 保持状态机的变量
	 */
	private State state;
	/**
	 * 表明是否为交换站点的标记
	 * true:	TrainEnter-TrainStop-RailSwtich-TrainStart-TrainLeave-RailSwitch
	 * false:	TrainEnter-TrainStop-TrainStart-TrainLeave
	 */
	private boolean isSwitch = false;
	
	public StationInfo(MonitorModel mm, TrainInfo[] trainList, boolean isSwitch) {
		this.mm = mm;
		this.trainList = trainList;
		if(DISTANCE_TYPE == 0){
			preDistance = 0;
			distance = 0;
		}
		else{
			preDistance = 255;
			distance = 255;
		}
		this.isSwitch = isSwitch;
		resetState();
		//站台编号
		stationIndex = autoIndex;
		autoIndex++;
	}

	@Override
	public boolean isSwitch() {
		return isSwitch;
	}
	
	public synchronized void setDistance(int dis){
		this.distance = dis;
	}
	
	public synchronized int getDistance(){
		return distance;
	}
	
	/**
	 * 推动状态机向前走的方法
	 */
	public void push() throws IOException {
		state.handle();
	}
	/**
	 * 重新设定状态
	 */
	public void resetState(){
		state = new InProgressState(this);
	}
	
	@Override
	public void setState(State newState) throws IOException{
		System.err.println(stationIndex+"]"+state.toString()+"-->"+newState.toString());
		state = newState;
		state.doExtra();
	}
	
	@Override
	public void resetDelay(){
		delay = 0;
	}
	
	@Override
	public void incDelay(){
		delay++;
	}
	
	@Override
	public boolean isExpired(){
		return delay > DELAY_COUNT;
	}
	
	@Override
	public int updateDistance() {
		if(DISTANCE_TYPE == 0){	
			if(distance == TOUCH_PRESS){
				return 1;
			}
			else if(distance == TOUCH_RELEASE){
				return 2;
			}
			return 0;
		}
		else{
			// train enter
			if (preDistance > ULTRASONIC_THRESH && (preDistance = distance) <= ULTRASONIC_THRESH)// TODO 15这个值需要修改
				return 1;
			// train leave
			if (preDistance <= ULTRASONIC_THRESH && (preDistance = distance) > ULTRASONIC_THRESH)
				return 2;
			return 0;
		}
	}
	
	@Override
	public void updateWhich() {
		System.err.println("in updateWhich");
		for(int i = trainList.length -1; i >= 0; i--){
			if(trainList[i].getDestination() == stationIndex){
				//先记下是哪一辆火车要到达站台
				which = i;
				break;
			}
		}
		System.err.println("which is "+ which);
	}

	@Override
	public void commandForward(int dest) throws IOException {
		//System.err.println("in commandForward + "+ dest);
		mm.commandForward(which, dest);
	}

	@Override
	public void commandBackward(int dest)  throws IOException {
		//System.err.println("in commandBackward + "+ dest);
		//TODO for test
		mm.commandBackward(which, dest);
		
	}
	
	@Override
	public void commandStop() throws IOException {
		//System.err.println("in commandStop + "+ which);
		mm.commandStop(which);
	}

	@Override
	public void commandSwitchMain(boolean flag) throws IOException {
		//System.err.println("in commandSwitch + "+ flag);
		mm.commandSwitchMain(flag);
	}


	
}