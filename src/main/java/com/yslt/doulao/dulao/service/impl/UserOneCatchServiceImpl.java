package com.yslt.doulao.dulao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yslt.doulao.dulao.dao.UserOneCatchDao;
import com.yslt.doulao.dulao.entity.UserOneCatch;
import com.yslt.doulao.dulao.service.UserOneCatchService;

/**
 * @Description: UserOneCatchServiceImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
@Service
public class UserOneCatchServiceImpl implements UserOneCatchService {
	@Resource
	private UserOneCatchDao userOneCatchDao;

	/*@Override
	public UserOneCatch get(String userId) {
		UserOneCatch userOneCatch = userOneCatchDao.get(userId);
		if (userOneCatch == null) {
			userOneCatch = new UserOneCatch();
			userOneCatch.setMoney(Float.valueOf(0L));
			userOneCatch.setCatchSum(0);
		}
		return userOneCatch;
	}*/
}
