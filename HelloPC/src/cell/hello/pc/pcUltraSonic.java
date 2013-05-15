package cell.hello.pc;

public class pcUltraSonic {
	private int distance;
	
	public pcUltraSonic(int distance) {
		this.distance = distance;
	}
	
	public int update(int dis) {
		//train enter
		if (distance > 15 && (distance = dis) <= 15)
			return 1;
		//train leave
		if (distance <= 15 && (distance = dis) > 15)
			return 2;
		return 0;
	}
}