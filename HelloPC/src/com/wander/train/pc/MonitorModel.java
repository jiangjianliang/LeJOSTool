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
	public StationInfo[] stationList;
	/*
	 * 假定同方向上有TrainInfo[0]早于TrainInfo[1]，
	 * 也就是TrainInfo[0]会更早到达它们的共同站点
	 */
	public TrainInfo[] trainList;

	public DataOutputStream[] sender;

	//private NXTConnector[] pcNxtList;
	private NXTComm[] pcNxtList;
	private DataInputStream[] receiver;

	public MonitorModel() {
		init();
	}

	private void init() {
		stationList = new StationInfo[2]; // 0 : sA; 1 : sB;
		trainList = new TrainInfo[2];

		boolean connected_1 = false;
		boolean connected_2 = false;
		boolean connected_3 = false;
		
		NXTInfo nxt_1 = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT2",
				"00:16:53:10:40:a7");
		
		NXTInfo nxt_2 = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT3",
				"00:16:53:13:3a:1f");
		
		NXTInfo nxt_3 = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT1",
				"00:16:53:09:78:1f");
		/*
		pcNxtList = new NXTConnector[2];
		// initialize NXT connection
		pcNxtList[0] = new NXTConnector();
		pcNxtList[1] = new NXTConnector();
		connected_1 = pcNxtList[0].connectTo("bt://NXT");
		connected_2 = pcNxtList[1].connectTo("bt://NXT_OF");
		*/
		///*
		pcNxtList = new NXTComm[2];
		try {
			pcNxtList[0] = NXTCommFactory
					.createNXTComm(NXTCommFactory.BLUETOOTH);
			pcNxtList[1] = NXTCommFactory
					.createNXTComm(NXTCommFactory.BLUETOOTH);
			connected_1 = pcNxtList[0].open(nxt_1);
			connected_2 = pcNxtList[1].open(nxt_2);

		} catch (NXTCommException e) {
			e.printStackTrace();
		}
		//*/
		
		if (!connected_1 || !connected_2) {
			System.out.println("error while connection to NXT.");
			System.exit(-1);
		}

		// initialize input and output
		sender = new DataOutputStream[2];
		sender[0] = new DataOutputStream(pcNxtList[0].getOutputStream());
		sender[1] = new DataOutputStream(pcNxtList[1].getOutputStream());
		receiver = new DataInputStream[2];
		receiver[0] = new DataInputStream(pcNxtList[0].getInputStream());
		receiver[1] = new DataInputStream(pcNxtList[1].getInputStream());

		// initialize train
		trainList[0] = new TrainInfo();
		trainList[0] = new TrainInfo();
		
		// initialize station
		stationList[0] = new SwitchStationInfo(this, 255);
		stationList[0].trainList = trainList;
		stationList[1] = new NormalStationInfo(this, 255);
		stationList[1].trainList = trainList;
	}

	/**
	 * 在DistanceWorker.doInBackground中调用
	 *
	 * @return
	 * @throws IOException
	 */
	public boolean update() throws IOException {
		updateDistance();
		//int[] isPassList = new int[2];
		//新的业务逻辑
		for(int i =0; i <stationList.length; i++){
			stationList[i].push();
			//是否经过站台
			//isPassList[i] = stationList[i].updateDistance();
		}
		
			
		for (int i = 0; i < stationList.length; i++) {
			int j = stationList[i].updateDistance();
			
			if (j == 0)
				continue;
			else if (j == 1) { // train enter
				// TODO update train state
				trainList[0].setPosition(3 - 2 * i);
				if (trainList[0].isArrival()) {
					trainList[0].setStop();
					//TODO 需要先写死是哪个停止
					int which = 0;
					commandStop(which);
				}
				return true;
			} else if (j == 2) { // train leave
				// TODO update train state
				if (trainList[0].isForward()) {
					if (i == 0)
						trainList[0].setPosition(0);
					else
						trainList[0].setPosition(2);
				} else { // backward
					if (i == 0)
						trainList[0].setPosition(2);
					else
						trainList[0].setPosition(0);
				}
				return true;
			}
		}
		return false;
	}

	private void updateDistance() throws IOException {
		for (int i = 0; i < stationList.length; i++) {
			sender[i].writeInt(Command.UPDATE_DISTANCE);
			sender[i].flush();
			stationList[i].distance = receiver[i].readInt();
			stationList[i].isIn = receiver[i].readInt();
		}
	}
	
	public void commandStop(int i) throws IOException {
		trainList[i].setStop();
		trainList[i].setDestination(trainList[i].getPosition());
		sender[i].writeInt(0);
		sender[i].flush();
	}

	public void commandForward(int i, int des) throws IOException {
		trainList[i].setDestination(des);
		trainList[i].setForward();
		int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
		if(i == 0){
			cmd +=Command.TRAIN_MARK_A;
		}
		sender[0].writeInt(cmd);
		sender[0].flush();
	}

	public void commandBackward(int i, int des) throws IOException {
		trainList[i].setDestination(des);
		trainList[i].setBackward();
		int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
		if(i == 0){
			cmd +=Command.TRAIN_MARK_A;
		}
		sender[0].writeInt(-cmd);
		sender[0].flush();
	}

	public void commandSpeedUp(int i) throws IOException {
		int speed = trainList[i].getSpeed() + 1;
		if ((speed >= 1) && (speed <= 7))
			trainList[i].setSpeed(speed);
		if ( !trainList[i].isStop()) {
			int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
			if(i == 0){
				cmd +=Command.TRAIN_MARK_A; 
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
		if ( !trainList[i].isStop()) {
			int cmd = Command.SPEED_MARK + trainList[i].getSpeed();
			if(i == 0){
				cmd +=Command.TRAIN_MARK_A;
			}
			cmd = (trainList[i].isForward() ? cmd : cmd * -1);
			sender[0].writeInt(cmd);
			sender[0].flush();
		}
	}

	public void commandExit() throws IOException {
		sender[0].writeInt(Command.EXIT);
		sender[1].writeInt(Command.EXIT);
		sender[0].flush();
		sender[1].flush();
	}

	public void commandSwitchMain(boolean flag) throws IOException {
		if(flag){
			sender[0].writeInt(Command.SWITCH_MAIN);
			sender[0].flush();
		}
		else{
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
