package cell.train.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class TSSubNXT {
	private static final int NXT_TRAIN_EXIT = -1;
	
	public static void main(String[] args) throws Exception {
		UltrasonicSensor station2 = new UltrasonicSensor(SensorPort.S4);
		
		String name = "NXT";
	    LCD.drawString("Connecting...", 0, 0);
	    RemoteDevice btrd = Bluetooth.getKnownDevice(name);

	    if (btrd == null) {
	    	LCD.clear();
	    	LCD.drawString("No such device", 0, 0);
	    	Button.waitForAnyPress();
	    	System.exit(1);
	    }

	    BTConnection btc = Bluetooth.connect(btrd);

	    if (btc == null) {
	    	LCD.clear();
	    	LCD.drawString("Connect fail", 0, 0);
	    	Button.waitForAnyPress();
	    	System.exit(1);
	    }
	  
	    LCD.clear();
	    LCD.drawString("Connected", 0, 0);

	    DataInputStream dis = btc.openDataInputStream();
	    DataOutputStream dos = btc.openDataOutputStream();

	    while(true) {
	    	try {
	    		int cmd = dis.readInt();
	    		if (cmd == NXT_TRAIN_EXIT)
	    			break;
	    		LCD.drawString("DIS read", 0, 5);
	    		LCD.drawInt(cmd, 1, 8, 5);
	    		int tDis = station2.getDistance();
	    		dos.writeInt(tDis);
	    		dos.flush();
	    		LCD.drawString("DOS flush", 0, 6);
	    		LCD.drawInt(tDis, 8, 0, 2);
	    	} catch (IOException ioe) {
	    		LCD.drawString("Write Exception", 0, 0);
	    		Button.waitForAnyPress();
	    		System.exit(0);
	    	}
	    }
	    
	    try {
	    	LCD.drawString("Closing... ", 0, 0);
	    	dis.close();
	    	dos.close();
	    	btc.close();
	    } catch (IOException ioe) {
	    	LCD.drawString("Close Exception", 0, 0);
	    }
	  
	    LCD.drawString("Finished",3, 4);
	    Button.waitForAnyPress();
	  }
}