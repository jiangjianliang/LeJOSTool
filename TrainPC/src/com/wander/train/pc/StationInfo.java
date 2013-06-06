package com.wander.train.pc;

import java.io.DataOutputStream;
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
	 * 发送数据流
	 */
	private DataOutputStream sender;
	
	/**
	 * 心跳标记
	 */
	private boolean beatFlag = false;
	
	/**
	 * 延迟
	 */
	private int delay = 0;
	private static int DELAY_COUNT = 18;
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
	
	public StationInfo(MonitorModel mm, TrainInfo[] trainList, boolean isSwitch, int initWhich, DataOutputStream out) {
		this.mm = mm;
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
		
		sender = out;
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
	
	public synchronized void setColor(int color){
		this.color = color;
	}
	
	public synchronized int getColor(){
		return color;
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
			if(distance == 255){
				System.err.println("incorrect distance");
				return 0;
			}
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
	public boolean updateColor(){
		//System.err.println("color is "+ color);
		//根据color返回是哪一辆火车进站了
		int result = chooseTrain(color);
		if(result == -1){
			return false;
		}
		else{
			which = result;
			return true;
		}
	}
	
	private int chooseTrain(int color){
		int i = 0;
		for(; i < Config.TrainColor.length; i++){
			if(Config.TrainColor[i] == color){
				break;
			}
		}
		if(i == Config.TrainColor.length){
			return -1;
		}
		else{
			return i;
		}
	}

	/**
	 * 发送程序运行命令
	 */
	public void start(){
		
		try {
			sender.writeInt(Command.PROGRAM_START);
			sender.flush();
		} catch (IOException e) {
			System.err.println("ERROR-send program-start!");
			e.printStackTrace();
		}
		
	}
	

	/**
	 * 发送退出程序命令
	 */
	public void exit(){
		try {
			sender.writeInt(Command.EXIT);
			sender.flush();
		} catch (IOException e) {
			System.err.println("ERROR-send station-exit!");
			e.printStackTrace();
		}
	}
	
	public void switchMain(){
		try {
			sender.writeInt(Command.SWITCH_MAIN);
			sender.flush();
		} catch (IOException e) {
			System.err.println("ERROR-send station-switchmain!");
			e.printStackTrace();
		}
	}
	
	public void switchBranch(){
		try {
			sender.writeInt(Command.SWITCH_BRANCH);
			sender.flush();
		} catch (IOException e) {
			System.err.println("ERROR-send station-switchbranch!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void commandForward(int dest){
		mm.commandTrainForward(which, dest);
	}

	@Override
	public void commandBackward(int dest){
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
	public void commandStop(){
		//System.err.println("in commandStop + "+ which);
		mm.commandTrainStop(which);
	}

	@Override
	public void commandSwitchMain(boolean flag){
		//System.err.println("in commandSwitch + "+ flag);
		mm.commandStationSwitch(flag);
	}

	//HeartDetector检测使用
	
	public synchronized boolean isBeatFlag() {
		return beatFlag;
	}

	public synchronized void setBeatFlag(boolean beatFlag) {
		this.beatFlag = beatFlag;
	}
	
}