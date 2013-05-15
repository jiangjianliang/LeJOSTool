package cell.hello.pc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

public class pcControl {
	public pcStationState[] stationList;
	public pcTrainState[] trainList;
	
	public pcSender[] sender;
	
	private NXTConnector[] pcNxtList;
	private ultrasonicThread[] receiver;
	private Thread[] nxtThread;
	
	public pcControl() {
		init();
	}
	
	private void init() {
		stationList = new pcStationState[2];	//0 : sA;	1 : sB;
		trainList = new pcTrainState[1];
		pcNxtList = new NXTConnector[2];
		
		//initialize NXT connection
		pcNxtList[0] = new NXTConnector();
		pcNxtList[1] = new NXTConnector();
		boolean connected_1 = pcNxtList[0].connectTo("usb://NXT");
		boolean connected_2 = pcNxtList[1].connectTo("usb://NXT_OF");
		if (!connected_1 || !connected_2) {
			System.out.println("error while connection to NXT.");
			System.exit(-1);
		}
		
		//initialize input and output
		sender = new pcSender[2];
		sender[0] = new pcSender(new DataOutputStream(pcNxtList[0].getOutputStream()));
		sender[1] = new pcSender(new DataOutputStream(pcNxtList[1].getOutputStream()));
		receiver = new ultrasonicThread[2];
		receiver[0] = new ultrasonicThread(0, new DataInputStream(pcNxtList[0].getInputStream()));
		receiver[1] = new ultrasonicThread(1, new DataInputStream(pcNxtList[1].getInputStream()));
		nxtThread = new Thread[2];
		nxtThread[0] = new Thread(receiver[0]);
		nxtThread[1] = new Thread(receiver[1]);
		nxtThread[0].start();
		nxtThread[1].start();
		
		//initialize train 
		trainList[0].port = 0;
		trainList[0].speed = 3;
		trainList[0].state = 0;
		trainList[0].position = 1;
		trainList[0].destination = 0;
	}
	
	public boolean update() {
		for (int i = 0; i < stationList.length; i++) {
			int j = stationList[i].update();
			if (j == 0)	continue;
			else if (j == 1) {	//train enter
				//TODO update train state
				trainList[0].position = 2 * i + 1;
				if (trainList[0].position == trainList[0].destination) {
					trainList[0].state = 0;
					commandStop();
				}
				return true;
			} else if (j == 2) {	//train leave
				//TODO update train state
				if (trainList[0].state == 1) {	//forward
					trainList[0].position = (i<<1 + 2) % 4;
				} else if (trainList[0].state == 2) {	//backward
					trainList[0].position = i<<1;
				}
				return true;
			}
		}
		return false;
	}
	
	public void commandStop() {
		trainList[0].state = 0;
		trainList[0].destination = trainList[0].position;
		sender[0].send(trainList[0], 0);
	}
	
	public void commandForward(int des) {
		trainList[0].destination = des;
		trainList[0].state = 1;
		sender[0].send(trainList[0], 1);
	}
	
	public void commandBackward(int des) {
		trainList[0].destination = des;
		trainList[0].state = -1;
		sender[0].send(trainList[0], 2);
	}
	
	public void commandSpeedUp() {
		int speed = trainList[0].speed ++;
		if (speed >= 1 && speed <= 7)
			trainList[0].speed = speed;
		if (trainList[0].state != 0)
			sender[0].send(trainList[0], 3);
	}
	
	public void commandSpeedDown() {
		int speed = trainList[0].speed --;
		if (speed >= 1 && speed <= 7)
			trainList[0].speed = speed;
		if (trainList[0].state != 0)
			sender[0].send(trainList[0], 3);
	}
	
	class ultrasonicThread implements Runnable {
		
		private int stationIndex;
		private DataInputStream distanceStream;
		
		public ultrasonicThread(int index, DataInputStream dis) {
			stationIndex = index;
			distanceStream = dis;
		}
		
		@Override
		public void run() {
			try {
				stationList[stationIndex].distance = distanceStream.readInt();
			} catch (IOException e) {
				System.err.println("error while get distance ! index : " + stationIndex);
			}
		}
		
	}
}








