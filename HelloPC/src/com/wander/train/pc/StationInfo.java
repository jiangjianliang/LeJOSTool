package com.wander.train.pc;

import java.io.IOException;

import com.wander.train.pc.state.Context;
import com.wander.train.pc.state.InProgressState;
import com.wander.train.pc.state.State;

public abstract class StationInfo implements Context{

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
	 * 延迟
	 */
	private int delay = 0;
	private final static int DELAY_COUNT = 8;

	protected State state = new InProgressState(this);
	
	public StationInfo(MonitorModel mm, int dis) {
		this.mm = mm;
		distance = dis;
		ultraSonic = new UltraSonic(dis);
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
		public UltraSonic(int distance) {
			this.distance = distance;
		}

		public int update(int dis) {
			// train enter
			if (distance > THRESH && (distance = dis) <= THRESH)// TODO 15这个值需要修改
				return 1;
			// train leave
			if (distance <= THRESH && (distance = dis) > THRESH)
				return 2;
			return 0;
		}
	}
	

	/**
	 * 推动状态机向前走的方法
	 */
	public void push() throws IOException {
		state.handle();
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
		System.err.println("in updateWhich");
		for(int i = 0; i < trainList.length; i++){
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

/**
 * 含有换轨的站台
 * 
 * @author wander
 * 
 */
class SwitchStationInfo extends StationInfo {
	// TrainEnter-TrainStop-RailSwtich-TrainStart-TrainLeave-RailSwitch

	public SwitchStationInfo(MonitorModel mm, int dis) {
		super(mm, dis);
		stationIndex = 1;
	}

	@Override
	public void push() throws IOException {
		System.out.println(state.toString());
		state.handle();
	}
	
	@Override
	public boolean isSwitch() {
		return true;
	}

}

/**
 * 不含有换轨的站台
 * 
 * @author wander
 * 
 */
class NormalStationInfo extends StationInfo {
	// TrainEnter-TrainStop-TrainStart-TrainLeave

	public NormalStationInfo(MonitorModel mm, int dis) {
		super(mm, dis);
		stationIndex = 2;
	}

	@Override
	public boolean isSwitch() {
		return false;
	}

}
