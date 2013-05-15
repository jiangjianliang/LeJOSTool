package cell.train.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
//import lejos.nxt.comm.USB;

public class nxtMain {
	public static void main(String[] args) {
		LCD.drawString("waiting...", 0, 0);
		
		LCD.drawString("waiting...", 0, 0);
		
		//NXTConnection connection = USB.waitForConnection();
		NXTConnection connection = Bluetooth.waitForConnection();
		DataInputStream pcDin = connection.openDataInputStream();
		DataOutputStream pcDout = connection.openDataOutputStream();
		LCD.drawString("connected.", 0, 0);
		LCD.drawString("distance:", 0, 1);
		LCD.drawString("cmd:", 0, 2);
		
		IrLinkExt link = new IrLinkExt(SensorPort.S1);
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S4);
		
		while (true) {
			try {
				int cmd = pcDin.readInt();
				LCD.drawInt(cmd, 4, 5, 2);
				if (cmd == -100)	//exit
					break;
				else if (cmd == 100) {	//write distance
					int distance = sonic.getDistance();
					//while (distance > 165)	distance = sonic.getDistance();
					LCD.drawInt(distance, 3, 10, 1);
					pcDout.writeInt(distance);
					pcDout.flush();
				} else if (cmd == 0) {
					link.sendPFSingleModePWM(0, 0, 0);
				} else {
					boolean dir = cmd > 0;	//true : forward;	false : backward
					if (!dir)	cmd *= -1;
					int speed = cmd % 1000;
					int cmdB;
					if (dir)	cmdB = speed;
					else		cmdB = 16 - speed;
					link.sendPFSingleModePWM(0, 0, cmdB);
				}
			} catch (IOException e) {
				LCD.drawString("ERROR !", 0, 7);
			}
		}
		try {
			pcDin.close();
			pcDout.close();
			connection.close();
		} catch (IOException e) {
			LCD.drawString("CLOSE ERROR.", 0, 7);
		}
		LCD.drawString("good bye", 5, 4);
		Button.waitForAnyPress();
	}
}
















