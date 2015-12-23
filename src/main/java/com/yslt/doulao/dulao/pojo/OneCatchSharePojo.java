package com.yslt.doulao.dulao.pojo;

/**
 * @Description: OneCatchSharePojo
 * @anthor: shi_lin
 * @CreateTime: 2015-11-12
 */

public class OneCatchSharePojo {
	private int typeId; // 卡类型
	private String userId; // 用户
	private String description; // 详情
	private String targetId ;   //针对现金的 详情Id
	private String shareregCashId ;   //分享出去的 现金Id

	public OneCatchSharePojo() {
	}

	public OneCatchSharePojo(int typeId, String userId, String description,String targetId) {
		this.targetId = targetId;
		this.typeId = typeId;
		this.userId = userId;
		this.description = description;
	}

	// ========================getter and setter========================

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getShareregCashId() {
		return shareregCashId;
	}

	public void setShareregCashId(String shareregCashId) {
		this.shareregCashId = shareregCashId;
	}
}
