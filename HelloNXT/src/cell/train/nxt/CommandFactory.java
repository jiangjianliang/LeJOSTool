package cell.train.nxt;

import java.io.DataOutputStream;

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
	public Command parseCommand(int cmd, IrLinkExt link, UltrasonicSensor sonic, DataOutputStream out){
		Command result = null;
		switch(cmd){
		case Command.EXIT:
				result = ExitCommand.getInstance();
				break;
		case Command.TRAIN_STOP:
				result = TrainStopCommand.getInstance(link);
				break;
		case Command.UPDATE_DISTANCE:
			result = UpdateDistanceCommand.getInstance(sonic, out);
			break;
		default:
				result = ChangeSpeedCommand.getInstance(cmd, link);
		}
		return result;
	}
}
