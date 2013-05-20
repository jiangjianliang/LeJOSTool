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
	//TODO 换轨道
	final static int SWITCH_MAIN = 2;
	final static int SWITCH_BRANCH = -2;
	
	// 要求发送距离
	final static int UPDATE_DISTANCE = 100;
	
	
	
	// 速度标记
	final static int SPEED_MARK = 1000;
	// 火车标记
	final static int TRAIN_MARK_A = 10000;
	
	/**
	 * 执行命令
	 * @return 执行结束是否成功，false时需要退出程序
	 */
	public boolean execute();

}
/**
 * 退出程序的命令
 * @author wander
 *
 */
class ExitCommand implements Command {
	private static ExitCommand instance = new ExitCommand();
	private ExitCommand() {
	}

	public static ExitCommand getInstance(){
		return instance;
	}
	
	@Override
	public boolean execute() {
		return false;
	}
}

/**
 * 停止火车的命令
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
	
	public static TrainStopCommand getInstance(IrLinkExt link, int channel){
		if(instance == null){
			instance = new TrainStopCommand(link);
		}
		instance.setChannel(channel);
		return instance;
	}
	
	@Override
	public boolean execute() {
		link.sendPFSingleModePWM(channel,
				IrLinkExt.PF_SINGLE_MODE_RED_PORT, IrLinkExt.PF_PMW_FLOAT);
		return true;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}
}


/**
 * 更改速度的命令
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
	
	public static ChangeSpeedCommand getInstance(int cmd, IrLinkExt link, int channel){
		if(instance == null){
			instance = new ChangeSpeedCommand(cmd, link);
		}
		instance.setChannel(channel);
		return instance;
	}
	
	@Override
	public boolean execute() {
		link.sendPFSingleModePWM(channel,
				IrLinkExt.PF_SINGLE_MODE_RED_PORT, cmd);
		return true;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

}

/**
 * 更新距离的命令
 * @author wander
 *
 */
class UpdateDistanceCommand implements Command {
	private static UpdateDistanceCommand instance = null;
	
	private UltrasonicSensor sonic;
	private DataOutputStream out;
	private TouchSensor touch;
	
	private UpdateDistanceCommand(UltrasonicSensor sonic, DataOutputStream out, TouchSensor touch) {
		this.sonic = sonic;
		this.out = out;
		this.touch = touch;
	}

	public static UpdateDistanceCommand getInstance(UltrasonicSensor sonic, DataOutputStream out, TouchSensor touch){
		if(instance == null){
			instance = new UpdateDistanceCommand(sonic, out, touch);
		}
		return instance;
	}
	
	@Override
	public boolean execute() {
		int distance = sonic.getDistance();
		int isPressed = touch.isPressed()?1:0;
		LCD.drawInt(distance, 3, 10, 1);
		try {
			out.writeInt(distance);
			out.writeInt(isPressed);
			out.flush();
		} catch (IOException e) {
			LCD.drawString("ERROR WRITE!", 0, 6);
		}
		return true;			
	}
}

/**
 * 变轨命令
 * @author wander
 *
 */
class SwitchCommand implements Command{
	private static SwitchCommand instance = new  SwitchCommand();
	
	private boolean switchToMain = false;
	
	private static int DEGREE = 35;
	
	private SwitchCommand(){
		Motor.A.setSpeed(DEGREE*3);
	}
	
	public static SwitchCommand getInstance(boolean main){
		instance.setSwitchMain(main);
		return instance;
	}
	
	@Override
	public boolean execute() {
		if(switchToMain){
			//TODO 等待现场调试
			
		}
		else{
			//Motor.A.rotate();
			
		}
		//Motor.A.rotate(DEGREE+10);
		//Motor.A.rotate(-DEGREE-10);
		return true;
	}

	public void setSwitchMain(boolean switchMain) {
		this.switchToMain = switchMain;
	}
	
}