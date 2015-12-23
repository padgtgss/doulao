package com.yslt.doulao.info.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

@Controller
@RequestMapping("/contacts")
public class ContactsController {

	@RequestMapping(value = "/contactsInfo", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object contactsInfo(String userId) {
		Map map = ImmutableMap.of("name", "jack");
		return JSON.toJSONString(map);
	}
}
