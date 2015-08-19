package com.gek.and.project4.model;

public class BookedValues {
	private int finished;
	private int running;
	
	public int getFinished() {
		return finished;
	}
	public int getRunning() {
		return running;
	}
	
	public int getComplete() {
		return this.finished + this.running;
	}
	
	public void addFinished(int finished) {
		this.finished += finished;
	}
	
	public void setRunning(int running) {
		this.running = running;
	}
}
