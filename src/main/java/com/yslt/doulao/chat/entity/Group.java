package com.yslt.doulao.chat.entity;

public class Group {

	private String id;					//群id
	private String groupName;			//（容联）群组名字，最长为50个字符
	private String master;			//群主id
	private String imgPath;		//群头像
	private String categoryCode;	//群分类代号
	private String createTime;			//创建时间
	private String updateTime;			//更新时间
	private String type;				//（容联）群类型（群组类型  
											//0：临时组(上限100人) 
											//1：普通组(上限300人) 
											//2：普通组(上限500人) 
											//3：付费普通组 (上限1000人) 
											//4：付费VIP组（上限2000人））
	private String permission;			//（容联）申请加入模式
											//0：默认直接加入 
											//1：需要身份验证 
											//2：私有群组
	private String declared;			//（容联）群组公告，最长为200个字符
	private String officialType;			//是否是官方群
	private String shamMaster;				//伪群主
	private String groupNum;				//群号

	private String flag;					//（不记录到数据库）
	private String categoryName;			//群分类名称（不记录到数据库）
	//=======getter and setter=========
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getDeclared() {
		return declared;
	}
	public void setDeclared(String declared) {
		this.declared = declared;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getOfficialType() {
		return officialType;
	}
	public void setOfficialType(String officialType) {
		this.officialType = officialType;
	}
	public String getShamMaster() {
		return shamMaster;
	}
	public void setShamMaster(String shamMaster) {
		this.shamMaster = shamMaster;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
