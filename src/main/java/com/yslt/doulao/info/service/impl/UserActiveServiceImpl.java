package com.yslt.doulao.info.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yslt.doulao.info.dao.DiamondDao;
import com.yslt.doulao.info.dao.UserActiveDao;
import com.yslt.doulao.info.dao.UserDao;
import com.yslt.doulao.info.service.UserActiveService;

/**
 * @Description: UserActiveServiceImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-13
 */
@Service
public class UserActiveServiceImpl implements UserActiveService {
	@Resource
	private UserActiveDao userActiveDao;

	@Resource
	private UserDao userDao;
	@Resource
	private DiamondDao diamondDao;

	@Override
	public void updateByPrivilegeCard(String userId) {
		// 获取钻卡最大活跃度
		int leave = userDao.getBrickLeave(userId);
		String active = diamondDao.getByLeave(leave).getMaxVitality();
		userActiveDao.updateActive(userId, active);
	}
}
