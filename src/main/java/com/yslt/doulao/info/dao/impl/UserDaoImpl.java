package com.yslt.doulao.info.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.yslt.doulao.info.entity.User;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yslt.doulao.info.dao.UserDao;

import common.util.db.DetaDiv;
import common.util.db.MonDB;
import common.var.constants.SystemConstant;

/**
 * @Description: UserDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
@Repository
public class UserDaoImpl implements UserDao {
	private final String TABLE_NAME = "AuraUser";
	@Resource
	private MonDB monDB;

	@Override
	public int getBrickLeave(String userId) {

		int drLevel = -1; // 普通用户 默认枚举值
		try {
			DBCollection udr = DetaDiv.getCollection("t_userDr");
			DBObject where = new BasicDBObject();
			where.put("userId", userId);
			where.put("activeTime", new BasicDBObject("$gte",
					new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date())));
			DBCursor cursor = udr.find(where).sort(new BasicDBObject("drLevel", -1));
			if (cursor.size() > 0 && cursor != null) {
				drLevel = Integer.parseInt(cursor.next().get("drLevel").toString());
			}
			return drLevel;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drLevel;
	}

	@Override
	public void updateExperience(String userId, String experience) {
		monDB.update(TABLE_NAME, new BasicDBObject("_id", userId),
				new BasicDBObject("exp", Integer.parseInt(experience)), 1);
	}

	@Override
	public String getUserLeave(String userId) {
		DBCollection user = DetaDiv.getCollection(TABLE_NAME);
		DBObject where = new BasicDBObject();
		where.put("_id", userId);
		DBObject one = user.findOne(where);
		return  one == null ? null : one.get("uvLevel").toString();
	}

	@Override
	public String getUserMobile(String userId) {
		DBCollection user = DetaDiv.getCollection(TABLE_NAME);
		DBObject where = new BasicDBObject();
		where.put("_id", userId);
		DBObject one = user.findOne(where);
		return  one == null ? null : one.get("mobile").toString();
	}

	@Override
	public User getById(String userId) {
		User user = new User();
		DBCollection db = DetaDiv.getCollection(TABLE_NAME);
		DBObject where = new BasicDBObject();
		where.put("_id", userId);
		DBObject one = db.findOne(where);
		if (one != null){
			user.setId(one.get("_id").toString());
			user.setExperience(one.get("exp").toString());
			user.setMobile(one.get("mobile").toString());
			user.setUserLeave(one.get("uvLevel").toString());

		}
		return user;
	}
}
