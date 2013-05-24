package com.wander.train.pc;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public interface Config {
		//配置参数 
		/**
		 * 站点数量
		 */
		public static int STATION_NUM = 2;
		/**
		 * 火车数量
		 */
		public static int TRAIN_NUM = 2;
		/**
		 * SWITCH站点的编号
		 */
		public static int SWITCH_INDEX  = 0;
		/**
		 * 连接方式
		 * 0 USB, 1 BLUETOOTH
		 */
		public static int CONN_TYPE = 1;
		
}
