package com.wander.train.nxt.train;
/**
 * 记录心跳
 * @author wander
 *
 */
public class HeartData {

	private  static boolean beatFlag = false;
	
	public static synchronized  void setBeatFlag(boolean beatFlag){
		HeartData.beatFlag = beatFlag;
	}
	
	public static synchronized  boolean isBeatFlag(){
		return beatFlag;
	}
}
