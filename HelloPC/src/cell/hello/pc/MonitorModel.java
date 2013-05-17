package cell.hello.pc;

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
		trainList = new TrainInfo[1];

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

		// initialize station
		stationList[0] = new StationInfo(255);
		stationList[1] = new StationInfo(255);
	}

	/**
	 * 在DistanceWorker.doInBackground中调用
	 * @return
	 * @throws IOException
	 */
	public boolean update() throws IOException {
		updateDistance();
		for (int i = 0; i < stationList.length; i++) {
			int j = stationList[i].update();
			if (j == 0)
				continue;
			else if (j == 1) { // train enter
				// TODO update train state
				trainList[0].setPosition(3 - 2 * i);
				if (trainList[0].isArrival()) {
					trainList[0].setStop();
					commandStop();
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
		// System.out.println("update distance.");
		sender[0].writeInt(Command.UPDATE_DISTANCE);
		sender[0].flush();
		stationList[0].distance = receiver[0].readInt();
		sender[1].writeInt(Command.UPDATE_DISTANCE);
		sender[1].flush();
		stationList[1].distance = receiver[1].readInt();
	}

	public void commandStop() throws IOException {
		trainList[0].setStop();
		trainList[0].setDestination(trainList[0].getPosition());
		sender[0].writeInt(0);
		sender[0].flush();
	}

	public void commandForward(int des) throws IOException {
		trainList[0].setDestination(des);
		trainList[0].setForward();
		sender[0].writeInt(Command.SPEED_MARK + trainList[0].getSpeed());
		sender[0].flush();
	}

	public void commandBackward(int des) throws IOException {
		trainList[0].setDestination(des);
		trainList[0].setBackward();
		sender[0].writeInt(-Command.SPEED_MARK - trainList[0].getSpeed());
		sender[0].flush();
	}

	public void commandSpeedUp() throws IOException {
		int speed = trainList[0].getSpeed() + 1;
		if ((speed >= 1) && (speed <= 7))
			trainList[0].setSpeed(speed);
		if ( !trainList[0].isStop()) {
			int cmd = Command.SPEED_MARK + trainList[0].getSpeed();
			cmd = (trainList[0].isForward() ? cmd : cmd * -1);
			sender[0].writeInt(cmd);
			sender[0].flush();
		}
	}

	public void commandSpeedDown() throws IOException {
		int speed = trainList[0].getSpeed() - 1;
		if ((speed >= 1) && (speed <= 7))
			trainList[0].setSpeed(speed);
		if ( !trainList[0].isStop()) {
			int cmd = Command.SPEED_MARK + trainList[0].getSpeed();
			cmd = (trainList[0].isForward() ? cmd : cmd * -1);
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

	public int getTrainPos() {
		return trainList[0].getPosition();
	}

	public int getTrainSpeed() {
		return trainList[0].getSpeed();
	}
}
