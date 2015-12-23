package com.yslt.doulao.dulao.entity;

import com.google.code.morphia.annotations.Entity;
import common.util.pojo.BaseDomain;

/**
 * @Description: UserCatch
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
@Entity(value = "user_catch",noClassnameStored = true)
public class UserCatch extends BaseDomain{

	private String userId; // 用户
	private Integer catchSum; // 使用次数
	private String date; // 时间 eg:yyyy--MM--dd

	public UserCatch() {
	}


	// ===============================getter and
	// setter===========================================


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getCatchSum() {
		return catchSum;
	}

	public void setCatchSum(Integer catchSum) {
		this.catchSum = catchSum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
