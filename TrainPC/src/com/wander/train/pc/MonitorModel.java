package com.wander.train.pc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

public class MonitorModel implements Config{
	/**
	 * NXT的Mac地址
	 */
	private static NXTInfo[] nxts = new NXTInfo[3];
	{
		nxts[0] = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT1",
			"00:16:53:10:40:a7");
		nxts[1] = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT2",
			"00:16:53:13:3a:1f");
		nxts[2] = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT3",
			"00:16:53:09:78:1f");
	}
	/**
	 * NXT的名称
	 */
	private static String[] nxtStr = new String[3];
	{
		nxtStr[0] = "usb://NXT_0";
		nxtStr[1] = "usb://NXT_1";
		nxtStr[2] = "usb://NXT_2";
	}
	
	//实例成员
	public StationInfo[] stationList= new StationInfo[STATION_NUM];
	// private NXTConnector[] pcNxtList;
	private NXTComm[] stationNxtList = new NXTComm[STATION_NUM];
	private DataOutputStream[] stationSender = new DataOutputStream[STATION_NUM];
	private DataInputStream[] stationReceiver = new DataInputStream[STATION_NUM];
	

	/*
	 * 假定同方向上有TrainInfo[0]早于TrainInfo[1]， 也就是TrainInfo[0]会更早到达它们的共同站点
	 */
	public TrainInfo[] trainList = new TrainInfo[TRAIN_NUM];
	private NXTComm[] trainNxtList = new NXTComm[TRAIN_NUM];
	private DataOutputStream[] trainSender = new DataOutputStream[TRAIN_NUM];
	private DataInputStream[] trainReceiver = new DataInputStream[TRAIN_NUM];
	
	public MonitorModel() {		
		connect();
		init();
	}
	/**
	 * 连接设备
	 */
	private void connect(){
		boolean[] stationConnected = new boolean[stationList.length];
		boolean[] trainConnected = new boolean[trainList.length];
		/*
		 * pcNxtList = new NXTConnector[2]; // initialize NXT connection
		 * pcNxtList[0] = new NXTConnector(); pcNxtList[1] = new NXTConnector();
		 * connected_1 = pcNxtList[0].connectTo("bt://NXT"); connected_2 =
		 * pcNxtList[1].connectTo("bt://NXT_OF");
		 */		
		// initialize input and output
		if( CONN_TYPE == 0){
			
		}
		else if(CONN_TYPE == 1){
			boolean total = true;
			try {
				for(int i = 0; i < stationList.length; i++){
					stationNxtList[i] = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
					stationConnected[i] = stationNxtList[i].open(nxts[i]);
					total = total && stationConnected[i];
				}

			} catch (NXTCommException e) {
				e.printStackTrace();
				System.err.println("error while connection to Station.");
				System.exit(-1);
			}

			if (!total) {
				System.err.println("error while connection to Station.");
				System.exit(-1);
			}
			
			for (int i = 0; i < stationList.length; i++) {
				stationSender[i] = new DataOutputStream(stationNxtList[i].getOutputStream());
				stationReceiver[i] = new DataInputStream(stationNxtList[i].getInputStream());
			}
			
			
			//连接火车
			total = true;
			try{
				for(int i=0; i < trainList.length; i++){
					trainNxtList[i] = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
					trainConnected[i] = stationNxtList[i].open(nxts[TRAIN_NXT_START+i]);
					total = total && stationConnected[i];
				}
			} catch(NXTCommException e){
				e.printStackTrace();
				System.err.println("error while connection to Train.");
				System.exit(-1);
			}
			
			if(!total){
				System.err.println("error while connection to Train.");
				System.exit(-1);
			}
			
			for(int i=0; i< trainList.length; i++){
				trainSender[i] = new DataOutputStream(trainNxtList[i].getOutputStream());
				trainReceiver[i] = new DataInputStream(trainNxtList[i].getInputStream());
			}
		}
	}
	/**
	 * 初始化
	 */
	private void init() {
		// initialize train
		
		for(int i=0; i < trainList.length; i++){
			trainList[i] = new TrainInfo(trainSender[i]);
		}

		trainList[0] = new TrainInfo(5, 0, 0,1);
		trainList[1] = new TrainInfo(5, 0, 2,0);
		
		// initialize station
		for(int i=0; i < stationList.length; i++){
			//TODO 以后火车数量增加需要修改
			if(i == SWITCH_INDEX){
				stationList[i] = new StationInfo(this, trainList, true, 0, stationSender[i]);
			}
			else{
				stationList[i] = new StationInfo(this, trainList, false, 1, stationSender[i]);
			}
			//TODO 以后再管这个
			new UpdateDistanceThread(stationReceiver[i], stationList[i]).start();
		}
	}
	
	/**
	 * 推动状态机
	 * @throws IOException 
	 */
	public void push() throws IOException{
		for (int i = 0; i < stationList.length; i++) {
			stationList[i].push();
		}
	}
	//具体的命令
	public void commandStart() {
		//TODO 以后修改
		for(int i =0; i< stationList.length; i++){
			stationList[i].resetState();
			try {
				stationSender[i].writeInt(Command.PROGRAM_START);
				stationSender[i].flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void commandStop(int i) throws IOException {
		trainList[i].setStop();
		// trainList[i].setDestination(trainList[i].getPosition());
		if (i == 0) {
			System.err.println("sending stop for [i=" + i + "], cmd="+Command.TRAIN_STOP_A);
			for(int j=0; j < stationList.length; j++){
				stationSender[j].writeInt(Command.TRAIN_STOP_A);				
			}
		} else {
			System.err.println("sending stop for [i=" +  i + "], cmd="+Command.TRAIN_STOP_B);
			for(int j=0; j < stationList.length; j++){				
				stationSender[j].writeInt(Command.TRAIN_STOP_B);
			}
		}
		for(int j =0; j< stationList.length; j++){
			stationSender[j].flush();			
		}
	}

	public void commandForward(int i, int dest) throws IOException {
		trainList[i].setDestination(dest);
		trainList[i].setForward();
		int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
		if (i == 0) {
			cmd += Command.TRAIN_MARK_A;
		}
		System.err.println("forward [i=" + i + "], dest=" + dest+", cmd="+cmd);
		for(int j =0; j< stationList.length; j++){
			stationSender[j].writeInt(cmd);
			stationSender[j].flush();			
		}
		//sender[0].flush();
	}

	public void commandBackward(int i, int dest) throws IOException {
		trainList[i].setDestination(dest);
		trainList[i].setBackward();
		int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
		if (i == 0) {
			cmd += Command.TRAIN_MARK_A;
		}
		System.err.println("backward [i=" + i + "], dest=" + dest+", cmd="+(-cmd));
		for(int j =0; j< stationList.length; j++){
			stationSender[j].writeInt(-cmd);
			stationSender[j].flush();
		}
	}

	public void commandSpeedUp(int i) throws IOException {
		int speed = trainList[i].getSpeed() + 1;
		if ((speed >= 1) && (speed <= 7))
			trainList[i].setSpeed(speed);
		if (!trainList[i].isStop()) {
			int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
			if (i == 0) {
				cmd += Command.TRAIN_MARK_A;
			}
			cmd = (trainList[i].isForward() ? cmd : cmd * -1);
			for(int j=0; j < stationList.length; j++){
				stationSender[j].writeInt(cmd);
				stationSender[j].flush();				
			}
		}
	}

	public void commandSpeedDown(int i) throws IOException {
		int speed = trainList[i].getSpeed() - 1;
		if ((speed >= 1) && (speed <= 7))
			trainList[i].setSpeed(speed);
		if (!trainList[i].isStop()) {
			int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
			if (i == 0) {
				cmd += Command.TRAIN_MARK_A;
			}
			cmd = (trainList[i].isForward() ? cmd : cmd * -1);
			for(int j = 0; j< stationList.length; j++){
				stationSender[j].writeInt(cmd);
				stationSender[j].flush();				
			}
		}
	}

	public void commandExit() throws IOException {
		for (int i = 0; i < stationList.length; i++) {
			stationSender[i].writeInt(Command.EXIT);
			stationSender[i].flush();
		}
	}

	public void commandSwitchMain(boolean flag) throws IOException {
		if (flag) {
			stationSender[0].writeInt(Command.SWITCH_MAIN);
			stationSender[0].flush();
		} else {
			stationSender[0].writeInt(Command.SWITCH_BRANCH);
			stationSender[0].flush();
		}
	}

	public int getTrainPos(int i) {
		//if(i >= trainList.length)
		return trainList[i].getPosition();
	}

	public int getTrainSpeed(int i) {
		//if(i >= trainList.length)
		return trainList[i].getSpeed();
	}
	/**
	 * 释放资源
	 */
	public void clean() {
		// TODO 释放资源
		for(int i=0; i< stationSender.length; i++){
			try {
				stationSender[i].close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		for(int i = 0; i < stationReceiver.length; i++){
			try {
				stationReceiver[i].close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		for(int i = 0; i < stationNxtList.length; i++){
			try {
				stationNxtList[i].close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}

	
}
