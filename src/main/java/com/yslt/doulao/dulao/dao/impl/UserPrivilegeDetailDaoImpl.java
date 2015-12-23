package com.yslt.doulao.dulao.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.yslt.doulao.dulao.dao.UserPrivilegeDetailDao;
import com.yslt.doulao.dulao.entity.UserPrivilegeDetail;

import common.util.db.DetaDiv;
import common.var.constants.SystemConstant;

/**
 * @Description: UserPrivilegeDetailDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-12
 */
@Repository
public class UserPrivilegeDetailDaoImpl implements UserPrivilegeDetailDao {

	private final String TABLE_NAME = "user_privilege_detail";

	@Override
	public void save(UserPrivilegeDetail userPrivilegeDetail) {
		DBCollection userPrivilegeTable = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("userId", userPrivilegeDetail.getUserId());
		basicDBObject.put("typeId", userPrivilegeDetail.getTypeId());
		basicDBObject.put("date", new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date()));
		basicDBObject.put("available", 1);
		userPrivilegeTable.insert(basicDBObject);
	}
}
