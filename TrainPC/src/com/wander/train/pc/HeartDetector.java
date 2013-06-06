package com.wander.train.pc;

public class HeartDetector extends Thread {
	
	private StationInfo station;
	private int period;
	
	public HeartDetector(StationInfo station, int period){
		this.station = station;
		this.period = period;
	}
	
	@Override
	public void run() {
		while(true){
			if(station.isBeatFlag()){
				station.setBeatFlag(false);
			}
			else{
				System.err.println("心跳检测-未响应");
			}
			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}

	
	
}
