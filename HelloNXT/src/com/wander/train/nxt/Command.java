package com.wander.train.nxt;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

/**
 * 命令接口
 * 
 * @author wander
 * 
 */
public interface Command {

	// 程序退出
	final static int EXIT = -100;
	// 火车停止
	final static int TRAIN_STOP_A = 1;
	final static int TRAIN_STOP_B = -1;

	final static int UPDATE_ARRIVAL = 0;
	// 换轨道
	final static int SWITCH_MAIN = 2;
	final static int SWITCH_BRANCH = -2;

	// 要求发送距离
	final static int UPDATE_DISTANCE = 100;

	// 速度标记
	final static int SPEED_MARK = 1000;
	// 火车标记
	final static int TRAIN_MARK_A = 10000;

	// 控制类的常量
	final static int TRAIN_PORT = IrLinkExt.PF_SINGLE_MODE_BLUE_PORT;

	/**
	 * 执行命令
	 * 
	 * @return 执行结束是否成功，false时需要退出程序
	 */
	public boolean execute();

}

/**
 * 退出程序的命令
 * 
 * @author wander
 * 
 */
class ExitCommand implements Command {
	private static ExitCommand instance = new ExitCommand();

	private ExitCommand() {
	}

	public static ExitCommand getInstance() {
		return instance;
	}

	@Override
	public boolean execute() {
		return false;
	}
}

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

/**
 * 更新距离的命令
 * 
 * @author wander
 * 
 */
class UpdateDistanceCommand implements Command {
	private static UpdateDistanceCommand instance = null;

	private UltrasonicSensor sonic;
	private DataOutputStream out;

	private UpdateDistanceCommand(UltrasonicSensor sonic, DataOutputStream out) {
		this.sonic = sonic;
		this.out = out;
	}

	public static UpdateDistanceCommand getInstance(UltrasonicSensor sonic,
			DataOutputStream out) {
		if (instance == null) {
			instance = new UpdateDistanceCommand(sonic, out);
		}
		return instance;
	}

	@Override
	public boolean execute() {
		int distance = sonic.getDistance();
		LCD.drawInt(distance, 3, 10, 1);
		try {
			out.writeInt(distance);
			// out.writeInt(isPressed);
			out.flush();
		} catch (IOException e) {
			LCD.drawString("ERROR WRITE!", 0, 3);
		}
		return true;
	}
}

/**
 * 变轨命令
 * 
 * @author wander
 * 
 */
class SwitchCommand implements Command {
	private static SwitchCommand instance = new SwitchCommand();

	private boolean switchToMain = false;

	private static int DEGREE = 35;

	private SwitchCommand() {
	}

	public static SwitchCommand getInstance(boolean isMain) {
		instance.setSwitchMain(isMain);
		return instance;
	}

	@Override
	public boolean execute() {
		if (switchToMain) {
			LCD.drawString("switch main  ", 0, 7);
			Motor.A.setSpeed(DEGREE * 3);
			Motor.A.rotate(-DEGREE - 15);
			Motor.A.rotate(5);
		} else {
			LCD.drawString("switch branch", 0, 7);
			Motor.A.setSpeed(DEGREE * 3);
			Motor.A.rotate(DEGREE + 10);
		}
		return true;
	}

	public void setSwitchMain(boolean switchMain) {
		this.switchToMain = switchMain;
	}

}

/**
 * 到站命令
 */
class ArriveCommand implements Command {
	private static ArriveCommand instance = null;

	private TouchSensor touch;
	private DataOutputStream out;

	private ArriveCommand(TouchSensor touch, DataOutputStream out) {
		this.touch = touch;
		this.out = out;
	}

	public static ArriveCommand getInstance(TouchSensor touch, DataOutputStream out) {
		if (instance == null) {
			instance = new ArriveCommand(touch, out);
		}
		return instance;
	}

	@Override
	public boolean execute() {
		boolean isPressed = touch.isPressed();
		try {
			if (isPressed) {
				out.writeInt(1);
				out.flush();
			} else {
				out.writeInt(0);
				out.flush();
			}
		} catch (IOException e) {
			LCD.drawString("ERROR WRITE!", 0, 3);
		}
		return true;
	}

}