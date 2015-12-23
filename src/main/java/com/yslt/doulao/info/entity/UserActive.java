package com.yslt.doulao.info.entity;

/**
 * @Description: 用户活跃度
 * @anthor: shi_lin
 * @CreateTime: 2015-11-13
 */

public class UserActive {

	private String id;
	private String userId;
	private String active;
	private String createTime;
	private int fullActNum;
	private double postCount;
	private String upTime;
	private int decayEndTime;
	private int lastUpTime;
	// ====================================getter and
	// setter========================================

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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getFullActNum() {
		return fullActNum;
	}

	public void setFullActNum(int fullActNum) {
		this.fullActNum = fullActNum;
	}

	public double getPostCount() {
		return postCount;
	}

	public void setPostCount(double postCount) {
		this.postCount = postCount;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public int getDecayEndTime() {
		return decayEndTime;
	}

	public void setDecayEndTime(int decayEndTime) {
		this.decayEndTime = decayEndTime;
	}

	public int getLastUpTime() {
		return lastUpTime;
	}

	public void setLastUpTime(int lastUpTime) {
		this.lastUpTime = lastUpTime;
	}
}
