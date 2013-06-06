package com.wander.train.nxt.common;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.LCD;

import com.wander.train.nxt.cmd.Command;

public class CommandExecutor extends Thread {
		
	private ControlData ca;
	private List<Command> cmdList = new ArrayList<Command>();
	private int peroid = 100;
	
	public CommandExecutor(ControlData ca, int period){
		this.ca = ca;
		this.peroid = period;
	}
	
	
	public synchronized void addCommand(Command cmd){
		cmdList.add(cmd);
	}
	
	public synchronized Command removeCommand(){
		return cmdList.remove(0);
	}
	
	public synchronized boolean isEmpty(){
		return cmdList.isEmpty();
	}
	
	@Override
	public void run() {
		while(ca.isKeepOn()){
			if(!isEmpty()){
				Command cmd = removeCommand();
				cmd.execute();
			}
			try {
				Thread.sleep(peroid);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
		LCD.drawString("exit CommandExecutor", 0, 0);
	}

}
