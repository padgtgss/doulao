package com.yslt.doulao.dulao.enums;

/**
 * @Description: 捞一捞转盘枚举类型
 * @anthor: shi_lin
 * @CreateTime: 2015-11-04
 */
public enum OneCatchTypeEnum {
	PRIVILEGE_CARD(1, "特权卡"), Empirical(2, "经验值"), CASH(3, "现金");
	// 序号
	private int number;
	// 描述
	private String description;

	OneCatchTypeEnum(int number, String description) {
		this.description = description;
		this.number = number;
	}

	public static OneCatchTypeEnum getByNumber(int number) {
		for (OneCatchTypeEnum oneCatchTypeEnum : OneCatchTypeEnum.values()) {
			if (oneCatchTypeEnum.getNumber() == number) {
				return oneCatchTypeEnum;
			}
		}
		throw new IllegalArgumentException(number + "Is not in OneCatchTypeEnum");
	}

	// ====================================getter and setter
	// ================================================

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
