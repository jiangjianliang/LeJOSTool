package com.wander.train.nxt.cmd.station;

import com.wander.train.nxt.cmd.Command;
import com.wander.train.nxt.common.ControlData;

public class StartCommand implements Command {
	private static StartCommand instance = new StartCommand();
	
	private ControlData ca;
	
	private StartCommand(){
		
	}
	
	public static StartCommand getInstance(ControlData ca){
		instance.setCa(ca);
		return instance;
	}
	
	public void setCa(ControlData ca) {
		this.ca = ca;
	}
	
	@Override
	public boolean execute() {
		ca.setStart(true);
		return true;
	}

	

}
