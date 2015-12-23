package com.yslt.doulao.dulao.entity;

/**
 * @Description: 特权卡转盘记录
 * @anthor: shi_lin
 * @CreateTime: 2015-11-12
 */

public class UserPrivilegeDetail {

	private String id;
	private String date; // 时间
	private String userId; // 用户
	private String typeId; // 特权卡类型
	private String available; // 是否已经使用（1：可以使用，0不能使用）

	// ======================================getter and seter
	// ==============================

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}
}
