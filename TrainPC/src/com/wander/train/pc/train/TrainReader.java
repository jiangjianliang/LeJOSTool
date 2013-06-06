package com.wander.train.pc.train;

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
public class TrainReader extends BluetoothReader {
	
	public TrainReader(DataInputStream in, HeartDetectable nxt){
		super(in, nxt);
	}

	@Override
	protected void receiveAndAct() {
		try {
			int result = in.readInt();
			if(result == Config.HEART_BEAT){
				nxt.setBeatFlag(true);
			}
		} catch (IOException e) {
			System.exit(-1);
		}
	}
	
	
	
}
