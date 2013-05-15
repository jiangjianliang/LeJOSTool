package cell.hello.pc.test;

//import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

public class UsbTest {
	public static void main(String[] args) {
		NXTConnector connector_1 = new NXTConnector();
		NXTConnector connector_2 = new NXTConnector();
		
		boolean connected_1 = connector_1.connectTo("usb://NXT");
		boolean connected_2 = connector_2.connectTo("usb://NXT_OF");
		if (!connected_1 || !connected_2) {
			System.out.println("NXT connect failed ...");
			System.exit(-1);
		}
		
		DataOutputStream Dout_1 = new DataOutputStream(connector_1.getOutputStream());
		DataOutputStream Dout_2 = new DataOutputStream(connector_2.getOutputStream());
		
		for (int i = 1; i < 7; i++) {
			try {
				int rand_1 = (int) (Math.random() * 9999);
				int rand_2 = (int) (Math.random() * 9999);
				Dout_1.writeInt(rand_1);
				Dout_2.writeInt(rand_2);
				Dout_1.flush();
				Dout_2.flush();
			} catch (IOException ioe) {
				System.out.println("IOException(connector_1)");
			}
		}
		try {
			Dout_1.writeInt(-1);
			Dout_2.writeInt(-1);
			Dout_1.flush();
			Dout_2.flush();
			Dout_1.close();
			Dout_2.close();
			connector_1.close();
			connector_2.close();
		} catch (IOException ioe) {
			System.out.println("IOException while close.");
		}
	}
}













