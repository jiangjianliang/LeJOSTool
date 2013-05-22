package com.wander.train.nxt.cmd;

import com.wander.train.nxt.IrLinkExt;

/**
 * 停止火车的命令
 * 
 * @author wander
 * 
 */
class TrainStopCommand implements Command {
	private static TrainStopCommand instance = null;

	private IrLinkExt link;
	private int channel;

	private TrainStopCommand(IrLinkExt link) {
		this.link = link;
	}

	public static TrainStopCommand getInstance(IrLinkExt link, int channel) {
		if (instance == null) {
			instance = new TrainStopCommand(link);
		}
		instance.setChannel(channel - 1);
		return instance;
	}

	@Override
	public boolean execute() {
		link.sendPFSingleModePWM(channel, TRAIN_PORT, IrLinkExt.PF_PMW_FLOAT);
		return true;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}
}