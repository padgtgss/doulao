package com.yslt.doulao.dulao.dao;

import com.yslt.doulao.dulao.entity.UserCatch;

/**
 * @Description: UserCatchDao
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
public interface UserCatchDao {

	/**
	 * 获取用户当天的 转盘次数
	 * 
	 * @param userId
	 * @return
	 */
	UserCatch getByUser(String userId);

	/**
	 * 增加当天已使用的转盘次数
	 */
	void addCatchSum(UserCatch userCatch);
}
