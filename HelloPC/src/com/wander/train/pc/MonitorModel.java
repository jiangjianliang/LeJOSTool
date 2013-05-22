package com.wander.train.pc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

public class MonitorModel {
	
	//配置参数 
	/**
	 * 站点数量
	 */
	private static int STATION_NUM = 2;
	/**
	 * 火车数量
	 */
	private static int TRAIN_NUM = 2;
	/**
	 * SWITCH站点的编号
	 */
	private static int SWITCH_INDEX  = 0;
	/**
	 * 连接方式
	 * 0 USB, 1 BLUETOOTH
	 */
	private static int CONN_TYPE = 1;
	/**
	 * NXT的Mac地址
	 */
	private NXTInfo[] nxts = new NXTInfo[3];
	{
		nxts[0] = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT1",
			"00:16:53:10:40:a7");
		nxts[1] = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT2",
			"00:16:53:13:3a:1f");
		nxts[2] = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT3",
			"00:16:53:09:78:1f");
	}
	
	//实例成员
	
	public StationInfo[] stationList;
	public DataOutputStream[] sender;
	private DataInputStream[] receiver;
	
	// private NXTConnector[] pcNxtList;
	private NXTComm[] pcNxtList;

	/*
	 * 假定同方向上有TrainInfo[0]早于TrainInfo[1]， 也就是TrainInfo[0]会更早到达它们的共同站点
	 */
	public TrainInfo[] trainList;
	
	
	public MonitorModel() {
		init();
	}

	private void init() {
		
		stationList = new StationInfo[STATION_NUM]; // 0 : sA; 1 : sB;
		trainList = new TrainInfo[TRAIN_NUM];
		
		sender = new DataOutputStream[STATION_NUM];
		receiver = new DataInputStream[STATION_NUM];
		
		// initialize train
		for(int i=0; i < trainList.length; i++){
			trainList[i] = new TrainInfo();
		}

		// initialize station
		for(int i=0; i < stationList.length; i++){
			if(i == SWITCH_INDEX){
				stationList[i] = new StationInfo(this, 255, true);
			}
			else{
				stationList[i] = new StationInfo(this, 255, false);
			}
			stationList[i].trainList = trainList;
		}
	}

	private void connect(){
		boolean[] connected = new boolean[stationList.length];
		
		/*
		 * pcNxtList = new NXTConnector[2]; // initialize NXT connection
		 * pcNxtList[0] = new NXTConnector(); pcNxtList[1] = new NXTConnector();
		 * connected_1 = pcNxtList[0].connectTo("bt://NXT"); connected_2 =
		 * pcNxtList[1].connectTo("bt://NXT_OF");
		 */
		
		pcNxtList = new NXTComm[STATION_NUM];
		
		// initialize input and output
		if( CONN_TYPE == 0){
			
		}
		else if(CONN_TYPE == 1){
			boolean total = true;
			try {
				for(int i = 0; i < stationList.length; i++){
					pcNxtList[i] = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
					connected[i] = pcNxtList[i].open(nxts[i]);
					total = total && connected[i];
				}

			} catch (NXTCommException e) {
				e.printStackTrace();
			}

			if (!total) {
				System.err.println("error while connection to NXT.");
				System.exit(-1);
			}
			
			for (int i = 0; i < stationList.length; i++) {
				sender[i] = new DataOutputStream(pcNxtList[i].getOutputStream());
				receiver[i] = new DataInputStream(pcNxtList[i].getInputStream());
			}
		}
	}
	
	/**
	 * 在DistanceWorker.doInBackground中调用
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean update() throws IOException {
		updateDistance();
		//TODO test
		for (int i = 0; i < stationList.length; i++) {
			stationList[i].push();
		}
		//stationList[0].push();
		return true;
	}

	private void updateDistance() throws IOException {
		for (int i = 0; i < stationList.length; i++) {
			try {
				sender[i].writeInt(Command.UPDATE_DISTANCE);
				sender[i].flush();
				stationList[i].distance = receiver[i].readInt();
				//TODO test
				System.err.println(i+"]"+stationList[i].distance);
			} catch (IOException e) {
				// TODO do nothing
				System.err.println(i+" update distance");
			}
		}
	}

	public void commandStop(int i) throws IOException {
		trainList[i].setStop();
		// trainList[i].setDestination(trainList[i].getPosition());
		if (i == 0) {
			sender[0].writeInt(Command.TRAIN_STOP_A);
		} else {
			sender[0].writeInt(Command.TRAIN_STOP_B);
		}
		sender[0].flush();
	}

	public void commandForward(int i, int dest) throws IOException {
		System.err.println("forward [" + i + "]," + dest);
		trainList[i].setDestination(dest);
		trainList[i].setForward();
		int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
		if (i == 0) {
			cmd += Command.TRAIN_MARK_A;
		}
		System.err.println(cmd);
		sender[0].writeInt(cmd);
		sender[0].flush();
	}

	public void commandBackward(int i, int dest) throws IOException {
		System.err.println("backward [" + i + "]," + dest);
		trainList[i].setDestination(dest);
		trainList[i].setBackward();
		int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
		if (i == 0) {
			cmd += Command.TRAIN_MARK_A;
		}
		System.err.println(-cmd);
		sender[0].writeInt(-cmd);
		sender[0].flush();
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
			sender[0].writeInt(cmd);
			sender[0].flush();
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
			sender[0].writeInt(cmd);
			sender[0].flush();
		}
	}

	public void commandExit() throws IOException {
		for (int i = 0; i < stationList.length; i++) {
			sender[i].writeInt(Command.EXIT);
			sender[i].flush();
		}
	}

	public void commandSwitchMain(boolean flag) throws IOException {
		if (flag) {
			sender[0].writeInt(Command.SWITCH_MAIN);
			sender[0].flush();
		} else {
			sender[0].writeInt(Command.SWITCH_BRANCH);
			sender[0].flush();
		}
	}

	public int getTrainPos(int i) {
		return trainList[i].getPosition();
	}

	public int getTrainSpeed(int i) {
		return trainList[i].getSpeed();
	}
}
