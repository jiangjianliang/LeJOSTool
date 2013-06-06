package com.wander.train.pc.common;

import java.io.DataInputStream;
import java.io.IOException;

import com.wander.train.pc.station.StationInfo;
/**
 * 更新"距离"的线程
 * @author wander
 * @deprecated
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
				int result = in.readInt();
				int dis = result/100;
				int color = result%100;
				//System.err.println(dis);
				station.setDistance(dis);
				station.setColor(color);
			} catch (IOException e1) {
				System.exit(-1);
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
