package cell.hello.pc;

public class pcTrainState {
	public int port;	//R : 0 ~ 3;	B : 4 ~ 7;
	public int speed;
	public int state;	//0 : stop;		1 : forward;	-1 : backward;
	public int position;	//0 : sA -> sB;	1 : sB;	2 : sB -> sA;	3 : sA
	public int destination;		//0 : none;		1 : sB;		2 : sA;
}