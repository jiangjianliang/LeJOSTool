package com.wander.train.pc.common;

public interface Config {
		//站点数量
		public static int STATION_NUM = 2;
		//火车对应的起始NXT编号
		public static int TRAIN_NXT_START = 2;
		//火车数量
		public static int TRAIN_NUM = 1;
		//SWITCH站点的编号
		public static int SWITCH_INDEX  = 0;
		/**
		 * 连接方式
		 * 0 USB, 1 BLUETOOTH
		 */
		public static int CONN_TYPE = 1;
		//心跳发送周期
		public final static int HeartBeatPeriod = 500;
		//心跳检测周期
		public final static int HeartDetectorPeriod = 2000;
		//心跳标记
		public final static int HEART_BEAT = 9999;

		/**
		 * 火车的颜色
		 * TODO 待确定真实的蓝色
		 * 2	蓝色
		 * 3	黄色
		 */
		public final static int[] TrainColor = {3, 2};
		
		
}
