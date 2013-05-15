package cell.nxt.Hello;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class UsContinuous {
	public static void main(String[] args) {
		LCD.drawString("UsContinue.", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		LCD.drawString("start test.", 0, 0);
		
		UltrasonicSensor sonar = new UltrasonicSensor(SensorPort.S1);
		
		int col = 0;
		
		while (true) {
			LCD.drawInt(getDistance(sonar), 4, 0, col+1);
			LCD.drawInt(getDistance(sonar), 4, 4, col+1);
			LCD.drawInt(getDistance(sonar), 4, 8, col+1);
			LCD.drawInt(getDistance(sonar), 4, 12, col+1);
			col = (col + 1) % 7;
			if (Button.waitForAnyPress() == 8)	break;
		}
	}
	
	private static int getDistance(UltrasonicSensor sonar) {
		int distance = sonar.getDistance();
		distance = distance > 170 ? 0 : distance;
		return distance;
	}
}