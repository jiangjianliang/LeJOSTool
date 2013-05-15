package cell.nxt.Hello;

import cell.nxt.train.IrLinkExt;
import lejos.nxt.*;

public class IrControl {
	public static void main(String[] args) {
		int btn_cmd;
		IrControl control = new IrControl();
		
		while (true) {
			btn_cmd = Button.waitForAnyPress();
			control.updateKeyDown(btn_cmd);
		}
	}

	public SendParam cur, sav;
	
	public static final int STATE_MAIN = 0;
	public static final int STATE_PORT = 1;
	public static final int STATE_CHANNEL = 2;
	public static final int STATE_SPEED = 3;
	public static final int STATE_COMMAND = 4;
	public static final int CHOICE_SEND = 5;
	public static final int CHOICE_EXIT = 6;
	
	/* state : STATE_*
	 * choice : STATE_* - STATE_MAIN + CHOICE_*
	 */
	public int state, choice;
	private IrLinkExt link;
	
	public IrControl() {
		this(1);
	}
	
	public IrControl(int port) {
		switch (port) {
			case 1 :	link = new IrLinkExt(SensorPort.S1);	break;
			case 2 :	link = new IrLinkExt(SensorPort.S2);	break;
			case 3 :	link = new IrLinkExt(SensorPort.S3);	break;
			case 4 :	link = new IrLinkExt(SensorPort.S4);	break;
		}
		
		state = 0;
		choice = 1;
		
		cur = new SendParam();
		sav = new SendParam();
		
		updateGraphics();
	}
	
	public void updateKeyDown(int key_code) {
		switch (state) {
			case STATE_MAIN :	mainCMD(key_code);	break;
			case STATE_PORT :	portCMD(key_code);	break;
			case STATE_CHANNEL :	channelCMD(key_code);	break;
			case STATE_SPEED :	speedCMD(key_code);	break;
			case STATE_COMMAND :	commandCMD(key_code);	break;
		}
		
		updateGraphics();
	}

	public void updateGraphics() {
		String cmd = (cur.command == SendParam.CMD_STOP) ? "STOP" : 
			(cur.command == SendParam.CMD_FORWARD) ? "FORWARD" : "BACKWARD";
		LCD.clear();
		LCD.drawString("IrControl", 0, 0);
		LCD.drawString("port:  " + (cur.port == SendParam.PORT_RED ? "RED" : "BLUE"), 1, 2);
		LCD.drawString("channel:  " + (cur.channel + 1), 1, 3);
		LCD.drawString("speed:  " + (cur.speed + 1), 1, 4);
		LCD.drawString("cmd:  " + cmd, 1, 5);
		LCD.drawString("send", 1, 6);
		LCD.drawString("exit", 1, 7);
		
		switch (state) {
			case STATE_MAIN :
				switch (choice) {
					case STATE_PORT :	LCD.drawString(">", 0, 2);	break;
					case STATE_CHANNEL :	LCD.drawString(">", 0, 3);	break;
					case STATE_SPEED :	LCD.drawString(">", 0, 4);	break;
					case STATE_COMMAND :	LCD.drawString(">", 0, 5);	break;
					case CHOICE_SEND :	LCD.drawString(">", 0, 6);	break;
					case CHOICE_EXIT :	LCD.drawString(">", 0, 7);	break;
				}
				break;
			case STATE_PORT :	LCD.drawString(">", 7, 2);	break;
			case STATE_CHANNEL :	LCD.drawString(">", 10, 3);	break;
			case STATE_SPEED :	LCD.drawString(">", 8, 4);	break;
			case STATE_COMMAND :	LCD.drawString(">", 6, 5);	break;
		}
	}
	
	private void send() {
		int channel, cmdA, cmdB = 0;
		channel = cur.channel;
		cmdA = cur.port;
		switch (cur.command) {
			case SendParam.CMD_STOP :	cmdB = IrLinkExt.PF_PMW_FLOAT;	break;
			case SendParam.CMD_FORWARD :	cmdB = cur.speed;	break;
			case SendParam.CMD_BACKWARD :	cmdB = 16 - cur.speed;	break;
		}
		
		link.sendPFSingleModePWM(channel, cmdA, cmdB);
	}
	
