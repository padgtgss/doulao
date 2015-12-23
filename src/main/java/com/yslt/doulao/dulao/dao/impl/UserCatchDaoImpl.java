package com.yslt.doulao.dulao.dao.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import common.util.db.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import com.yslt.doulao.dulao.dao.UserCatchDao;
import com.yslt.doulao.dulao.entity.UserCatch;
import common.var.constants.SystemConstant;

/**
 * @Description: UserCatchDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
@Repository
public class UserCatchDaoImpl extends BaseDaoImpl implements UserCatchDao {

	public UserCatch getByUser(String userId) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId",userId);
		params.put("date",new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date()));
		List<UserCatch> userCatchList = this.getEntityByFieldList(UserCatch.class, params);

		if (userCatchList != null && userCatchList.size() > 0) {
			return userCatchList.get(0);
		}else {
			UserCatch userCatch = new UserCatch();
			userCatch.setUserId(userId);
			userCatch.setCatchSum(0);
			userCatch.setDate(new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date()));
			this.save(userCatch);
			return userCatch;
		}

	}

	@Override
	public void addCatchSum(UserCatch userCatch) {
		UpdateOperations<UserCatch> updateOperations = ds.createUpdateOperations(UserCatch.class).inc("catchSum",1);
		Query<UserCatch> query = ds.createQuery(UserCatch.class);
		query.filter("userId",userCatch.getUserId());
		query.filter("date",new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date()));
		ds.update(query,updateOperations);
	}

}
