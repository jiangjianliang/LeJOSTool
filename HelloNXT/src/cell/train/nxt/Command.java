package cell.train.nxt;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
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
	final static int TRAIN_STOP = 0;
	// 要求发送距离
	final static int UPDATE_DISTANCE = 100;

	// 速度标记
	final static int SPEED_MARK = 1000;

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

	private TrainStopCommand(IrLinkExt link) {
		this.link = link;
	}
	
	public static TrainStopCommand getInstance(IrLinkExt link){
		if(instance == null){
			instance = new TrainStopCommand(link);
		}
		return instance;
	}
	@Override
	public boolean execute() {
		link.sendPFSingleModePWM(IrLinkExt.PF_Channel_1,
				IrLinkExt.PF_SINGLE_MODE_RED_PORT, IrLinkExt.PF_PMW_FLOAT);
		return true;
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

	private UpdateDistanceCommand(UltrasonicSensor sonic, DataOutputStream out) {
		this.sonic = sonic;
		this.out = out;
	}

	public static UpdateDistanceCommand getInstance(UltrasonicSensor sonic, DataOutputStream out){
		if(instance == null){
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
			out.flush();
		} catch (IOException e) {
			LCD.drawString("ERROR WRITE!", 0, 6);
		}
		return true;			
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

	
	private ChangeSpeedCommand(int cmd, IrLinkExt link) {
		this.cmd = cmd;
		this.link = link;
	}
	
	public static ChangeSpeedCommand getInstance(int cmd, IrLinkExt link){
		if(instance == null){
			instance = new ChangeSpeedCommand(cmd, link);
		}
		return instance;
	}
	
	@Override
	public boolean execute() {
		boolean dir = cmd > 0; // true : forward; false : backward
		cmd = Math.abs(cmd);
		int speed = cmd % Command.SPEED_MARK;
		int newSpeed;
		if (dir){
			newSpeed = speed;
		}
		else
		{
			newSpeed = 16 - speed;
		}
		link.sendPFSingleModePWM(IrLinkExt.PF_Channel_1,
				IrLinkExt.PF_SINGLE_MODE_RED_PORT, newSpeed);
		return true;
	}

}