	private void mainCMD (int key_code) {
		if (key_code == Button.ID_ESCAPE)
			choice = CHOICE_EXIT;
		else if (key_code == Button.ID_ENTER) {
			if (choice == CHOICE_SEND)	send();
			else if (choice == CHOICE_EXIT)	System.exit(0);
			else {
				state = choice;
				sav.copy(cur);
			}
		} else {
			if (key_code == Button.ID_RIGHT) {
				choice = choice + 1;
				choice = (choice > CHOICE_EXIT) ? 1 : choice;
			} else if (key_code == Button.ID_LEFT) {
				choice = choice - 1;
				choice = (choice < 1) ? CHOICE_EXIT : choice;
			}
		}
	}
	
	private void portCMD(int key_code) {
		if (key_code == Button.ID_ESCAPE) {
			state = STATE_MAIN;
			cur.copy(sav);
		} else if (key_code == Button.ID_ENTER) {
			state = STATE_MAIN;
		} else {
			if (key_code == Button.ID_RIGHT) {
				cur.port = (cur.port + 1) % (SendParam.PORT_BLUE + 1);
			} else if (key_code == Button.ID_LEFT) {
				cur.port = cur.port - 1;
				cur.port = (cur.port < 0) ? SendParam.PORT_BLUE : cur.port;
			}
		}
	}
	
	private void channelCMD(int key_code) {
		if (key_code == Button.ID_ESCAPE) {
			state = STATE_MAIN;
			cur.copy(sav);
		} else if (key_code == Button.ID_ENTER) {
			state = STATE_MAIN;
		} else {
			if (key_code == Button.ID_RIGHT) {
				cur.channel = (cur.channel + 1) % 4;
			} else if (key_code == Button.ID_LEFT) {
				cur.channel = cur.channel - 1;
				cur.channel = (cur.channel < 0) ? 3 : cur.channel;
			}
		}
	}
	
	private void speedCMD(int key_code) {
		if (key_code == Button.ID_ESCAPE) {
			state = STATE_MAIN;
			cur.copy(sav);
		} else if (key_code == Button.ID_ENTER) {
			state = STATE_MAIN;
		} else {
			if (key_code == Button.ID_RIGHT) {
				cur.speed = (cur.speed + 1) % 7;
			} else if (key_code == Button.ID_LEFT) {
				cur.speed = cur.speed - 1;
				cur.speed = (cur.speed < 0) ? 6 : cur.speed;
			}
		}
	}
	
	private void commandCMD(int key_code) {
		if (key_code == Button.ID_ESCAPE) {
			state = STATE_MAIN;
			cur.copy(sav);
		} else if (key_code == Button.ID_ENTER) {
			state = STATE_MAIN;
		} else {
			if (key_code == Button.ID_RIGHT) {
				cur.command = (cur.command + 1) % 3;
			} else if (key_code == Button.ID_LEFT) {
				cur.command = cur.command - 1;
				cur.command = (cur.command < 0) ? 2 : cur.command;
			}
		}
	}
	
	class SendParam {
		public static final int PORT_RED = 0;
		public static final int PORT_BLUE = 1;
		
		public static final int CMD_STOP = 0;
		public static final int CMD_FORWARD = 1;
		public static final int CMD_BACKWARD = 2;
		
		/* port : PORT_RED or PORT_BLUE
		 * channel : 0 ~ 3
		 * speed : 0 ~ 6
		 * command : CMD_STOP or CMD_FORWARD or CMD_BACKWARD
		 */
		
		public int port, channel, speed, command;
		
		public void copy(SendParam sp) {
			port = sp.port;
			channel = sp.channel;
			speed = sp.speed;
			command = sp.command;
		}
	}
}







