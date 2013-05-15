package cell.hello.pc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

public class pcControlTest {
	public pcStationState[] stationList;
	public pcTrainState[] trainList;
	
	public DataOutputStream[] sender;
	
	private NXTConnector[] pcNxtList;
	private DataInputStream[] receiver;
	
	public pcControlTest() {
		init();
	}
	
	private void init() {
		stationList = new pcStationState[2];	//0 : sA;	1 : sB;
		trainList = new pcTrainState[1];
		pcNxtList = new NXTConnector[2];
		
		//initialize NXT connection
		pcNxtList[0] = new NXTConnector();
		pcNxtList[1] = new NXTConnector();
//		boolean connected_1 = pcNxtList[0].connectTo("usb://NXT_OF");
//		boolean connected_2 = pcNxtList[1].connectTo("usb://NXT");
		boolean connected_1 = pcNxtList[0].connectTo("bt://NXT");
		boolean connected_2 = pcNxtList[1].connectTo("bt://NXT_OF");
		if (!connected_1 || !connected_2) {
			System.out.println("error while connection to NXT.");
			System.exit(-1);
		}
		
		//initialize input and output
		sender = new DataOutputStream[2];
		sender[0] = new DataOutputStream(pcNxtList[0].getOutputStream());
		sender[1] = new DataOutputStream(pcNxtList[1].getOutputStream());
		receiver = new DataInputStream[2];
		receiver[0] = new DataInputStream(pcNxtList[0].getInputStream());
		receiver[1] = new DataInputStream(pcNxtList[1].getInputStream());
		
		//initialize train 
		trainList[0] = new pcTrainState();
		trainList[0].port = 0;			//Red and port 0
		trainList[0].speed = 3;			//speed = 3
		trainList[0].state = 0;			//stop
		trainList[0].position = 1;		//sA
		trainList[0].destination = 0;	//none
		
		//initialize station
		stationList[0] = new pcStationState(255);
		stationList[1] = new pcStationState(255);
	}
	
	public boolean update() throws IOException {
		updateDistance();
		for (int i = 0; i < stationList.length; i++) {
			int j = stationList[i].update();
			if (j == 0)	continue;
			else if (j == 1) {	//train enter
				//TODO update train state
				trainList[0].position = 3 - 2 * i;
				if (trainList[0].position == trainList[0].destination) {
					trainList[0].state = 0;
					commandStop();
				}
				return true;
			} else if (j == 2) {	//train leave
				//TODO update train state
				if (trainList[0].state == 1) {	//forward
					if (i == 0)	trainList[0].position = 0;
					else		trainList[0].position = 2;
				}
				else {	//backward
					if (i == 0)	trainList[0].position = 2;
					else		trainList[0].position = 0;
				}
				return true;
			}
		}
		return false;
	}
	
	public void updateDistance() throws IOException {
		//System.out.println("update distance.");
		sender[0].writeInt(100);
		sender[0].flush();
		stationList[0].distance = receiver[0].readInt();
		sender[1].writeInt(100);
		sender[1].flush();
		stationList[1].distance = receiver[1].readInt();
	}
	
	public void commandStop() throws IOException {
		trainList[0].state = 0;
		trainList[0].destination = trainList[0].position;
		sender[0].writeInt(0);
		sender[0].flush();
	}
	
	public void commandForward(int des) throws IOException {
		trainList[0].destination = des;
		trainList[0].state = 1;
		sender[0].writeInt(1000 + trainList[0].speed);
		sender[0].flush();
	}
	
	public void commandBackward(int des) throws IOException {
		trainList[0].destination = des;
		trainList[0].state = -1;
		sender[0].writeInt(-1000 - trainList[0].speed);
		sender[0].flush();
	}
	
	public void commandSpeedUp() throws IOException {
		int speed = trainList[0].speed + 1;
		if ((speed >= 1) && (speed <= 7))
			trainList[0].speed = speed;
		if (trainList[0].state != 0) {
			int cmd = 1000 + trainList[0].speed;
			cmd = (trainList[0].state > 0 ? cmd : cmd * -1);
			sender[0].writeInt(cmd);
			sender[0].flush();
		}
	}
	
	public void commandSpeedDown() throws IOException {
		int speed = trainList[0].speed - 1;
		if ((speed >= 1) && (speed <= 7))
			trainList[0].speed = speed;
		if (trainList[0].state != 0) {
			int cmd = 1000 + trainList[0].speed;
			cmd = (trainList[0].state > 0 ? cmd : cmd * -1);
			sender[0].writeInt(cmd);
			sender[0].flush();
		}
	}
	
	public void commandExit() throws IOException {
		sender[0].writeInt(-100);
		sender[1].writeInt(-100);
		sender[0].flush();
		sender[1].flush();
	}
	
	public int getTrainPos() {
		return trainList[0].position;
	}
	
	public int getTrainSpeed() {
		return trainList[0].speed;
	}
}








