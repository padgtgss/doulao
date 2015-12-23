package com.yslt.doulao.dulao.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.dulao.dao.UserOneCatchDao;
import com.yslt.doulao.dulao.entity.UserOneCatch;

import common.util.db.DetaDiv;
import common.var.constants.SystemConstant;

/**
 * @Description: UserOneCatchDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
@Repository
public class UserOneCatchDaoImpl implements UserOneCatchDao {
	private final String TABLE_NAME = "user_one_catch";

	/*@Override
	public UserOneCatch get(String userId) {

		DBCollection dbCollection = DetaDiv.getCollection(TABLE_NAME);
		DBObject query = new BasicDBObject();
		query.put("userId", userId);
		query.put("date", new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date()));
		DBObject dbObject = dbCollection.findOne(query);
		if (dbObject == null) {
			return null;
		}
		UserOneCatch userOneCatch = new UserOneCatch();
		userOneCatch.setCatchSum(Integer.parseInt(dbObject.get("catchSum").toString()));
		userOneCatch.setMoney(Float.valueOf(dbObject.get("money").toString()));
		return userOneCatch;
	}

	@Override
	public void update(String userId) {

	}*/

}
