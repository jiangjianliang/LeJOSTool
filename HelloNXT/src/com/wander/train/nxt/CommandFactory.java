package com.wander.train.nxt;

import java.io.DataOutputStream;

import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
/**
 * 命令工厂类
 * @author wander
 *
 */
public class CommandFactory {
	
	private static CommandFactory instance = new CommandFactory();

	private CommandFactory() {

	}

	public static CommandFactory getInstance() {
		return instance;
	}
	/**
	 * 格式化命令
	 * @param cmd
	 * @param link
	 * @param sonic
	 * @param out
	 * @return
	 */
	public Command parseCommand(int cmd, IrLinkExt link, UltrasonicSensor sonic, DataOutputStream out, TouchSensor touch){
		Command result = null;
		switch(cmd){
		case Command.EXIT:
				result = ExitCommand.getInstance();
				break;
		case Command.TRAIN_STOP_A:
				result = TrainStopCommand.getInstance(link, 1);
				break;
		case Command.TRAIN_STOP_B:
			result = TrainStopCommand.getInstance(link, 2);
			break;
		case Command.UPDATE_DISTANCE:
			result = UpdateDistanceCommand.getInstance(sonic, out, touch);
			break;
		default:
			boolean dir = cmd > 0; // true : forward; false : backward
			cmd = Math.abs(cmd);
			int speed = (cmd % Command.TRAIN_MARK_A) % Command.SPEED_MARK;
			int newSpeed;
			if (dir){
				newSpeed = speed;
			}
			else
			{
				newSpeed = 16 - speed;
			}
			
			if(Math.abs(cmd) > Command.TRAIN_MARK_A){
				result = ChangeSpeedCommand.getInstance(newSpeed, link, 1);				
			}
			else{
				result = ChangeSpeedCommand.getInstance(newSpeed, link, 2);				
			}
		}
		return result;
	}
}