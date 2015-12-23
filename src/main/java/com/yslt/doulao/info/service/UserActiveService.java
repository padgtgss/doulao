package com.yslt.doulao.info.service;

/**
 * @Description: UserActiveService
 * @anthor: shi_lin
 * @CreateTime: 2015-11-13
 */

public interface UserActiveService {

	/**
	 * 根据捞一捞 活跃度卡 增加活跃度
	 * 
	 * @param userId
	 */
	void updateByPrivilegeCard(String userId);
}
