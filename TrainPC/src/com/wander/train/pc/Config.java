package com.wander.train.pc;

public interface Config {
		//配置参数 
		/**
		 * 站点数量
		 */
		public static int STATION_NUM = 1;
		/**
		 * 火车对应的起始NXT编号
		 */
		public static int TRAIN_NXT_START = 2;
		/**
		 * 火车数量
		 */
		public static int TRAIN_NUM = 0;
		
		/**
		 * SWITCH站点的编号
		 */
		public static int SWITCH_INDEX  = 0;
		
		/**
		 * 连接方式
		 * 0 USB, 1 BLUETOOTH
		 */
		public static int CONN_TYPE = 1;
		
		/**
		 * 心跳标记
		 */
		public final static int HEARTBEAT = 99999;
		
		/**
		 * 心跳检测周期
		 */
		public final static int HeartDetectorPeriod = 1000;
		
		/**
		 * 火车的颜色
		 * TODO 待确定真实的蓝色
		 * 2	蓝色
		 * 3	黄色
		 */
		public final static int[] TrainColor = {3, 2};
		
		
}
