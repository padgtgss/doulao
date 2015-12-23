package com.yslt.doulao.dulao.entity;

import com.yslt.doulao.dulao.enums.CashFromEnum;

/**
 * @Description: 捞到的 现金记录
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */

public class UserCatchCashDetail {
	public String id;
	private String userId;
	private String time;
	private String message;
	private double countent;   //现金数量
	private CashFromEnum cashFromEnum;
	private String available;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getCountent() {
		return countent;
	}

	public void setCountent(double countent) {
		this.countent = countent;
	}

	public CashFromEnum getCashFromEnum() {
		return cashFromEnum;
	}

	public void setCashFromEnum(CashFromEnum cashFromEnum) {
		this.cashFromEnum = cashFromEnum;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}
}
