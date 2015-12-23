package com.yslt.doulao.dulao.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.yslt.doulao.dulao.pojo.OneCatchSharePojo;
import com.yslt.doulao.dulao.service.OneCatchService;

/**
 * @Description: 捞一捞
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
@Controller
@RequestMapping("/X20151118")
public class OneCatchController {

	@Resource
	private OneCatchService oneCatchService;

	/**
	 * 转盘
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/turn", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object excute(String userId, int extraCost) {
		if (StringUtils.isBlank(userId)) {
			throw new RuntimeException("userId null");
		}
		return oneCatchService.excute(userId, extraCost);
	}

	/**
	 * 获取剩余次数
	 * 
	 * @return
	 */
	@RequestMapping(value = "/time", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object hasCatchTimes(String userId) {

		return JSON.toJSONString(ImmutableMap.of("result",oneCatchService.getOneCatchSum(userId)));
	}

	/**
	 * 确认转盘分享成功！
	 * 
	 * @param typeId
	 *            抽奖类型
	 * @param userId
	 *            目标Id
	 * @return
	 */
	@RequestMapping(value = "/share", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object callBack(int typeId, String userId, String description,String targetId) {
		OneCatchSharePojo oneCatchSharePojo = new OneCatchSharePojo(typeId, userId, description,targetId);
		return JSON.toJSONString(ImmutableMap.of("isChange", oneCatchService.callBackShare(oneCatchSharePojo)));
	}



}
