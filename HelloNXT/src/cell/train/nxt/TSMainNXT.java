package cell.train.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cell.nxt.train.IrLinkExt;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

public class TSMainNXT {
	public static final int NXT_TRAIN_READY = 0;
	public static final int NXT_TRAIN_EXIT = -1;
	
	public static final int TRAIN_POS_MIDDLE = 0;
	public static final int TRAIN_POS_STATION_A = 1;
	public static final int TRAIN_POS_STATION_B = 2;
	public static final int TRAIN_STATE_RUN = 1;
	public static final int TRAIN_STATE_STOP = 0;
	
	public static boolean exit = false;
	public static boolean trueExit = false;
	public static boolean arriveSend = true;
	public static int station1Dis = 255;
	public static int station2Dis = 255;
	public static int trainState = TRAIN_STATE_STOP;
	public static int trainPos;
	public static int trainDesPos;
	
	public static void main(String[] args) throws Exception {
		trainState = TRAIN_STATE_STOP;
		trainPos = TRAIN_POS_STATION_A;		trainDesPos = TRAIN_POS_STATION_A;
		exit = false;	arriveSend = true;		station1Dis = 255;	station2Dis = 255;
		
		IrLinkExt link = new IrLinkExt(SensorPort.S1);
		UltrasonicSensor station1 = new UltrasonicSensor(SensorPort.S4);
		LCD.drawString("Sensor connected", 0, 0);
		 
		NXTConnection usbc = USB.waitForConnection();
		DataInputStream PCdis = usbc.openDataInputStream();
		DataOutputStream PCdos = usbc.openDataOutputStream();
		LCD.drawString("PC USB connected", 0, 1);
		
		NXTConnection btc = Bluetooth.waitForConnection();
		DataInputStream NXTdis = btc.openDataInputStream();
		DataOutputStream NXTdos = btc.openDataOutputStream();
		LCD.drawString("NXT BT connected", 0, 2);
		
		PCListener pcListener = new PCListener(PCdis);
		StateListener stateListener = new StateListener(NXTdis, PCdos, NXTdos);
		
		Thread pcThread = new Thread(pcListener);
		Thread stateThread = new Thread(stateListener);
		
		pcThread.start();
		stateThread.start();
		 
		LCD.clear();
		LCD.drawString("Sensor data", 0, 0);
		LCD.drawString("T D:  P:  S: ", 0, 1);
		LCD.drawString("TS1 dis:", 0, 2);
		LCD.drawString("TS2 dis:", 0, 3);
		while (!trueExit) {
			station1Dis = station1.getDistance();
			LCD.drawInt(trainDesPos, 1, 5, 1);
			LCD.drawInt(trainPos, 1, 9, 1);
			LCD.drawInt(trainState, 1, 13, 1);
			LCD.drawInt(station1Dis, 3, 9, 2);
			LCD.drawInt(station2Dis, 3, 9, 3);
			
			if (station1Dis < 10)
				trainPos = TRAIN_POS_STATION_A;
			else if (station2Dis < 10)
				trainPos = TRAIN_POS_STATION_B;
			else
				trainPos = TRAIN_POS_MIDDLE;
			
			if (trainDesPos == TRAIN_STATE_STOP || trainPos == trainDesPos) {
				trainState = TRAIN_STATE_STOP;
				LCD.drawString("STOP", 0, 7);
				link.sendPFSingleModePWM(IrLinkExt.PF_Channel_1, IrLinkExt.PF_SINGLE_MODE_RED_PORT,IrLinkExt.PF_PMW_FLOAT);
			}
			
			if (trainDesPos == TRAIN_POS_STATION_A && trainPos != TRAIN_POS_STATION_A) {
				trainState = TRAIN_STATE_RUN;
				LCD.drawString("TO A", 0, 7);
				link.sendPFSingleModePWM(IrLinkExt.PF_Channel_1, IrLinkExt.PF_SINGLE_MODE_RED_PORT,IrLinkExt.PF_PMW_REV_4);
			}
			
			if (trainDesPos == TRAIN_POS_STATION_B && trainPos != TRAIN_POS_STATION_B) {
				trainState = TRAIN_STATE_RUN;
				LCD.drawString("TO B", 0, 7);
				link.sendPFSingleModePWM(IrLinkExt.PF_Channel_1, IrLinkExt.PF_SINGLE_MODE_RED_PORT,IrLinkExt.PF_PMW_FWD_4);
			}
			
			Thread.yield();
		}
		
		PCdis.close();
		PCdos.close();
		usbc.close();
		
		NXTdis.close();
		NXTdos.close();
		btc.close();
	}
}

class PCListener implements Runnable {
	private DataInputStream dis;
	
	public PCListener(DataInputStream dis) {
		this.dis = dis;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				int cmd = dis.readInt();
				if (cmd == TSMainNXT.NXT_TRAIN_EXIT) {
					TSMainNXT.exit = true;
					break;
				}
				TSMainNXT.trainDesPos = cmd;
				Thread.yield();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}

class StateListener implements Runnable {
	private DataInputStream dis;
	private DataOutputStream PCdos;
	private DataOutputStream NXTdos;
	
	public StateListener(DataInputStream dis, DataOutputStream PCdos, DataOutputStream NXTdos) {
		this.dis = dis;
		this.PCdos = PCdos;
		this.NXTdos = NXTdos;
	}

	@Override
	public void run() {
		while(true) {
			try {
				if (TSMainNXT.exit)	{
					PCdos.writeInt(TSMainNXT.NXT_TRAIN_EXIT);
					LCD.drawString("sended", 0, 7);
					Button.waitForAnyPress();
					NXTdos.writeInt(TSMainNXT.NXT_TRAIN_EXIT);
					TSMainNXT.trueExit = true;
					break;
				}
				else	NXTdos.writeInt(TSMainNXT.NXT_TRAIN_READY);
				NXTdos.flush();
				LCD.drawString("NXT w", 0, 6);
				TSMainNXT.station2Dis = dis.readInt();
				if (TSMainNXT.trainPos == TSMainNXT.trainDesPos && TSMainNXT.arriveSend) {
					TSMainNXT.arriveSend = false;
					PCdos.writeInt(1000 * TSMainNXT.trainState + TSMainNXT.trainPos);
					PCdos.flush();
				} else {
					TSMainNXT.arriveSend = true;
				}
				LCD.drawString("PC w", 8, 6);
				Thread.yield();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}






















