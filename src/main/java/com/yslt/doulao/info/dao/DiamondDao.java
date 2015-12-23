package com.yslt.doulao.info.dao;

import com.yslt.doulao.info.entity.Diamond;

/**
 * @Description: 钻卡基础信息DAO
 * @anthor: shi_lin
 * @CreateTime: 2015-11-13
 */
public interface DiamondDao {

	/**
	 * 根据用户转卡等级，获取转卡特权
	 * @param leave
	 * @return
	 */
	Diamond getByLeave(int leave);
}
