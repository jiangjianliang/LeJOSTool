package cell.nxt.train;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class IrLinkTest {
	private static final String STR_TITLE = "IR LINK TEST";
	private static final String STR_ARROW = "=>";
	private static final String STR_STATE = "state :";
	private static final String STR_STOP = "stop";
	private static final String STR_FORWARD = "forward";
	private static final String STR_BACKWARD = "backward";
	private static final String STR_SPEED = "speed :";
	private static final String STR_SPEED_UP = "speed up";
	private static final String STR_SPEED_DOWN = "speed down";
	
	private static final int[] index = {2, 3, 4, 6, 7};
	
	private IrLinkExt link;
	public int choice = 0;
	public int speed = 3;
	
	public static void main(String[] args) {
		IrLinkTest test = new IrLinkTest();
		while (true) {
			int cmd = Button.waitForAnyPress();
			switch (cmd) {
				case Button.ID_ESCAPE:
					LCD.clear();
					LCD.drawString("good bye", 3, 3);
					Button.waitForAnyPress(5000);
					System.exit(0);
					break;
				case Button.ID_ENTER:
					test.enterHandle();
					break;
				case Button.ID_LEFT:
					test.arrowUp();
					break;
				case Button.ID_RIGHT:
					test.arrowDown();
					break;
			}
		}
	}
	
	public IrLinkTest() {
		this(1);
	}
	
	public IrLinkTest(int port) {
		if (port == 1)	link = new IrLinkExt(SensorPort.S1);
		else if (port == 2)	link = new IrLinkExt(SensorPort.S2);
		else if (port == 3) link = new IrLinkExt(SensorPort.S3);
		else	link = new IrLinkExt(SensorPort.S4);
		
		//draw string
		LCD.drawString(STR_TITLE, 0, 0);
		LCD.drawString(STR_STATE, 0, 1);
		LCD.drawString(STR_STOP, 2, 2);
		LCD.drawString(STR_FORWARD, 2, 3);
		LCD.drawString(STR_BACKWARD, 2, 4);
		LCD.drawString(STR_SPEED, 0, 5);
		LCD.drawString(STR_SPEED_UP, 2, 6);
		LCD.drawString(STR_SPEED_DOWN, 2, 7);
		arrowDraw();
		drawSpeed();
		drawState();
	}
	
	public void enterHandle() {
		switch (choice) {
			case 3 :	//speed up
				speedUp();
				drawSpeed();
				break;
			case 4 :	//speed down
				speedDown();
				drawSpeed();
				break;
			default :	//stop, forward, backward
				send(choice);
				drawState();
				break;
		}
	}
	
	public void send(int dir) {
		//0 : stop;		1 : forward;	2 : backward;
		if (dir == 0)	link.sendPFSingleModePWM(0, 0, IrLinkExt.PF_PMW_FLOAT);
		else if (dir == 1) {
			int cmdB = speed;
			link.sendPFSingleModePWM(0, 0, cmdB);
		} else {
			int cmdB = 16 - speed;
			link.sendPFSingleModePWM(0, 0, cmdB);
		}
	}
	
	public void drawState() {
		LCD.clear(8, 1, 8);
		if (choice == 0)	LCD.drawString(STR_STOP, 8, 1);
		else if (choice == 1) 	LCD.drawString(STR_FORWARD, 8, 1);
		else	LCD.drawString(STR_BACKWARD, 8, 1);
	}
	
	public void drawSpeed() {
		LCD.clear(8, 5, 2);
		LCD.drawInt(speed, 2, 8, 5);
	}
	
	public void speedUp() {
		speed += 1;
		speed = (speed > 7 ? 7 : speed);
	}
	
	public void speedDown() {
		speed -= 1;
		speed = (speed < 1 ? 1 : speed);
	}
	
	public void arrowUp() {
		arrowClear();
		choice -= 1;
		choice = (choice < 0 ? 4 : choice);
		arrowDraw();
	}
	
	public void arrowDown() {
		arrowClear();
		choice = (choice + 1) % 5;
		arrowDraw();
	}
	
	private void arrowClear() {
		int pos = index[choice];
		LCD.clear(0, pos, 2);
	}
	
	private void arrowDraw() {
		int pos = index[choice];
		LCD.drawString(STR_ARROW, 0, pos);
	}
}
















