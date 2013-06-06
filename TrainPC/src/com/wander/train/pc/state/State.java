package com.wander.train.pc.state;

import java.io.IOException;

public abstract class State {
	protected Context context;
	
	public State (Context context){
		this.context = context;
	}
	/**
	 * 状态机处理函数
	 * @throws IOException
	 */
	public abstract void handle();
	/**
	 * 进入本状态时的处理
	 * @throws IOException
	 */
	public abstract void doExtra();
}
