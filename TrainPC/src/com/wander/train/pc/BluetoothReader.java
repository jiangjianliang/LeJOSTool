package com.wander.train.pc;

import java.io.DataInputStream;
import java.io.IOException;
/**
 * 从蓝牙连接中读取数据
 * @author wander
 *
 */
public class BluetoothReader extends Thread {
	
	private DataInputStream in;
	private StationInfo station;
	
	
	public BluetoothReader(DataInputStream in, StationInfo station){
		this.in = in;
		this.station = station;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				int result = in.readInt();
				if(result == Config.HEARTBEAT){
					station.setBeatFlag(true);
				}
				else{
					int dis = result/100;
					int color = result%100;
					station.setDistance(dis);
					station.setColor(color);					
				}
				//System.err.println(dis);
			} catch (IOException e1) {
				System.exit(-1);
			}
			//TODO 有这个必要吗
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
