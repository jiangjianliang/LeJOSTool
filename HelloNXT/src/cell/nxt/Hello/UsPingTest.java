package cell.nxt.Hello;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class UsPingTest {
	public static void main(String[] args) {
		LCD.drawString("USPing", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		UltrasonicSensor sonar = new UltrasonicSensor(SensorPort.S1);
		int[] distances = new int[8];
		int col = 0;
		boolean more = true;
		while (more) {
			sonar.ping();
			Sound.pause(200);
			sonar.getDistances(distances);
			for (int i = 0; i < distances.length; i++) {
				LCD.drawInt(distances[i], 4, 4 * col, i);
			}
			col += 1;
			if (col > 4) {
				more = Button.waitForAnyPress() < 8;
				col = 0;
			}
		}
	}
}