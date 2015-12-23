package com.yslt.doulao.dulao.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yslt.doulao.dulao.enums.CashFromEnum;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yslt.doulao.dulao.dao.UserCatchCashDetailDao;
import com.yslt.doulao.dulao.entity.UserCatchCashDetail;

import common.util.db.DetaDiv;
import common.util.db.MonDB;
import common.var.constants.SystemConstant;

/**
 * @Description: UserCatchCashDetailDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
@Repository
public class UserCatchCashDetailDaoImpl implements UserCatchCashDetailDao {

	private final String TABLE_NAME = "user_cash_detail";
	@Resource
	private MonDB mondb;

	@Override
	public double getTodayCash(String userId) {
		int cash = 0;
		DBCollection dbCollection = DetaDiv.getCollection(TABLE_NAME);
		DBObject query = new BasicDBObject();
		query.put("userId", userId);
		query.put("time", new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT).format(new Date()));
		query.put("available", 1);
		DBCursor dbCursor = dbCollection.find(query);
		if (dbCursor == null) {
			return cash;
		}
		while (dbCursor.hasNext()) {
			DBObject next = dbCursor.next();
			cash += Double.parseDouble(next.get("countent").toString());
		}

		return cash;
	}

	@Override
	public String save(UserCatchCashDetail userCatchCashDetail) {
		DBObject dbObject = new BasicDBObject();
		dbObject.put("userId", userCatchCashDetail.getUserId());
		dbObject.put("time", new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date()));
		dbObject.put("countent", userCatchCashDetail.getCountent());
		dbObject.put("message", userCatchCashDetail.getMessage());
		dbObject.put("fromType",userCatchCashDetail.getCashFromEnum().getTypeId());
		dbObject.put("available", userCatchCashDetail.getAvailable());
		dbObject.put("_id",userCatchCashDetail.getId());
		mondb.insertOb(TABLE_NAME, dbObject);
		return dbObject.get("_id").toString();
	}

	@Override
	public void updateAvailableById(String id) {
		 mondb.update(TABLE_NAME,new  BasicDBObject("_id",id),new
		 BasicDBObject("available",1));
	}

	@Override
	public List getDetails(String userId) {
		DBCollection dbCollection = DetaDiv.getCollection(TABLE_NAME);
		DBObject query = new BasicDBObject();
		query.put("userId", userId);
		DBCursor dbCursor = dbCollection.find(query);
		List<UserCatchCashDetail> list = new ArrayList<UserCatchCashDetail>();
		UserCatchCashDetail detail = null;
		while (dbCursor.hasNext()) {
			DBObject dbObject = dbCursor.next();
			detail = new UserCatchCashDetail();
			detail.setTime(dbObject.get("time").toString());
			detail.setUserId(dbObject.get("userId").toString());
			detail.setCountent(Double.parseDouble(dbObject.get("countent").toString()));
			detail.setMessage(dbObject.get("message").toString());
			detail.setCashFromEnum(CashFromEnum.getByNumber(Integer.parseInt(dbObject.get("fromType").toString())));
			list.add(detail);
		}
		return list;
	}

	@Override
	public double getCashSum(String userId) {
		double cash = 0;
		DBCollection dbCollection = DetaDiv.getCollection(TABLE_NAME);
		DBObject query = new BasicDBObject();
		query.put("userId", userId);
		DBCursor dbCursor = dbCollection.find(query);
		while (dbCursor.hasNext()) {
			cash += Integer.parseInt(dbCursor.next().get("countent").toString());
		}
		return cash;
	}
}
