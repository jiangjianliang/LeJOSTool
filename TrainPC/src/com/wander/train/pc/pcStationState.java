package com.wander.train.pc;

public class pcStationState {
	public int distance;
	public pcUltraSonic ultraSonic;
	
	public pcStationState(int dis) {
		distance = dis;
		ultraSonic = new pcUltraSonic(dis);
	}
	
	public int update() {
		return ultraSonic.update(distance);
	}
}