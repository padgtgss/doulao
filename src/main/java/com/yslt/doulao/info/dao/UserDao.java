package com.yslt.doulao.info.dao;

import com.yslt.doulao.info.entity.User;

/**
 * @Description: UserDao
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
public interface UserDao {
	/**
	 * 获取用户钻卡等级
	 * 
	 * @param userId
	 * @return
	 */
	int getBrickLeave(String userId);

	/**
	 * 更新用户经验值
	 * 
	 * @param userId
	 */
	void updateExperience(String userId, String experience);

	/**
	 * 用户等级
	 * @param userId
	 * @return
	 */
	String getUserLeave(String userId);


	/**
	 * 电话
	 * @param userId
	 * @return
	 */
	String getUserMobile(String userId);

	User getById(String userId);
}
