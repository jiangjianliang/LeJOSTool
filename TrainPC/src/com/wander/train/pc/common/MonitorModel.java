package com.wander.train.pc.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.wander.train.pc.station.StationInfo;
import com.wander.train.pc.station.StationReader;
import com.wander.train.pc.train.HeartBeat;
import com.wander.train.pc.train.TrainInfo;
import com.wander.train.pc.train.TrainReader;

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
	
	private BluetoothReader[] stationReader = new BluetoothReader[STATION_NUM];
	private BluetoothWriter[] stationWriter = new BluetoothWriter[STATION_NUM];

	/*
	 * 假定同方向上有TrainInfo[0]早于TrainInfo[1]， 也就是TrainInfo[0]会更早到达它们的共同站点
	 */
	public TrainInfo[] trainList = new TrainInfo[TRAIN_NUM];
	private NXTComm[] trainNxtList = new NXTComm[TRAIN_NUM];
	private DataOutputStream[] trainSender = new DataOutputStream[TRAIN_NUM];
	private DataInputStream[] trainReceiver = new DataInputStream[TRAIN_NUM];
	
	private BluetoothReader[] trainReader = new BluetoothReader[TRAIN_NUM];
	private BluetoothWriter[] trainWriter = new BluetoothWriter[TRAIN_NUM];
	
	public MonitorModel() {		
		connect();
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
				System.err.println("error while connecting to Station.");
				System.exit(-1);
			}

			if (!total) {
				System.err.println("error while connecting to Station.");
				System.exit(-1);
			}
			
			for (int i = 0; i < stationList.length; i++) {
				stationWriter[i] = new BluetoothWriter(new DataOutputStream(stationNxtList[i].getOutputStream()));
				if(i == SWITCH_INDEX){
					stationList[i] = new StationInfo(this, trainList, true, 0, stationWriter[i]);
				}
				else{
					stationList[i] = new StationInfo(this, trainList, false, 1, stationWriter[i]);
				}
				stationReader[i] = new StationReader(new DataInputStream(stationNxtList[i].getInputStream()), stationList[i]);
				stationReader[i].start();
				new HeartDetector(stationList[i], Config.HeartDetectorPeriod).start();
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
				trainWriter[i] = new BluetoothWriter(new DataOutputStream(trainNxtList[i].getOutputStream()));
				trainList[i] = new TrainInfo(9,0, 0, 1, trainWriter[i]);
				trainReader[i] = new TrainReader(new DataInputStream(trainNxtList[i].getInputStream()), trainList[i]);
				trainReader[i].start();
				new HeartBeat(trainWriter[i], 500).start();
				new HeartDetector(trainList[i], Config.HeartDetectorPeriod).start();
			}
		}
	}
	
	/**
	 * 推动状态机
	 */
	public void push(){
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
	
	public void commandTrainResume(int which){
		if(which >= trainList.length){
			return;
		}
		trainList[which].resume();
	}
	
	public void commandTrainForward(int which, int dest){
		if(which >= trainList.length){
			return;
		}
		//TODO trainList[which].setSpeed(10);
		trainList[which].forward(dest);
	}

	public void commandTrainBackward(int which, int dest){
		if(which >= trainList.length){
			return;
		}
		//TODO trainList[which].setSpeed(10);
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
	 * 给MonitorView测试用
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
		if(i >= trainList.length){
			return 0;
		}
		return trainList[i].getPosition();
	}

	public int getTrainSpeed(int i) {
		if(i >= trainList.length){
			return 0;
		}
		return trainList[i].getSpeed();
	}
	/**
	 * 释放资源
	 */
	public void clean() {
		//释放资源
		/*
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
		*/
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
