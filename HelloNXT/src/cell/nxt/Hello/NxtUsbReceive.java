package cell.nxt.Hello;

import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

public class NxtUsbReceive {
	public static void main(String[] args) {
		LCD.drawString("waiting..", 0, 0);
		USBConnection connection = USB.waitForConnection();
		LCD.drawString("connected.", 0, 0);
		DataInputStream dIn = connection.openDataInputStream();
		
		int pos = 0;
		
		while (true) {
			int i;
			try {
				i = dIn.readInt();
				if (i == -1)	break;
				LCD.drawInt(i, 4, 0, pos+1);
				pos = (pos + 1) % 6;
			} catch (IOException e) {
				LCD.drawString("IO Exception", 0, 7);
			}
		}
		
		try {
			dIn.close();
			connection.close();
		} catch (IOException ioe) {
			LCD.drawString("IOE (close)", 0, 7);
		}
		
		LCD.drawString("Press to Exit.", 0, 7);
		Button.waitForAnyPress();
	}
}










