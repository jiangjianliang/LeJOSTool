package  cell.hello.pc.test;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

public class BtTest {
	public static void main(String[] args) {
		NXTConnector connector_1 = new NXTConnector();
		NXTConnector connector_2 = new NXTConnector();
		
		boolean connected_1 = connector_1.connectTo("bt://NXT");
		boolean connected_2 = connector_2.connectTo("bt://NXT_OF");
		
		if (!connected_1 || !connected_2) {
			System.out.println("NXT bt connect failed.");
			System.exit(-1);
		}
		
		DataOutputStream output_1 = new DataOutputStream(connector_1.getOutputStream());
		DataOutputStream output_2 = new DataOutputStream(connector_2.getOutputStream());
		
		for (int i = 0; i < 7; i++) {
			try {
				int rand_1 = (int) (Math.random() * 9999);
				int rand_2 = (int) (Math.random() * 9999);
				output_1.writeInt(rand_1);
				output_2.writeInt(rand_2);
				output_1.flush();
				output_2.flush();
				System.out.println("NXT : " + rand_1);
				System.out.println("NXT_OF : " + rand_2);
			} catch (IOException ioe) {
				System.out.println("IOException : send int.");
			}
		}
		try {
			output_1.writeInt(-1);
			output_2.writeInt(-1);
			output_1.flush();
			output_2.flush();
			output_1.close();
			output_2.close();
			connector_1.close();
			connector_2.close();
		} catch (IOException ioe) {
			System.out.println("IOException : close connection.");
		}
	}
}















