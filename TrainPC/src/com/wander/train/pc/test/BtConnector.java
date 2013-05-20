package com.wander.train.pc.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

public class BtConnector {
	public static void main(String[] args) {
		//NXTConnector connector_1 = new NXTConnector();
		//NXTConnector connector_2 = new NXTConnector();
		//NXTConnector connector_3 = new NXTConnector();
		
		NXTComm connector_1 = null;
		NXTComm connector_2 = null;
		NXTComm connector_3 = null;
		
		boolean connected_1 = false;
		boolean connected_2 = false;
		boolean connected_3 = false;
		
		NXTInfo nxt_1 = new NXTInfo(NXTCommFactory.BLUETOOTH,"NXT1","00:16:53:09:78:1f");
		NXTInfo nxt_2 = new NXTInfo(NXTCommFactory.BLUETOOTH,"NXT2","00:16:53:10:40:a7");
		NXTInfo nxt_3 = new NXTInfo(NXTCommFactory.BLUETOOTH,"NXT3","00:16:53:13:3a:1f");
		
		try {
			connector_1 = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			connector_2 = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			connector_3 = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			connected_1 = connector_1.open(nxt_1);
			connected_2 = connector_2.open(nxt_2);
			connected_3 = connector_3.open(nxt_3);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}

		//boolean connected_1 = connector_1.connectTo("bt://NXT");
		//boolean connected_2 = connector_2.connectTo("bt://NXT_OF");
		//boolean connected_3 = connector_3.connectTo("bt://NXT");
		
		if (!connected_1 || !connected_2 || !connected_3) {
			System.out.println("NXT bt connect failed.");
			System.exit(-1);
		}
		
		DataOutputStream output_1 = new DataOutputStream(connector_1.getOutputStream());
		DataOutputStream output_2 = new DataOutputStream(connector_2.getOutputStream());
		DataOutputStream output_3 = new DataOutputStream(connector_3.getOutputStream());
		
		DataInputStream input_1 = new DataInputStream(connector_1.getInputStream());
		DataInputStream input_2 = new DataInputStream(connector_2.getInputStream());
		DataInputStream input_3 = new DataInputStream(connector_3.getInputStream());
		
		for (int i = 0; i < 6; i++) {
			try {
				int rand_1 = (int) (Math.random() * 9999);
				int rand_2 = (int) (Math.random() * 9999);
				int rand_3 = (int) (Math.random() * 9999);
				output_1.writeInt(rand_1);
				output_2.writeInt(rand_2);
				output_3.writeInt(rand_3);
				output_1.flush();
				output_2.flush();
				output_3.flush();
				System.out.println("===output " + i + "====");
				System.out.println("NXT1 : " + rand_1);
				System.out.println("NXT2 : " + rand_2);
				System.out.println("NXT3 : " + rand_3);
				System.out.println("===input " + i + "====");
				System.out.println("NXT1 : " + input_1.readInt());
				System.out.println("NXT2 : " + input_2.readInt());
				System.out.println("NXT3 : " + input_3.readInt());
			} catch (IOException ioe) {
				System.out.println("IOException : send int.");
			}
		}
		try {
			output_1.writeInt(-1);
			output_2.writeInt(-1);
			output_3.writeInt(-1);
			output_1.flush();
			output_2.flush();
			output_3.flush();
			output_1.close();
			output_2.close();
			output_3.close();
			connector_1.close();
			connector_2.close();
			connector_3.close();
		} catch (IOException ioe) {
			System.out.println("IOException : close connection.");
		}
	}
}