package cell.hello.pc.usb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

import static cell.hello.pc.usb.USBGUI.*;

public class USBSend {
	private NXTConnector conn;
	private DataOutputStream outDat;
	private DataInputStream inDat;

	public USBSend() {
		conn = new NXTConnector();
		
		if (!conn.connectTo("usb://")) {
			System.err.println("No NXT found using USB");
			System.exit(1);
		}
		
		outDat = new DataOutputStream(conn.getOutputStream());
		inDat = new DataInputStream(conn.getInputStream());
	}

	public void SendMessage(int control) {
		try {
			outDat.writeInt(control);
			outDat.flush();

		} catch (IOException ioe) {
			System.err.println("IO Exception writing bytes");
		}
//		try {
//			
//			System.out.println("Closed data streams");
//		} catch (IOException ioe) {
//			System.err.println("IO Exception Closing connection");
//		}
	}

	public void ReceiveMessage() {
		int proc = 0;
		while(true) 
		{    
			try {
	        	 proc = inDat.readInt();
	        	 if (proc == -1) {
	        		 System.out.println("EXIT !!!!!!!!!!!");
	        		 break;
	        	 }
	        	 UpdateText(Integer.toString(proc));
	        } catch (IOException ioe) {
	           System.err.println("IO Exception reading reply");
	           ioe.printStackTrace();
	        }            
//		
//			try {
//				
//				System.out.println("Closed data streams");
//			} catch (IOException ioe) {
//				System.err.println("IO Exception Closing connection");
//			}
		}
	}
	
	public void QuitMessage() {
		try {
			inDat.close();
			outDat.close();
			conn.close();
			System.out.println("Closed connection");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
	}
}