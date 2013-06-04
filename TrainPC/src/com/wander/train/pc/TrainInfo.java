package com.wander.train.pc;

import java.io.DataOutputStream;
import java.io.IOException;

public class TrainInfo {
	
	private int speed;
	private int state;	//0 : stop;		1 : forward;	-1 : backward;
	private int position;	//0 : sA -> sB;	1 : sB;	2 : sB -> sA;	3 : sA
	private int destination;		//0 : none;		1 : sB;		2 : sA;
	
	/**
	 * 发送数据流
	 */
	private DataOutputStream sender;
	
	
	public TrainInfo(DataOutputStream out){
		speed = 4;
		state = 0;
		position = 0;
		destination = 0;
		if(out == null){
			System.err.println("is null");
		}
		sender = out;
	}
	
	public TrainInfo(int speed, int state, int position, int destination, DataOutputStream out){
		this.speed = speed;
		this.state = state;
		this.position = position;
		this.destination = destination;
		sender = out;
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
		try {
			sender.writeInt(Command.PROGRAM_START);
			sender.flush();
		} catch (IOException e) {
			System.err.println("ERROR-send program-start!");
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送火车停止命令
	 */
	public void stop() {
		try {
			sender.writeInt(Command.TRAIN_STOP);
			sender.flush();
		} catch (IOException e) {
			System.err.println("ERROR-send train-stop!");
			e.printStackTrace();
		}
	}
	/**
	 * 发送前进命令
	 * @param dest
	 */
	public void forward(int dest){
		setDestination(dest);
		setForward();
		int cmd = Command.SPEED_MARK + getSpeed();
		
		System.err.println("forward " +  "], dest=" + dest+", cmd="+cmd);
		try {
			sender.writeInt(cmd);
			sender.flush();
		} catch (IOException e) {
			System.err.println("ERROR-send train-forward!");			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 发送后退命令
	 * @param dest
	 */
	public void backward(int dest){
		setDestination(dest);
		setBackward();
		int cmd = Command.SPEED_MARK + getSpeed();
		System.err.println("backward " +", dest=" + dest+", cmd="+(-cmd));
		try {
			sender.writeInt(-cmd);
			sender.flush();
		} catch (IOException e) {
			System.err.println("ERROR-send train-forward!");			
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
			System.err.println("ERROR-send train-exit!");
			e.printStackTrace();
		}
	}
	
}