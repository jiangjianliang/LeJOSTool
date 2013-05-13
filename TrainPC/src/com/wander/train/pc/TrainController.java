package com.wander.train.pc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class TrainController {

	public pcStationState[] stationList;
	public pcTrainState[] trainList;

	public DataOutputStream[] sender;

	// private NXTConnector[] pcNxtList;
	private NXTComm[] pcNxtList;
	private DataInputStream[] receiver;

	public TrainController() {
		init();
	}

	private void init() {
		stationList = new pcStationState[2]; // 0 : sA; 1 : sB;
		trainList = new pcTrainState[1];
		pcNxtList = new NXTComm[2];

		NXTInfo nxt_1 = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT1",
				"00:16:53:09:78:1f");
		NXTInfo nxt_2 = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT2",
				"00:16:53:10:40:a7");
		NXTInfo nxt_3 = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT3",
				"00:16:53:13:3a:1f");

		boolean connected_1 = false;
		boolean connected_2 = false;
		boolean connected_3 = false;

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
		trainList[0] = new pcTrainState();
		trainList[0].port = 0; // Red and port 0
		trainList[0].speed = 3; // speed = 3
		trainList[0].state = 0; // stop
		trainList[0].position = 1; // sA
		trainList[0].destination = 0; // none

		// initialize station
		stationList[0] = new pcStationState(255);
		stationList[1] = new pcStationState(255);
	}

	public void commandForward(int des) throws IOException {
		trainList[0].destination = des;
		trainList[0].state = 1;
		//TODO 以后修改
		sender[0].writeInt(Command.SPEED_UP.ordinal());
		sender[0].flush();
	}

	public void commandBackward(int des) throws IOException {
		trainList[0].destination = des;
		trainList[0].state = -1;
		//TODO 以后修改
		sender[0].writeInt(Command.SLOW_DOWN.ordinal());
		sender[0].flush();
	}

	public void commandStop() throws IOException {
		trainList[0].state = 0;
		trainList[0].destination = trainList[0].position;
		// TODO STOP对应的是0?
		sender[0].writeInt(Command.SLOW_DOWN.ordinal());
		sender[0].flush();
	}

	public boolean update() throws IOException {
		updateDistance();
		for (int i = 0; i < stationList.length; i++) {
			int j = stationList[i].update();
			if (j == 0)
				continue;
			else if (j == 1) { // train enter
				// TODO update train state
				trainList[0].position = 3 - 2 * i;
				if (trainList[0].position == trainList[0].destination) {
					trainList[0].state = 0;
					commandStop();
				}
				return true;
			} else if (j == 2) { // train leave
				// TODO update train state
				if (trainList[0].state == 1) { // forward
					if (i == 0) {
						trainList[0].position = 0;
					} else {
						trainList[0].position = 2;
					}
				} else { // backward
					if (i == 0) {
						trainList[0].position = 2;

					} else {
						trainList[0].position = 0;
					}

				}
				return true;
			}
		}
		return false;
	}

	public void updateDistance() throws IOException {
		// System.out.println("update distance.");

		sender[0].writeInt(Command.WRITE_DISTANCE.ordinal());
		sender[0].flush();
		//TODO distance共享冲突
		stationList[0].distance = receiver[0].readInt();

		sender[1].writeInt(Command.WRITE_DISTANCE.ordinal());
		sender[1].flush();
		// TODO distance共享冲突?
		stationList[1].distance = receiver[1].readInt();
	}

	public int getTrainPos() {
		return trainList[0].position;
	}

}
