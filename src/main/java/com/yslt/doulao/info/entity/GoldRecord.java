package com.yslt.doulao.info.entity;

/**
 * @Description: 经验值，流量石记录表
 * @anthor: shi_lin
 * @CreateTime: 2015-11-12
 */
public class GoldRecord {
	private String id;
	private String userId;
	private String typeId; // 类型 流量石1，经验值为2
	private String time; //
	private String countent; // 数量
	private String message; // 描述

	// ==========================getter and
	// setter=============================================

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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCountent() {
		return countent;
	}

	public void setCountent(String countent) {
		this.countent = countent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
