package com.wander.train.pc;

import java.io.IOException;

public class UpdateDistanceThread extends Thread {
	private MonitorModel mm;
	private int index;
	public UpdateDistanceThread(MonitorModel mm, int i){
		this.mm = mm;
		index = i;
	}
	@Override
	public void run() {
		while(true){
			try {
				int dis = mm.receiver[index].readInt();
				System.err.println(dis);
				mm.setDistance(index, dis);
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
