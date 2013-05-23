package com.wander.train.pc.test;

import java.io.DataInputStream;
import java.io.IOException;

public class Scheduler extends Thread{
	
	private DataInputStream in;
	
	public Scheduler(DataInputStream in){
		this.in = in;
	}
	
	@Override
	public void run() {
		while(true){
			
			try {
				try {
					//System.err.println(in.available());
					System.err.println(in.readInt());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
