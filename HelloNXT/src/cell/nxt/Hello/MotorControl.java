package cell.nxt.Hello;

import lejos.nxt.*;

public class MotorControl {
	
	public static void main(String[] args) {
		int angle = 100;
		LCD.drawInt(angle, 4, 0, 7);
		
		while (true) {
			int id = Button.waitForAnyPress();
			
			switch (id) {
				case Button.ID_ENTER :
					Motor.A.rotate(angle);
					LCD.drawString("rotated.", 0, 1);
					Button.waitForAnyPress();
					Motor.A.rotateTo(0);
					LCD.drawString("rotate back.", 0, 1);
					Button.waitForAnyPress();
					LCD.clear(1);
					
					break;
				case Button.ID_ESCAPE :
					System.exit(0);
				case Button.ID_LEFT :
					angle -= 5;
					if (angle < 0)	angle = 5;
					LCD.drawInt(angle, 4, 0, 7);
					break;
				case Button.ID_RIGHT :
					angle += 5;
					if (angle > 360) angle = 360;
					LCD.drawInt(angle, 4, 0, 7);
					break;
			}
		}
	}
	
	public static void program_1() {
		Motor.A.forward();
		LCD.drawString("FORWARD", 0, 0);
		Button.waitForAnyPress();
		Motor.A.backward();
		LCD.drawString("BACKWARD", 0, 1);
		Button.waitForAnyPress();
		Motor.A.forward();
		LCD.drawString("FORWARD", 0, 0);
		Button.waitForAnyPress();
		Motor.A.stop();
	}
	
	public static void program_2() {
		LCD.drawString("Inertia Test", 0, 0);  // just so the program will not start immediately
        Button.waitForAnyPress();
        program_2_rotate360();
        Motor.A.setSpeed(720);
        program_2_rotate360();
	}
	
	public static void program_2_rotate360() {
		Motor.A.resetTachoCount();
		Motor.A.forward();
		int count = 0;
		while (count < 360)	count = Motor.A.getTachoCount();
//		Motor.A.stop();
		Motor.A.flt();
		LCD.drawInt(count, 0, 1);
		while (Motor.A.getRotationSpeed() > 0);
		LCD.drawInt(Motor.A.getTachoCount(), 7, 1);
		Button.waitForAnyPress();
		LCD.clear();
	}
	
	public static void program_3() {
		Button.waitForAnyPress();
		program_3_rotate();
		Motor.A.setSpeed(720);
		program_3_rotate();
	}
	
	public static void program_3_rotate() {
		Motor.A.rotate(360);
		LCD.drawInt(Motor.A.getTachoCount(), 0, 0);
		Motor.A.rotateTo(0);
		LCD.drawInt(Motor.A.getTachoCount(), 0, 1);
		Button.waitForAnyPress();
		LCD.clear();
	}
	
	public static void program_4() {
		Motor.A.rotate(360, true);
		program_4_print();

		Motor.A.rotate(720, true);
		LCD.drawString("second", 0, 7);
		program_4_print();
	}
	
	public static void program_4_print() {
		while (Motor.A.isMoving()) {
			LCD.drawInt(Motor.A.getTachoCount(), 4, 0, 0);
			if (Button.readButtons() == Button.ID_ESCAPE) {
				LCD.drawString("Button", 0, 6);
				Motor.A.stop();
			}
		}
		while (Motor.A.getRotationSpeed() > 0);
		LCD.drawInt(Motor.A.getTachoCount(), 4, 0, 1);
		Button.waitForAnyPress();
	}
}















