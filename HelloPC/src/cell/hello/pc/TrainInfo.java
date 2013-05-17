package cell.hello.pc;

public class TrainInfo {
	
	
	private int speed;
	private int state;	//0 : stop;		1 : forward;	-1 : backward;
	private int position;	//0 : sA -> sB;	1 : sB;	2 : sB -> sA;	3 : sA
	private int destination;		//0 : none;		1 : sB;		2 : sA;
	
	public TrainInfo(){
		speed = 3;
		state = 0;
		position = 1;
		destination = 0;
	}
	
	public boolean isForward(){
		return state > 0;
	}
	
	public boolean isStop(){
		return state == 0;
	}
	
	public boolean isBackward(){
		return state < 0;
	}
	
	public void setForward(){
		state = 1;
	}
	public void setStop(){
		state = 0;
	}
	public void setBackward(){
		state = -1;
	}

	public boolean isArrival(){
		return position == destination;
	}
	
	//getter and setter
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}
}