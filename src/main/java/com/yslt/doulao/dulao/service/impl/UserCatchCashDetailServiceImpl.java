package com.yslt.doulao.dulao.service.impl;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.yslt.doulao.dulao.pojo.CashPojo;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.dulao.dao.UserCatchCashDetailDao;
import com.yslt.doulao.dulao.service.UserCatchCashDetailService;
import common.util.HttpUtil;
import common.util.MD5Util;
import common.util.db.DetaDiv;

/**
 * @Description: UserCatchCashDetailServiceImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-12
 */
@Service
public class UserCatchCashDetailServiceImpl implements UserCatchCashDetailService {

	@Resource
	private UserCatchCashDetailDao userCatchCashDetailDao;

	@Override
	public List getDetails(String userId,String recordtype) {



	return null;
		//return userCatchCashDetailDao.getDetails(userId,recordtype);
	}

	//获取smallCash和Cash和lls
	@Override
	public CashPojo getCash(String userId) {
		double smallCash = 0;
		double cash = 0;
		DBCollection userTable = DetaDiv.getCollection("AuraUser");
		BasicDBObject where = new BasicDBObject();
		where.put("_id", userId);
		DBObject dbObject = userTable.findOne(where);
		if(dbObject == null){
			return null;
		}
		String mobile = dbObject.get("mobile").toString();
		MD5Util md5 = new MD5Util();
		String hash = md5.getMD5(mobile+"cashAndCoinBalance");
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", mobile));
		params.add(new BasicNameValuePair("action", "cashAndCoinBalance"));
		params.add(new BasicNameValuePair("appkey", "X7J28D9CK3"));
		params.add(new BasicNameValuePair("hash", hash));
		HttpUtil httpUtil = new HttpUtil();
		String s = httpUtil.getResponse2(params, "balance");
		s.replaceAll("\"", "\\\"");
		JSONObject jsonObject = JSONObject.parseObject(s);
		JSONObject jsonObject2 = jsonObject.getJSONObject("data");
		CashPojo cashPojo = new CashPojo();
		cashPojo.setCash(jsonObject2.getString("cash"));
		cashPojo.setLls(jsonObject2.getString("lls"));
		cashPojo.setSmallCash(jsonObject2.getString("smallCash"));
		cashPojo.setLlq(jsonObject2.getString("llq"));
		return cashPojo;
	}

}
