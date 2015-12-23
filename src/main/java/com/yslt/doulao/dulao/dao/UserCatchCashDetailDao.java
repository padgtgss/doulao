package com.yslt.doulao.dulao.dao;

import java.util.List;

import com.yslt.doulao.dulao.entity.UserCatchCashDetail;

/**
 * @Description: UserCatchCashDetailDao
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
public interface UserCatchCashDetailDao {
	/**
	 * 获取当天抽到的现金总和
	 * 
	 * @param userId
	 * @return
	 */
	double getTodayCash(String userId);

	/**
	 * 保存捞一捞获得的现金记录（未分享）
	 * 
	 * @param userCatchCashDetail
	 */
	String save(UserCatchCashDetail userCatchCashDetail);

	/**
	 * 分享成功，更改状态
	 * 
	 * @param userCatchCashDetail
	 */
	void updateAvailableById(String id);

	/**
	 * 获取现金列表
	 * 
	 * @param userId
	 * @return
	 */
	List getDetails(String userId);

	/**
	 * 计算总现金
	 * 
	 * @param userId
	 * @return
	 */
	double getCashSum(String userId);
}
