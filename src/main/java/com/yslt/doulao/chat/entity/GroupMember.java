package com.yslt.doulao.chat.entity;

import com.mongodb.BasicDBObject;

public class GroupMember {

	private String id;				//主键
	private String groupId;			//群id
	private String memberId;		//成员id
	private String remarkName;		//群昵称
	private String autoToMailList;	//是否自动保存到通讯录
	private String joinTime;		//加入时间
	private String status;			//状态（0：退群 1：在群 2：主动申请中 3:被邀请状态）
	private String quitTime;		//离开时间
	private String updateTime;		//更新数据时间
	private String ifManager;		//是否是管理员（0：不是 1：是）
	private String remark;			//备注信息/加群理由
	private String inviterId;		//邀请者id
	private BasicDBObject settings;		//用户设置

	private String screenName;		//昵称，不计库
	private String imgPath;			//头像路径（不计库）
	private Integer gender;			//性别（不计库）
	//------------getter and setter------------------
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getRemarkName() {
		return remarkName;
	}
	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}
	public String getAutoToMailList() {
		return autoToMailList;
	}
	public void setAutoToMailList(String autoToMailList) {
		this.autoToMailList = autoToMailList;
	}
	public String getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getQuitTime() {
		return quitTime;
	}
	public void setQuitTime(String quitTime) {
		this.quitTime = quitTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getIfManager() {
		return ifManager;
	}

	public void setIfManager(String ifManager) {
		this.ifManager = ifManager;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInviterId() {
		return inviterId;
	}

	public void setInviterId(String inviterId) {
		this.inviterId = inviterId;
	}

	public BasicDBObject getSettings() {
		return settings;
	}

	public void setSettings(BasicDBObject settings) {
		this.settings = settings;
	}
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}
}
