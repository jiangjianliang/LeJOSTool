package com.wander.train.pc;

import java.io.IOException;

public abstract class StationInfo {

	private MonitorModel mm;

	public int distance;
	public int isIn = 0;
	public UltraSonic ultraSonic;

	public TrainInfo[] trainList;
	private int which;
	
	protected int stationIndex;
	
	protected State state = new InProgress();
	private int delay = 0;
	private int DELAY_COUNT = 5;

	public StationInfo(MonitorModel mm, int dis) {
		this.mm = mm;
		distance = dis;
		ultraSonic = new UltraSonic(dis);
	}

	public int updateDistance() {
		return ultraSonic.update(distance);
	}

	/**
	 * 推动状态机向前走的方法
	 */
	public void push() throws IOException {
		state.handle();
	}

	/**
	 * 判断距离变化的类
	 * 
	 * @author wander
	 * 
	 */
	class UltraSonic {
		private int distance;

		public UltraSonic(int distance) {
			this.distance = distance;
		}

		public int update(int dis) {
			// train enter
			if (distance > 15 && (distance = dis) <= 15)// TODO 15这个值需要修改
				return 1;
			// train leave
			if (distance <= 15 && (distance = dis) > 15)
				return 2;
			return 0;
		}
	}

	/**
	 * 是否含有换轨控制
	 * 
	 * @return
	 */
	public abstract boolean isSwitch();

	interface State {
		void handle() throws IOException;

		void doExtra() throws IOException;
	}

	class InProgress implements State {

		@Override
		public void handle() throws IOException {
			// 进行某些计算
			int result = updateDistance();
			if (result == 1) {
				state = new TrainEnter();
				state.doExtra();
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

	class TrainEnter implements State {

		@Override
		public void handle() throws IOException {
			state = new TrainStop();
			state.doExtra();
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

	class TrainStop implements State {

		@Override
		public void handle() throws IOException {

			if (isSwitch()) {
				// 转向ForwardSwitchRail
				state = new ForwardSwitchRail();
				state.doExtra();
			} else {
				// 转向TrainStart
				state = new TrainStart();
				state.doExtra();
			}
		}

		@Override
		public void doExtra() throws IOException {
			// 发出列车停止命令
			for(int i = 0; i < trainList.length; i++){
				if(trainList[i].getDestination() == stationIndex){
					//先记下是哪一辆火车要到达站台
					which = i;
					break;
				}
			}
			mm.commandStop(which);
		}

		@Override
		public String toString() {
			return "TrainStop";
		}

	}

	class ForwardSwitchRail implements State {

		@Override
		public void handle() throws IOException {
			delay++;
			if (delay > DELAY_COUNT) {
				state = new TrainStart();
				state.doExtra();
			}
		}

		@Override
		public void doExtra() throws IOException {
			delay = 0;
			// 发出换轨道命令
			mm.commandSwitchMain(false);
		}

		@Override
		public String toString() {
			return "ForwardSwitchRail";
		}

	}

	class TrainStart implements State {

		@Override
		public void handle() throws IOException {
			int result = updateDistance();
			if (result == 2) {
				state = new TrainLeave();
				state.doExtra();
			}
		}

		@Override
		public void doExtra() throws IOException {
			// 发出列车出发命令
			if(isSwitch()){
				mm.commandBackward(which, 2);//朝着A站台
			}
			else{
				mm.commandForward(which, 1);//朝着B站台
			}
		}

		@Override
		public String toString() {
			return "TrainStart";
		}
		
	}

	class TrainLeave implements State {

		@Override
		public void handle() throws IOException {

			if (isSwitch()) {
				// 转向BackwardSwitchRail
				if(delay > DELAY_COUNT){
					state = new BackwardSwitchRail();
					state.doExtra();
				}
				delay++;
			} else {
				// 转向InProgress
				state = new InProgress();
				state.doExtra();
			}
		}

		@Override
		public void doExtra() throws IOException {
			// do nothing
			delay = 0;
		}

		@Override
		public String toString() {
			return "TrainLeave";
		}
		
		

	}

	class BackwardSwitchRail implements State {

		@Override
		public void handle() throws IOException {
			// 转向InProgress
			state = new InProgress();
			state.doExtra();

		}

		@Override
		public void doExtra() throws IOException {
			//切换轨道
			mm.commandSwitchMain(true);
		}

		@Override
		public String toString() {
			return "BackwardSwitchRail";
		}

	}
}

/**
 * 含有换轨的站台
 * 
 * @author wander
 * 
 */
class SwitchStationInfo extends StationInfo {
	// TrainEnter-TrainStop-SwtichRail-TrainStart-TrainLeave-SwitchRail

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
