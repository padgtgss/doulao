package com.yslt.doulao.dulao.enums;

/**
 * @Description: 捞一捞现金枚举
 * @anthor: shi_lin
 * @CreateTime: 2015-11-13
 */
public enum OneCatchCashEnum {

	MONEY_5(1, 5), MONEY_10(2, 10), MONEY_15(3, 15), MONEY_20(4, 20), MONEY_30(5, 30), MONEY_50(6, 50), MONEY_80(7,
			80), MONEY_100(8, 100);

	// 序号
	private int number; // 枚举编号
	// 描述
	private int cash; // 现金

	OneCatchCashEnum(int number, int cash) {
		this.cash = cash;
		this.number = number;
	}

	public static OneCatchCashEnum getByNumber(int number) {
		for (OneCatchCashEnum oneCatchCashEnum : OneCatchCashEnum.values()) {
			if (oneCatchCashEnum.getNumber() == number) {
				return oneCatchCashEnum;
			}
		}
		throw new IllegalArgumentException(number + "Is not in OneCatchCashEnum");
	}

	// ==========================getter and
	// setter=============================================

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}
}
