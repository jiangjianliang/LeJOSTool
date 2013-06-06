package com.wander.train.nxt.common;

import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.LCD;

import com.wander.train.nxt.cmd.Command;
import com.wander.train.nxt.cmd.CommandFactory;

public class CommandReceiver extends Thread{
	
	private DataInputStream in;
	private ControlData ca;
	private CommandFactory cmdFactory;
	private CommandExecutor cmdExecutor;
	
	public CommandReceiver(DataInputStream in, ControlData ca, CommandFactory cmdFactory, CommandExecutor cmdExecutor){
		this.in = in;
		this.ca = ca;
		this.cmdFactory = cmdFactory;
		this.cmdExecutor = cmdExecutor;
	}
	
	@Override
	public void run() {
		
		while(ca.isKeepOn()){
			int cmd;
			try {
				cmd = in.readInt();
				if(cmd != Config.HEART_BEAT){
					LCD.drawInt(cmd, 6, 5, 3);					
				}
				Command concreteCommand = cmdFactory.parseCommand(cmd);
				cmdExecutor.addCommand(concreteCommand);
			} catch (IOException e) {
				LCD.drawString("ERROR READ!", 0, 7);
				//TODO 以后可能要注释掉
				break;
			}
		}
		LCD.drawString("exit CommandReceiver", 0, 1);
	}
}
