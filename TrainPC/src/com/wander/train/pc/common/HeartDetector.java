package com.wander.train.pc.common;



public class HeartDetector extends Thread {
	
	private HeartDetectable nxt;
	private int period;
	
	public HeartDetector(HeartDetectable nxt, int period){
		this.nxt = nxt;
		this.period = period;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
			
			if(nxt.isBeatFlag()){
				nxt.setBeatFlag(false);
			}
			else{
				System.err.println("心跳检测-未响应");
			}
			
		}
	}

	
	
}
