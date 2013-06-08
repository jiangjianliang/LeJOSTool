package com.wander.train.pc.train;

import com.wander.train.pc.common.BluetoothWriter;
import com.wander.train.pc.common.Command;
import com.wander.train.pc.common.HeartDetectable;

/**
 * 火车信息
 * @author wander
 *
 */
public class TrainInfo implements HeartDetectable{
	
	private int speed;
	private int state;	//0 : stop;		1 : forward;	-1 : backward;
	private int position;	//0 : sA -> sB;	1 : sB;	2 : sB -> sA;	3 : sA
	private int destination;		//0 : none;		1 : sB;		2 : sA;
	
	/**
	 * 心跳检测
	 */
	private boolean beatFlag;
	
	/**
	 * 发送数据
	 */
	private BluetoothWriter writer;
	
	public TrainInfo(BluetoothWriter writer){
		speed = 4;
		state = 0;
		position = 0;
		destination = 0;
		this.writer = writer;
	}
	
	public TrainInfo(int speed, int state, int position, int destination, BluetoothWriter writer){
		this.speed = speed;
		this.state = state;
		this.position = position;
		this.destination = destination;
		this.writer = writer;
	}
	
	public boolean isForward(){
		return state > 0;
	}
	
	public boolean isStop(){
		return state == 0;
	}
	
	public boolean isBackward(){
		return state < 0;
	}
	
	public void setForward(){
		state = 1;
	}
	public void setStop(){
		state = 0;
	}
	public void setBackward(){
		state = -1;
	}

	public boolean isArrival(){
		return position == destination;
	}
	
	//getter and setter
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}
	
	/**
	 * 发送程序运行命令
	 */
	public void start(){
		writer.synWriteIntAndFlush(Command.PROGRAM_START, "send program-start!");
	}
	
	/**
	 * 发送火车停止命令
	 */
	public void stop() {
		writer.synWriteIntAndFlush(Command.TRAIN_STOP, "send train-stop!");
	}
	/**
	 * 发送前进命令
	 * @param dest
	 */
	public void forward(int dest){
		//speed = 7;
		setDestination(dest);
		setForward();
		int cmd = Command.SPEED_MARK + getSpeed();
		System.err.println("forward " +  "], dest=" + dest+", cmd="+cmd);
		
		writer.synWriteIntAndFlush(cmd, "send train-forward!");
	}
	
	/**
	 * 发送后退命令
	 * @param dest
	 */
	public void backward(int dest){
		//speed = 7;
		
		setDestination(dest);
		setBackward();
		int cmd = Command.SPEED_MARK + getSpeed();
		System.err.println("backward " +", dest=" + dest+", cmd="+(-cmd));
		
		writer.synWriteIntAndFlush(-cmd, "send train-backward!");
		
	}
	
	/**
	 * 加速
	 * @param i
	 */
	public void speedUp(int i){
		int newSpeed = getSpeed()+i;
		if(newSpeed > 10){
			newSpeed = 10;
		}
		changeSpeed(newSpeed);
	}
	/**
	 * 减速
	 */
	public void slowDown(int i){
		int newSpeed = getSpeed()-i;
		if(newSpeed < 1){
			newSpeed = 1;
		}
		changeSpeed(newSpeed);
	}
	
	private void changeSpeed(int speed){
		System.err.println("changespeed "+speed);
		setSpeed(speed);
		int cmd = Command.SPEED_MARK + getSpeed();
		cmd = (isForward() ? cmd : -cmd);
		
		writer.synWriteIntAndFlush(cmd, "send train-change!");
	}
	
	/**
	 * 发送退出程序命令
	 */
	public void exit(){
		writer.synWriteIntAndFlush(Command.EXIT, "send train-exit");
	}

	//HeartDetector使用
	@Override
	public boolean isBeatFlag() {
		return beatFlag;
	}
	@Override
	public void setBeatFlag(boolean beatFlag) {
		this.beatFlag = beatFlag;
	}
	
}