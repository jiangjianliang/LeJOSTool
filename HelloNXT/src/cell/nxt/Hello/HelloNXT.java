package cell.nxt.Hello;

import lejos.nxt.*;

public class HelloNXT {
	
	public static boolean running = false;
	
	public static void main(String[] args) {
		
		TouchSensor touchSensor = new TouchSensor(SensorPort.S1);
		touchSensor.isPressed();
		
		LCD.drawString("motor A & C proc", 0, 0);
		LCD.drawString("ENTER: run/stop", 0, 1);
		LCD.drawString("LEFT:  weak A", 0, 2);
		LCD.drawString("RIGHT: weak C", 0, 3);
		LCD.drawString("ESC:   exit", 0, 4);
		
		Motor.A.setSpeed(9000);
		Motor.C.setSpeed(9000);

		Button.ENTER.addButtonListener(new ButtonListener() {
			
			@Override
			public void buttonReleased(Button b) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub
				if (running) {
					Motor.A.stop();
					Motor.C.stop();
					running = false;
				}
				else {
					Motor.A.backward();
					Motor.C.backward();
					running = true;
				}
			}
		});
		
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			
			@Override
			public void buttonReleased(Button b) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub
				System.exit(1);
			}
		});
		
		Button.LEFT.addButtonListener(new ButtonListener() {
			
			@Override
			public void buttonReleased(Button b) {
				// TODO Auto-generated method stub
				Motor.A.setSpeed(9000);
				if (running)	Motor.A.backward();
			}
			
			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub
				Motor.A.setSpeed(300);
				if (running)	Motor.A.backward();
			}
		});
		
		Button.RIGHT.addButtonListener(new ButtonListener() {
			
			@Override
			public void buttonReleased(Button b) {
				// TODO Auto-generated method stub
				Motor.C.setSpeed(9000);
				if (running)	Motor.C.backward();
			}
			
			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub
				Motor.C.setSpeed(300);
				if (running)	Motor.C.backward();
			}
		});
		
		while (true) {
			Button.waitForAnyPress();
		}
	}
}














