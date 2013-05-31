package com.wander.train.nxt.common;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.LCD;

import com.wander.train.nxt.cmd.Command;

public class CommandExecutor extends Thread {
	
	private ControlData ca;
	private List<Command> cmdList = new ArrayList<Command>();
	
	public CommandExecutor(ControlData ca){
		this.ca = ca;
	}
	
	
	public synchronized void addCommand(Command cmd){
		cmdList.add(cmd);
	}
	
	public synchronized Command removeCommand(){
		return cmdList.remove(0);
	}
	
	@Override
	public void run() {
		while(ca.isKeepOn()){
			if(!cmdList.isEmpty()){
				Command cmd = removeCommand();
				cmd.execute();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		LCD.drawString("exit CommandExecutor", 0, 0);
	}

}
