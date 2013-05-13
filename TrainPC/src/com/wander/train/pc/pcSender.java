package com.wander.train.pc;

import java.io.DataOutputStream;
import java.io.IOException;

public class pcSender {
	
	private DataOutputStream dos;
	
	public pcSender(DataOutputStream dos) {
		this.dos = dos;
	}
	
	//@param(cmd) : stop-0; forward-1; backward-2; speed-3; exit-4; get_distance-5;
	public void send(pcTrainState train, int cmd) {
		try {
			int msg = 0;
			dos.writeInt(msg);
			dos.flush();
		} catch (IOException e) {
			System.out.println("error while send message.");
		}
	}
}