package com.yslt.doulao.info.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.yslt.doulao.info.dao.GoldRecordDao;
import com.yslt.doulao.info.entity.GoldRecord;

import common.util.db.MonDB;
import common.var.constants.SystemConstant;

/**
 * @Description: GoldRecordDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-12
 */
@Repository
public class GoldRecordDaoImpl implements GoldRecordDao {
	private final String TABLE_NAME = "GoldRecord";

	@Resource
	private MonDB monDB;

	@Override
	public void save(GoldRecord goldRecord) {
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("userId", goldRecord.getUserId());
		dbObject.put("countent", goldRecord.getCountent());
		dbObject.put("message", goldRecord.getMessage());
		dbObject.put("time", new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT).format(new Date()));
		dbObject.put("type", Integer.parseInt(goldRecord.getTypeId()));
		monDB.insertOb(TABLE_NAME, dbObject);
	}
}
