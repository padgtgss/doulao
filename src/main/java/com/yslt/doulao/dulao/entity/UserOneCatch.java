package com.yslt.doulao.dulao.entity;

/**
 * @Description: UserOneCatch
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */

public class UserOneCatch {
	private String id;

	// 时间
	private String date;
	// 已经捞到的金额
	private Float money;
	// 当日已经捞过的次数
	private Integer catchSum;

	// ==================================getter and
	// setter=======================================

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Integer getCatchSum() {
		return catchSum;
	}

	public void setCatchSum(Integer catchSum) {
		this.catchSum = catchSum;
	}
}
