package com.yslt.doulao.info.dao.impl;

import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.info.dao.DiamondDao;
import com.yslt.doulao.info.entity.Diamond;

import common.util.db.DetaDiv;

/**
 * @Description: DiamondDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-13
 */
@Repository
public class DiamondDaoImpl implements DiamondDao {

	private final String TABLE_NAME = "DiamondTable";

	@Override
	public Diamond getByLeave(int leave) {
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		DBObject dbObject = collection.findOne(new BasicDBObject("dLevelType", leave));
		Diamond diamond = new Diamond();
		diamond.setId(dbObject.get("_id").toString());
		diamond.setMaxExp(dbObject.get("maxExp").toString());
		diamond.setMaxVitality(dbObject.get("maxVitality").toString());
		diamond.setMaxSmallCash(Double.parseDouble(dbObject.get("maxSmallCash").toString()));
		diamond.setRedTimes(Integer.parseInt(dbObject.get("redTimes").toString()));
		diamond.setScale(dbObject.get("scale").toString());
		return diamond;
	}
}
