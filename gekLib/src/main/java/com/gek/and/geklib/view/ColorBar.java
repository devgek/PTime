package com.gek.and.geklib.view;


public class ColorBar implements Comparable<ColorBar>{
	private int value;
	private int color;
	
	public ColorBar(int value, int color) {
		this.value = value;
		this.color = color;
	}
	
	public Integer getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public int compareTo(ColorBar another) {
		return another.getValue().compareTo(this.getValue());
	}
}
