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
	public DataOutputStream[] sender;
	private DataInputStream[] receiver;
	
	/*
	 * 假定同方向上有TrainInfo[0]早于TrainInfo[1]， 也就是TrainInfo[0]会更早到达它们的共同站点
	 */
	public TrainInfo[] trainList;


	// private NXTConnector[] pcNxtList;
	private NXTComm[] pcNxtList;

	public MonitorModel() {
		init();
	}

	private void init() {
		//TODO test
		//stationList = new StationInfo[1];
		stationList = new StationInfo[2]; // 0 : sA; 1 : sB;
		trainList = new TrainInfo[1];
		//trainList = new TrainInfo[2];

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
		 * pcNxtList = new NXTConnector[2]; // initialize NXT connection
		 * pcNxtList[0] = new NXTConnector(); pcNxtList[1] = new NXTConnector();
		 * connected_1 = pcNxtList[0].connectTo("bt://NXT"); connected_2 =
		 * pcNxtList[1].connectTo("bt://NXT_OF");
		 */
		// /*
		pcNxtList = new NXTComm[2];
		try {
			pcNxtList[0] = NXTCommFactory
					.createNXTComm(NXTCommFactory.BLUETOOTH);
			connected_1 = pcNxtList[0].open(nxt_1);

			connected_2 = true;

			pcNxtList[1] = NXTCommFactory
					.createNXTComm(NXTCommFactory.BLUETOOTH);
			connected_2 = pcNxtList[1].open(nxt_2);

		} catch (NXTCommException e) {
			e.printStackTrace();
		}
		// */

		if (!connected_1 || !connected_2) {
			System.err.println("error while connection to NXT.");
			System.exit(-1);
		}

		// initialize input and output
		sender = new DataOutputStream[stationList.length];
		receiver = new DataInputStream[stationList.length];

		for (int i = 0; i < stationList.length; i++) {
			sender[i] = new DataOutputStream(pcNxtList[i].getOutputStream());
			receiver[i] = new DataInputStream(pcNxtList[i].getInputStream());
		}

		// initialize train
		trainList[0] = new TrainInfo();
		//trainList[1] = new TrainInfo();

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
