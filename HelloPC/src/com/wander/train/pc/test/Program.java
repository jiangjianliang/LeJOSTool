package com.wander.train.pc.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Program {

	private NXTInfo[] nxts = new NXTInfo[3];
	{
		nxts[0] = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT1",
			"00:16:53:10:40:a7");
		nxts[1] = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT2",
			"00:16:53:13:3a:1f");
		nxts[2] = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT3",
			"00:16:53:09:78:1f");
	}
	
	// private NXTConnector[] pcNxtList;
	private NXTComm[] pcNxtList;
	private DataOutputStream[] sender;
	private DataInputStream[] receiver;
	
	public Program(){
		pcNxtList = new NXTComm[1];
		sender = new DataOutputStream[1];
		receiver = new DataInputStream[1];
		connect();
		new Scheduler(receiver[0]).start();
	}
	
	public void connect(){
		boolean[] connected = new boolean[pcNxtList.length];
		boolean total = true;
		try {
			for(int i = 0; i < pcNxtList.length; i++){
				pcNxtList[i] = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				connected[i] = pcNxtList[i].open(nxts[i]);
				total = total && connected[i];
			}

		} catch (NXTCommException e) {
			e.printStackTrace();
		}

		if (!total) {
			System.err.println("error while connection to NXT.");
			System.exit(-1);
		}
		
		for (int i = 0; i < pcNxtList.length; i++) {
			System.err.println(i);
			sender[i] = new DataOutputStream(pcNxtList[i].getOutputStream());
			receiver[i] = new DataInputStream(pcNxtList[i].getInputStream());
		}
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Program pro = new Program();
		//pro.connect();
	}
}
