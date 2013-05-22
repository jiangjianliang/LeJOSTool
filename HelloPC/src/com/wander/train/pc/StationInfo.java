package com.wander.train.pc;

import java.io.IOException;

import com.wander.train.pc.state.Context;
import com.wander.train.pc.state.InProgressState;
import com.wander.train.pc.state.State;

public class StationInfo implements Context{

	private MonitorModel mm;

	public UltraSonic ultraSonic;
	public int distance;

	public TrainInfo[] trainList;
	/**
	 * 用于记录是哪一辆火车进站
	 */
	private int which;
	
	/**
	 * 本站台的索引
	 */
	protected int stationIndex;
	/**
	 * 自动增长的编号
	 */
	private static int sIndex = 0;
	
	/**
	 * 延迟
	 */
	private int delay = 0;
	private final static int DELAY_COUNT = 5;
	/**
	 * 保持状态机的变量
	 */
	protected State state;
	/**
	 * 表明是否为交换站点的标记
	 * true:	TrainEnter-TrainStop-RailSwtich-TrainStart-TrainLeave-RailSwitch
	 * false:	TrainEnter-TrainStop-TrainStart-TrainLeave
	 */
	private boolean isSwitch = false;
	
	public StationInfo(MonitorModel mm, int dis, boolean isSwitch) {
		this.mm = mm;
		distance = dis;
		this.isSwitch = isSwitch;
		ultraSonic = new UltraSonic(dis);
		resetState();
	}

	@Override
	public boolean isSwitch() {
		return isSwitch;
	}
	
	/**
	 * 判断距离变化的类
	 * 
	 * @author wander
	 * 
	 */
	class UltraSonic {
		private int distance;
		private final static int THRESH = 15;
		private final static int PRESS = 1;
		private final static int RELEASE = 0;
		public UltraSonic(int distance) {
			this.distance = distance;
		}

		public int update(int dis) {
			if(dis == PRESS){
				return 1;
			}
			else if(dis == RELEASE){
				return 2;
			}
			return 0;
			/*
			if(distance == RELEASE && (distance = dis) == PRESS){
				return 1;
			}
			else if(distance == PRESS && (distance = dis) == RELEASE){
				
			}
			return 0;
			*/
			/*
			// train enter
			if (distance > THRESH && (distance = dis) <= THRESH)// TODO 15这个值需要修改
				return 1;
			// train leave
			if (distance <= THRESH && (distance = dis) > THRESH)
				return 2;
			return 0;
			*/
		}
	}
	

	/**
	 * 推动状态机向前走的方法
	 */
	public void push() throws IOException {
		System.err.print("["+stationIndex+"]");
		System.err.println(state.toString());
		state.handle();
	}
	
	public void resetState(){
		state = new InProgressState(this);
		//trainList[0].setDestination(2);
	}
	
	@Override
	public void setState(State newState) throws IOException{
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
		return ultraSonic.update(distance);
	}
	
	@Override
	public void updateWhich() {
		//System.err.println("in updateWhich");
		for(int i = 0; i < trainList.length; i++){
			if(trainList[i].getDestination() == stationIndex){
				//先记下是哪一辆火车要到达站台
				which = i;
				break;
			}
		}
		//System.err.println("which is "+ which);
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