package com.wander.train.pc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
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
				//e.printStackTrace();
				System.err.println("error while connecting to Station.");
				System.exit(-1);
			}

			if (!total) {
				System.err.println("error while connecting to Station.");
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
					System.err.println(nxts[TRAIN_NXT_START+i].deviceAddress);
					trainConnected[i] = trainNxtList[i].open(nxts[TRAIN_NXT_START+i]);
					total = total && trainConnected[i];
				}
			} catch(NXTCommException e){
				//e.printStackTrace();
				System.err.println("error while connecting to Train.");
				System.exit(-1);
			}
			
			if(!total){
				System.err.println("error while connecting to Train.");
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
			trainList[i] = new TrainInfo(7,0, 0, 1, trainSender[i]);
		}

		//trainList[0] = new TrainInfo(5, 0, 0,1);
		//trainList[1] = new TrainInfo(5, 0, 2,0);
		
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
			new HeartDetector(stationList[i], Config.HeartDetectorPeriod).start();
			new BluetoothReader(stationReceiver[i], stationList[i]).start();
			//new UpdateDistanceThread(stationReceiver[i], stationList[i]).start();
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
	
	/**
	 * 开始运行命令
	 */
	public void commandStart() {
		//TODO 以后修改
		for(int i =0; i< stationList.length; i++){
			stationList[i].resetState();
			stationList[i].start();
		}
		
		for(int i=0; i < trainList.length; i++){
			trainList[i].start();
		}
	}
	/**
	 * 停止火车命令
	 * @param which
	 */
	public void commandTrainStop(int which) {
		if(which >= trainList.length){
			return;
		}
		trainList[which].stop();
	}
	
	public void commandTrainForward(int which, int dest){
		if(which >= trainList.length){
			return;
		}
		trainList[which].setSpeed(10);
		trainList[which].forward(dest);
	}

	public void commandTrainBackward(int which, int dest){
		if(which >= trainList.length){
			return;
		}
		trainList[which].setSpeed(10);
		trainList[which].backward(dest);
	}
	
	public void commandTrainSlowDown(int which, int delta){
		if( which>= trainList.length){
			return;
		}
		trainList[which].slowDown(delta);
	}
	
	public void commandTrainSpeedUp(int which, int delta){
		if( which>= trainList.length){
			return;
		}
		trainList[which].speedUp(delta);
	}
	
	/**
	 * 退出程序命令
	 * @throws IOException
	 */
	public void commandExit() {
		for(int i=0; i < stationList.length; i++){
			stationList[i].exit();
		}
		for(int i=0; i < trainList.length; i++){
			trainList[i].exit();
		}
	}
	/**
	 * 换轨命令
	 * @param flag
	 */
	public void commandStationSwitch(boolean flag){
		if(flag){
			stationList[SWITCH_INDEX].switchMain();
		}
		else{
			stationList[SWITCH_INDEX].switchBranch();
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
		//释放资源
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
		
		for(int i=0; i < trainSender.length; i++){
			try {
				trainSender[i].close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		for(int i=0; i < trainReceiver.length; i++){
			try {
				trainReceiver[i].close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		for(int i=0; i < trainNxtList.length; i++){
			try {
				trainNxtList[i].close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		
	}

	
}
