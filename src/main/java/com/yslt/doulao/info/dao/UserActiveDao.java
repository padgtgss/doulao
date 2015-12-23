package com.yslt.doulao.info.dao;

import com.yslt.doulao.info.entity.UserActive;

/**
 * @Description: UserActiveDao
 * @anthor: shi_lin
 * @CreateTime: 2015-11-13
 */
public interface UserActiveDao {

	UserActive save(UserActive userActive);

	void update(UserActive userActive);

	UserActive get(UserActive userActive);

	/**
	 * 更新用户活跃度
	 * 
	 * @param active
	 */
	void updateActive(String userId, String active);
}
