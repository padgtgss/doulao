package com.yslt.doulao.info.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.info.dao.UserActiveDao;
import com.yslt.doulao.info.entity.UserActive;

import common.util.db.DetaDiv;
import common.util.db.MonDB;
import common.var.constants.SystemConstant;

/**
 * @Description: UserActiveDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-13
 */
@Repository
public class UserActiveDaoImpl implements UserActiveDao {

	private final String TABLE_NAME = "UserActive";
	@Resource
	private MonDB monDB;

	@Override
	public UserActive save(UserActive userActive) {
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("userId", userActive.getUserId());
		dbObject.put("createTime", new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date()));
		dbObject.put("upTime", new Date().getTime());
		dbObject.put("decayEndTime", userActive.getDecayEndTime());// 上一次衰减计算截止时间
		dbObject.put("postCount", userActive.getPostCount());
		dbObject.put("active", userActive.getActive());
		collection.insert(dbObject);
		userActive.setId(dbObject.get("_id").toString());
		userActive.setCreateTime(dbObject.get("createTime").toString());
		userActive.setUpTime(dbObject.get("upTime").toString());
		userActive.setActive(dbObject.get("active").toString());
		userActive.setPostCount(Double.parseDouble(dbObject.get("postCount").toString()));
		return userActive;
	}

	@Override
	public void update(UserActive userActive) {

	}

	@Override
	public UserActive get(UserActive userActive) {
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("userId", userActive.getUserId());
		dbObject.put("createTime", new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date()));
		DBObject one = collection.findOne(dbObject);
		if (one != null) {
			userActive.setId(one.get("_id").toString());
			userActive.setCreateTime(one.get("createTime").toString());
			userActive.setUpTime(one.get("upTime").toString());
			userActive.setActive(one.get("active").toString());
			userActive.setPostCount(Double.parseDouble(one.get("postCount").toString()));
			return userActive;
		}
		return null;
	}

	@Override
	public void updateActive(String userId, String active) {
		UserActive userActive = new UserActive();
		userActive.setUserId(userId);
		userActive.setPostCount(Double.valueOf(active));
		UserActive _userActive = this.get(userActive);
		if (_userActive != null) {
			monDB.update(TABLE_NAME, new BasicDBObject("_id", ObjectId.massageToObjectId(_userActive.getId())),
					new BasicDBObject("postCount", active));
		} else {
			this.save(userActive);
		}
	}
}
