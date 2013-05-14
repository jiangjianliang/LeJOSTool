package com.wander.train.nxt;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class Station {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LCD.drawString("waiting...", 0, 0);
		
		//建立bluetooth连接
		NXTConnection nxtCon = Bluetooth.waitForConnection();
		DataInputStream dis = nxtCon.openDataInputStream();
		DataOutputStream dos = nxtCon.openDataOutputStream();
		
		LCD.drawString("connected.", 0, 0);
		LCD.drawString("distance:", 0, 1);
		LCD.drawString("cmd:", 0, 2);
		
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S4);
		
		boolean exitFlag = false;
		while(true){
			try {
				//TODO readInt是否为阻塞的
				if(exitFlag){
					break;
				}
				int cmd = dis.readInt();
				LCD.drawString("debug", 0,4);
				LCD.drawString(String.valueOf(cmd), 0, 5);
				//会报告异常
				Command command = Command.values()[cmd];
				switch(command){
				case EXIT:
					exitFlag = true;
					break ;
				case WRITE_DISTANCE:
					dos.writeInt(sonic.getDistance());
					dos.flush();
					
					break;
				default:
					LCD.drawString("UNKNOW COMMAND!", 0, 6);
				}
			} catch (IOException e) {
				LCD.drawString("ERROR !", 0, 7);
			}
			
		}
		//释放资源
		try{
			dis.close();
			dos.close();
			nxtCon.close();
		}
		catch(Exception e){
			LCD.drawString("CLOSE ERROR.", 0, 7);
		}
		
		LCD.drawString("good bye", 5, 4);
		Button.waitForAnyPress();

	}

}
