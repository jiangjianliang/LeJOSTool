package cell.hello.pc;
/**
 * 命令常量
 * @author wander
 *
 */
public interface Command {
	//要求发送距离
	final static int UPDATE_DISTANCE = 100;
	//火车停止
	final static int TRAIN_STOP  = 0;
	//程序退出
	final static int EXIT = -100;
	
	//速度标记
	final static int SPEED_MARK = 1000;
}
