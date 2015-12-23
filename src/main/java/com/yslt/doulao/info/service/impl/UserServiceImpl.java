package com.yslt.doulao.info.service.impl;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import common.util.HttpUtil;
import common.util.MD5Util;
import common.var.constants.SystemConstant;
import org.springframework.stereotype.Service;

import com.yslt.doulao.info.dao.UserDao;
import com.yslt.doulao.info.service.UserService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: UserServiceImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userDao;

	@Override
	public int getBrickLeave(String userId) {
		return userDao.getBrickLeave(userId);
	}

	public void decreaseExp(String userId, String exp) {
		userDao.updateExperience(userId, exp);
	}

	@Override
	public String getCashDetail(String userId, String recordtype) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		MD5Util md5 = new MD5Util();
		String hash = "jhgsdljgpdf53sd4f3f46sdf34s";
		map.put("username", userDao.getById(userId).getMobile());
		map.put("action", "smallcashrecords");
		map.put("appkey", "X7J28D9CK3");
		map.put("recordtype",recordtype);
		map.put("hash", hash);

		String json = HttpUtil.post(SystemConstant.PHP_API_URL, map);
		JSONObject jsonObject = JSONObject.parseObject(json);
		return  jsonObject.get("data").toString();


	}
}
