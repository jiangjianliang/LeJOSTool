package com.wander.train.pc;

public class StationInfo {

	public int distance;
	public int isIn = 0;
	public UltraSonic ultraSonic;

	public StationInfo(int dis) {
		distance = dis;
		ultraSonic = new UltraSonic(dis);
	}

	public int updateDistance() {
		return ultraSonic.update(distance);
	}
	
	/**
	 * 判断距离变化的类
	 * @author wander
	 *
	 */
	class UltraSonic {
		private int distance;

		public UltraSonic(int distance) {
			this.distance = distance;
		}

		public int update(int dis) {
			// train enter
			if (distance > 15 && (distance = dis) <= 15)
				return 1;
			// train leave
			if (distance <= 15 && (distance = dis) > 15)
				return 2;
			return 0;
		}
	}

}
