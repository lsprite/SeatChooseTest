package com.ldm.seatchoosetest.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 每一排的座位信息
 * 
 * @author Sunny
 */
public class SeatInfo implements Serializable {
	static final long serialVersionUID = 1L;
	//
	private String des = "";// 行数
	private ArrayList<Seat> seatList = null;// 一行所有座位

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public ArrayList<Seat> getSeatList() {
		return seatList;
	}

	public void setSeatList(ArrayList<Seat> seatList) {
		this.seatList = seatList;
	}

}