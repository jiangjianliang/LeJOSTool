package com.wander.train.pc.station;


import com.wander.train.pc.common.BluetoothWriter;
import com.wander.train.pc.common.Command;
import com.wander.train.pc.common.Config;
import com.wander.train.pc.common.HeartDetectable;
import com.wander.train.pc.common.MonitorModel;
import com.wander.train.pc.state.Context;
import com.wander.train.pc.state.InProgressState;
import com.wander.train.pc.state.State;
import com.wander.train.pc.train.TrainInfo;

public class StationInfo implements Context,HeartDetectable {
	/**
	 * 距离的类型 0 TouchSensor 1 UltrasonicSensor
	 */
	private static int DISTANCE_TYPE = 1;

	private MonitorModel mm;
	/**
	 * 前一次距离
	 */
	private int preDistance;
	/**
	 * 最新的距离
	 */
	private int distance;

	/**
	 * 颜色,用于分辨火车
	 */
	private int color = 7;

	/**
	 * 用于记录是哪一辆火车进站
	 */
	private int which;
	private boolean flag;
	private int colWhich;
	private boolean colFlag;
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
	 * 发送数据流
	 */
	private BluetoothWriter writer;
	/**
	 * 心跳标记
	 */
	private boolean beatFlag = false;

	/**
	 * 延迟
	 */
	private int delay = 0;
	private int DelayThresh = 18;
	
	/**
	 * 保持状态机的变量
	 */
	private State state;
	/**
	 * 表明是否为交换站点的标记 true:
	 * TrainEnter-TrainStop-RailSwtich-TrainStart-TrainLeave-RailSwitch
	 * false:
	 * TrainEnter-TrainStop-TrainStart-TrainLeave
	 */
	private boolean isSwitch = false;

	public StationInfo(MonitorModel mm, TrainInfo[] trainList,
			boolean isSwitch, int initWhich, BluetoothWriter writer) {
		this.mm = mm;
		if (DISTANCE_TYPE == 0) {
			preDistance = 0;
			distance = 0;
		} else {
			preDistance = 255;
			distance = 255;
		}
		this.isSwitch = isSwitch;
		resetState();
		// 站台编号
		stationIndex = autoIndex;
		autoIndex++;

		this.writer = writer;
	}

	@Override
	public boolean isSwitch() {
		return isSwitch;
	}

	public synchronized void setDistance(int dis) {
		this.distance = dis;
	}

	public synchronized int getDistance() {
		return distance;
	}

	public synchronized void setColor(int color) {
		this.color = color;
	}

	public synchronized int getColor() {
		return color;
	}

	/**
	 * 推动状态机向前走的方法
	 */
	public void push(){
		state.handle();
	}

	/**
	 * 重新设定状态
	 */
	public void resetState() {
		state = new InProgressState(this);
	}

	@Override
	public void setState(State newState){
		System.err.println(stationIndex + "]" + state.toString() + "-->"
				+ newState.toString());
		state = newState;
		state.doExtra();
	}

	@Override
	public void resetDelay(int initDelay) {
		delay = 0;
		DelayThresh = initDelay;
	}

	@Override
	public void incDelay() {
		delay++;
	}

	@Override
	public boolean isExpired() {
		return delay > DelayThresh;
	}

	@Override
	public int updateDistance() {

		if (distance == 255) {
			System.err.println("incorrect distance");
			return 0;
		}
		// train enter
		if (preDistance > ULTRASONIC_THRESH
				&& (preDistance = distance) <= ULTRASONIC_THRESH)
																	// 15这个值需要修改
			return 1;
		// train leave
		if (preDistance <= ULTRASONIC_THRESH
				&& (preDistance = distance) > ULTRASONIC_THRESH)
			return 2;
		return 0;
	}

	@Override
	public boolean updateColor() {
		// 根据color返回是哪一辆火车进站了
		int result = chooseTrain();
		if (result == -1) {
			return false;
		} else {
			System.err.println(stationIndex+"]choose "+ result);
			if(flag){//已经有火车在独占站台
				if(which != result){
					colWhich = result;
					return true;					
				}
				else{
					return false;
				}
			}
			else{
				which = result;
				return true;
			}
		}
	}

	private int chooseTrain() {
		int i = 0;
		for (; i < Config.TrainColor.length; i++) {
			if (Config.TrainColor[i] == color) {
				break;
			}
		}
		if (i == Config.TrainColor.length) {
			return -1;
		} else {
			System.err.println(stationIndex+"]color is "+color);
			return i;
		}
	}

	
	
	/**
	 * 发送程序运行命令
	 */
	public void start() {
		writer.writeIntAndFlush(Command.PROGRAM_START, "send program-start!");
	}

	/**
	 * 发送退出程序命令
	 */
	public void exit() {
		writer.writeIntAndFlush(Command.EXIT, "send station-exit!");
	}
	/**
	 * 发送换轨道命令
	 */
	public void switchMain() {
		writer.writeIntAndFlush(Command.SWITCH_MAIN, "send station-switchmain!");
	}
	/**
	 * 发送换轨道命令
	 */
	public void switchBranch() {
		writer.writeIntAndFlush(Command.SWITCH_BRANCH, "send station-switchbranch!");		
	}

	@Override
	public void commandForward(int dest) {
		mm.commandTrainForward(which, dest);
	}

	@Override
	public void commandBackward(int dest) {
		mm.commandTrainBackward(which, dest);
	}

	@Override
	public void commandSlowDown(int delta) {
		mm.commandTrainSlowDown(which, delta);
	}

	@Override
	public void commandSpeedUp(int delta) {
		mm.commandTrainSpeedUp(which, delta);
	}

	@Override
	public void commandStop() {
		mm.commandTrainStop(which);
	}
	
	@Override
	public void commandColForward(int dest) {
		mm.commandTrainForward(colWhich, dest);
	}

	@Override
	public void commandColBackward(int dest) {
		mm.commandTrainBackward(colWhich, dest);
	}
	
	@Override
	public void commandColStop(){
		mm.commandTrainStop(colWhich);
	}

	@Override
	public void commandSwitchMain(boolean flag) {
		if(flag){
			switchMain();
		}
		else{
			switchBranch();
		}
	}

	// HeartDetector检测使用
	@Override
	public synchronized boolean isBeatFlag() {
		return beatFlag;
	}
	@Override
	public synchronized void setBeatFlag(boolean beatFlag) {
		this.beatFlag = beatFlag;
	}

	@Override
	public void itrInit() {
		which = -1;
	}

	@Override
	public boolean itrNext() {
		if(which == Config.TRAIN_NUM){			
			return false;
		}
		else{
			which++;
			return true;
		}
	}

	@Override
	public void enterCS() {
		flag = true;
		
	}

	@Override
	public void exitCS() {
		flag = false;
	}

	@Override
	public void enterCol() {
		//TODO 考虑到后面需要还原状态这里可能要进行减速
		System.err.println(stationIndex+"]enterCol: "+colWhich);
		mm.commandTrainStop(colWhich);
		colFlag = true;
	}

	@Override
	public void exitCol() {
		System.err.println(stationIndex+"]exitCol: "+colWhich);
		mm.commandTrainResume(colWhich);
		//TODO 也要将which的值换成colWhich
		which = colWhich;
		colFlag = false;
	}

	@Override
	public boolean isInCol() {
		return colFlag;
	}
	
	
}