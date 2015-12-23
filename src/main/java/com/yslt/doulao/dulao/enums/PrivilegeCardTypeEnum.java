package com.yslt.doulao.dulao.enums;

/**
 * @Description: 特权卡类型
 * @anthor: shi_lin
 * @CreateTime: 2015-11-04
 */
public enum PrivilegeCardTypeEnum {
	SIGN_CARD(1, "签到翻倍"), REWARD_CARD(2, "打赏翻倍"), PASS_THROUGH_CARD(3, "穿越卡"), ACTIVE_CARD(4, "活跃卡");

	// 序号
	private int typeId;
	// 描述
	private String description;

	PrivilegeCardTypeEnum(int typeId, String description) {
		this.description = description;
		this.typeId = typeId;
	}

	public static PrivilegeCardTypeEnum getByTypeId(int typeId) {
		for (PrivilegeCardTypeEnum typeEnum : PrivilegeCardTypeEnum.values()) {
			if (typeEnum.getTypeId() == typeId) {
				return typeEnum;
			}
		}
		throw new IllegalArgumentException(typeId + "Is not in OneCatchTypeEnum");
	}

	// ========================================getter and
	// setter==============================================

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
