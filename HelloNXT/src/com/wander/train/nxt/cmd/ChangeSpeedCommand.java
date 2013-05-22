package com.wander.train.nxt.cmd;

import lejos.nxt.LCD;

import com.wander.train.nxt.IrLinkExt;


/**
 * 更改速度的命令
 * 
 * @author wander
 * 
 */
class ChangeSpeedCommand implements Command {
	private static ChangeSpeedCommand instance = null;

	private int cmd;
	private IrLinkExt link;
	private int channel;

	private ChangeSpeedCommand(int cmd, IrLinkExt link) {
		this.cmd = cmd;
		this.link = link;
	}

	public static ChangeSpeedCommand getInstance(int cmd, IrLinkExt link,
			int channel) {
		if (instance == null) {
			instance = new ChangeSpeedCommand(cmd, link);
		}
		instance.setCmd(cmd);
		instance.setChannel(channel - 1);
		return instance;
	}

	@Override
	public boolean execute() {
		LCD.drawString(channel + "] speed ", 0, 7);
		LCD.drawInt(cmd, 2, 9, 7);
		link.sendPFSingleModePWM(channel, TRAIN_PORT, cmd);
		return true;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

}

