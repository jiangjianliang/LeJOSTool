package com.wander.train.pc;

import java.io.DataInputStream;
import java.io.IOException;
/**
 * 更新"距离"的线程
 * @author wander
 *
 */
public class UpdateDistanceThread extends Thread {
	
	private DataInputStream in;
	private StationInfo station;
	public UpdateDistanceThread(DataInputStream in, StationInfo station){
		this.in = in;
		this.station = station;
	}
	@Override
	public void run() {
		while(true){
			try {
				int dis = in.readInt();
				System.err.println(dis);
				station.setDistance(dis);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
