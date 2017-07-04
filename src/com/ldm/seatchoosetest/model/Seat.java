package com.ldm.seatchoosetest.model;

import java.io.Serializable;

public class Seat implements Serializable {
	static final long serialVersionUID = 1L;

	//
	public interface SeatStatus {
		int WALKWAY_X = -1;// 走道x轴
		int WALKWAY_Y = -2;// 走道y轴
		int WALKWAY = 0;// 走道
		int SELECTABLE = 1;// 可选
		int LOCKED = 2;// 锁定(已经被选了)
		int SELECTED = 3;// 已选
	}

	private int status = 0;
	private String des = "";// 列数

	public Seat() {
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return status;
	}

	 public void setStatus(int status) {
	 this.status = status;
	 }

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

}