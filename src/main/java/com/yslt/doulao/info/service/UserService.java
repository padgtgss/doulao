package com.yslt.doulao.info.service;

/**
 * @Description: AboutUserService
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
public interface UserService {
	/**
	 * 获取用户钻卡等级
	 * 
	 * @param userId
	 * @return
	 */
	int getBrickLeave(String userId);

	/**
	 * 减少用户经验值
	 * 
	 * @param userId
	 * @param exp
	 */
	void decreaseExp(String userId, String exp);

	/**
	 *根据 不同类型的 现金  获取现金记录
	 * recordtype   1 是查零钱记录  2是转整记录
	 * @param userId
	 * @param recordtype
	 * @return
	 */
	String getCashDetail(String userId, String recordtype);
}
