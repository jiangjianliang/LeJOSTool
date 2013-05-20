package com.wander.train.pc;

public abstract class StationInfo {

	public int distance;
	public int isIn = 0;
	public UltraSonic ultraSonic;
	private State state;
	
	public StationInfo(int dis) {
		distance = dis;
		ultraSonic = new UltraSonic(dis);
	}

	public int updateDistance() {
		return ultraSonic.update(distance);
	}
	
	/**
	 * 判断距离变化的类
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
			if (distance > 15 && (distance = dis) <= 15)
				return 1;
			// train leave
			if (distance <= 15 && (distance = dis) > 15)
				return 2;
			return 0;
		}
	}
	
	public abstract boolean isSwitch();
	
	interface State{
		void handle();
	}
	
	
	class InProgress implements State{

		@Override
		public void handle() {
			//进行某些计算
			state = new ForwardTrainEnter();
		}
	}
	
	class ForwardTrainEnter implements State{

		@Override
		public void handle() {
			// TODO Auto-generated method stub
			
			state = new ForwardTrainLeave();
		}
	}
	
	class ForwardTrainLeave implements State{

		@Override
		public void handle() {
			// TODO Auto-generated method stub
			
			state = new TrainArrive();
			
		}
		
	}
	
	class TrainArrive implements State{

		@Override
		public void handle() {
			// TODO Auto-generated method stub
			state = new TrainStop();
		}
		
	}
	
	class TrainStop implements State{

		@Override
		public void handle() {
			// TODO 需要知道是哪一个列车
			
			if(isSwitch()){
				//转向ForwardSwitchRail
				state = new ForwardSwitchRail();
			}
			else{
				//转向TrainStart
				state = new TrainStart();
			}
			
		}
		
	}
	
	class ForwardSwitchRail implements State{

		@Override
		public void handle() {
			// TODO Auto-generated method stub
			
			state = new TrainStart();
		}
		
	}
	
	class TrainStart implements State{

		@Override
		public void handle() {
			// TODO 需要知道是哪一个列车
			
			state = new BackwardTrainEnter();
		}
		
	}
	
	class BackwardTrainEnter implements State{

		@Override
		public void handle() {

			state = new BackwardTrainLeave();
		}
		
	}
	
	class BackwardTrainLeave implements State{

		@Override
		public void handle() {
			
			
			if(isSwitch()){
				//转向BackwardSwitchRail
				state = new BackwardSwitchRail();
			}
			else{
				//转向InProgress
				state = new InProgress();
			}
		}
		
	}
	
	class BackwardSwitchRail implements State{

		@Override
		public void handle() {
			//转向InProgress
			state = new InProgress();
		}
		
	}
}
/**
 * 含有换轨的站台
 * @author wander
 *
 */
class SwitchStationInfo extends StationInfo{
	//TrainEnter-TrainLeave-TouchSensor-TrainStop-SwtichRail-TrainStart-TrainEnter-TrainLeave-SwitchRail
	
	public SwitchStationInfo(int dis) {
		super(dis);
	}

	@Override
	public boolean isSwitch() {
		return true;
	}
	
}
/**
 * 不含有换轨的站台
 * @author wander
 *
 */
class NormalStationInfo extends StationInfo{
	//TrainEnter-TrainLeave-TouchSensor-TrainStop-TrainStart-TrainEnter-TrainLeave
	
	public NormalStationInfo(int dis) {
		super(dis);
	}

	@Override
	public boolean isSwitch() {
		return false;
	}
	
}
