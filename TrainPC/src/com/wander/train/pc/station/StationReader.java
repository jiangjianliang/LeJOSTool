package com.wander.train.pc.station;

import java.io.DataInputStream;
import java.io.IOException;

import com.wander.train.pc.common.BluetoothReader;
import com.wander.train.pc.common.Config;
import com.wander.train.pc.common.HeartDetectable;
/**
 * 从蓝牙连接中读取数据
 * @author wander
 *
 */
public class StationReader extends BluetoothReader {
	
	
	public StationReader(DataInputStream in, HeartDetectable nxt){
		super(in, nxt);
	}

	@Override
	protected void receiveAndAct() {
		try {
			int result = in.readInt();
			//System.err.println("reading from station");
			if(result == Config.HEART_BEAT){
				nxt.setBeatFlag(true);
			}
			else{
				int dis = result/100;
				int color = result%100;
				((StationInfo) nxt).setDistance(dis);
				((StationInfo) nxt).setColor(color);		
			}
		} catch (IOException e) {
			System.exit(-1);
		}
	}
	
	
	
}
