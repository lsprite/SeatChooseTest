﻿package com.ldm.seatchoosetest.model;

public class Seat {
	//
	public interface SeatStatus {
		int WALKWAY_X = -1;// 走道x轴
		int WALKWAY_Y = -2;// 走道y轴
		int WALKWAY = 0;// 走道
		int SELECTABLE = 1;// 可选
		int LOCKED = 2;// 锁定(已经被选了)
		int SELECTED = 3;// 已选
	}

	//
	/** 序号，当为走道时 为"Z" */
	private String n = null;
	/** 损坏标签 */
	private String damagedFlg = null;
	/** 情侣座 */
	private String loveInd = null;

	public void setN(String paramString) {
		this.n = paramString;
	}

	public boolean a() {
		return ("1".equals(this.loveInd)) || ("2".equals(this.loveInd));
	}

	public String getN() {
		return this.n;
	}

	public void setDamagedFlg(String paramString) {
		this.damagedFlg = paramString;
	}

	public String getDamagedFlg() {
		return this.damagedFlg;
	}

	public void setLoveInd(String paramString) {
		this.loveInd = paramString;
	}

	public String getLoveInd() {
		return this.loveInd;
	}
}