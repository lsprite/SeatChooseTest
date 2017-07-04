package com.ldm.seatchoosetest.model;

import java.io.Serializable;

public class SeatSelect implements Serializable {
	static final long serialVersionUID = 1L;
	private int x = 0;
	private int y = 0;

	public SeatSelect() {
		// TODO Auto-generated constructor stub
	}

	public SeatSelect(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
