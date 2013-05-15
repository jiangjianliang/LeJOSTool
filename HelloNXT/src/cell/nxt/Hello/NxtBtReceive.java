package cell.nxt.Hello;

import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class NxtBtReceive {
	public static void main(String[] args) {
		LCD.drawString("waiting", 0, 0);
		BTConnection connection = Bluetooth.waitForConnection();
		LCD.drawString("connected.", 0, 0);
		DataInputStream input = connection.openDataInputStream();
		
		int pos = 0;
		while (true) {
			try {
				int i = input.readInt();
				if (i == -1)	break;
				LCD.drawInt(i, 4, 0, pos+1);
				pos = (pos + 1) % 6;
			} catch (IOException ioe) {
				LCD.drawString("ERROR.", 0, 7);
			}
		}
		try {
			input.close();
			connection.close();
		} catch (IOException ioe) {
			LCD.drawString("CLOSE ERROR.", 0, 7);
		}
		Button.waitForAnyPress();
	}
}
















