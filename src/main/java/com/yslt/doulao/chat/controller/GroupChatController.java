package com.yslt.doulao.chat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;
import com.yslt.doulao.chat.service.GroupChatService;
import com.yslt.doulao.chat.service.GroupFunctionService;
import com.yslt.doulao.dulao.pojo.CashPojo;
import common.util.HttpUtil;
import common.util.MD5Util;
import common.util.db.DetaDiv;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/groupchat")
public class GroupChatController {

	@Resource
	private GroupChatService groupChatService;

	//创建群接口
	@RequestMapping(value = "/createGroup", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object createGroup(String groupMaster, String[] groupMember,String categoryCode,String type,String groupName,
			String permission,String declared,String imgPath) {
		Map<String,Object> resultMap = groupChatService.createGroup(groupName, groupMaster, groupMember, categoryCode, type, permission, declared,imgPath);
		return JSON.toJSONString(resultMap);
	}

	//修改群接口
	@RequestMapping(value = "/modifyGroup", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object modifyGroup(String groupId, String userId ,String permission,String groupName,
							  String declared,String categoryCode, String imgPath
			) {
		Map<String,Object> resultMap = groupChatService.modifyGroup(groupId, userId, permission,groupName, declared, categoryCode, imgPath);
		return JSON.toJSONString(resultMap);
	}

	//个人申请加入群接口
	@RequestMapping(value = "/joinGroup", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object joinGroup(String groupId, String userId, String declared) {
		Map<String,Object> resultMap = groupChatService.joinGroup( groupId, userId, declared);
		return JSON.toJSONString(resultMap);
	}

	//获取群资料接口
	@RequestMapping(value = "/groupInfo", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object groupInfo(String groupId) {
		Map<String,Object> resultMap = groupChatService.getGroupInfo(groupId);
		return JSON.toJSONString(resultMap);
	}

	//退出群接口
	@RequestMapping(value = "/logoutGroup", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object logoutGroup(String groupId, String userId) {
		Map<String,Object> resultMap = groupChatService.logoutGroup(groupId,userId);
		return JSON.toJSONString(resultMap);
	}

	//批量邀请加入群接口
	@RequestMapping(value = "/inviteGroup", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object inviteGroup(String groupId, String senderId,String[] receiverIds,String confirm) {
		Map<String,Object> resultMap = groupChatService.inviteGroup(groupId,senderId,receiverIds,confirm);
		return JSON.toJSONString(resultMap);
	}

	//批量确认用户加入群接口
	@RequestMapping(value = "/letJoinGroup", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object letJoinGroup(String groupId, String senderId,String[] receiverIds) {
		Map<String,Object> resultMap = groupChatService.letJoinGroup(groupId,senderId,receiverIds);
		return JSON.toJSONString(resultMap);
	}

	//我创建的群列表接口
	@RequestMapping(value = "/myGroupList", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object myGroupList(String userId) {
		Map<String,Object> resultMap = groupChatService.myGroupList(userId);
		return JSON.toJSONString(resultMap);
	}

	//我加入的群（我不是群主）
	@RequestMapping(value = "/IAmMember", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object IAmMember(String userId) {
		Map<String,Object> resultMap = groupChatService.IAmMember(userId);
		return JSON.toJSONString(resultMap);
	}

	//我在群内的群列表接口（整合了上面两个接口）
	@RequestMapping(value = "/groupAboutMe", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object groupAboutMe(String userId) {
		Map<String,Object> resultMap = groupChatService.groupAboutMe(userId);
		return JSON.toJSONString(resultMap);
	}

	//解散群接口
	@RequestMapping(value = "/deleteGroup", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object deleteGroup(String groupId,String userId) {
		Map<String,Object> resultMap = groupChatService.deleteGroup(groupId, userId);
		return JSON.toJSONString(resultMap);
	}

	//批量删除群成员
	@RequestMapping(value = "/deleteGroupMember", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object deleteGroup(String groupId,String userId, String[] memberIds) {
		Map<String,Object> resultMap = groupChatService.deleteGroupMember(groupId, userId,memberIds);
		return JSON.toJSONString(resultMap);
	}

	//群主处理申请
	@RequestMapping(value = "/askJoin", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object askJoin(String groupId,String masterId,String askerId, String confirm) {
		Map<String,Object> resultMap = groupChatService.askJoin(groupId, masterId, askerId,confirm);
		return JSON.toJSONString(resultMap);
	}

	//查询群
	@RequestMapping(value = "/queryGroup", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object queryGroup(String queryContent) {
		Map<String,Object> resultMap = groupChatService.queryGroup(queryContent);
		return JSON.toJSONString(resultMap);
	}

	//修改群名片
	@RequestMapping(value = "/modifyGroupMember", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object modifyGroupMember(String groupId,String userId,String remarkName) {
		Map<String,Object> resultMap = groupChatService.modifyGroupMember(groupId,userId,remarkName);
		return JSON.toJSONString(resultMap);
	}

	//禁言接口
	@RequestMapping(value = "/forbidSpeak", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object forbidSpeak(String groupId,String masterId,String memberId,String confirm) {
		Map<String,Object> resultMap = groupChatService.forbidSpeak(groupId, masterId, memberId, confirm);
		return JSON.toJSONString(resultMap);
	}

	//用户设置群消息接收规则
	@RequestMapping(value = "/setGroupMsg", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object setGroupMsg(String groupId,String memberId, String rule) {
		Map<String,Object> resultMap = groupChatService.setGroupMsg(groupId,memberId,rule);
		return JSON.toJSONString(resultMap);
	}

	//查询群组成员（从容联那边去查询）
	@RequestMapping(value="/queryMember", method = {RequestMethod.GET,RequestMethod.POST},produces = {
			"application/json;charset=UTF-8"})
	@ResponseBody
	public Object queryMember(String groupId,String userId){
		Map<String,Object> resultMap = groupChatService.queryMember(groupId,userId);
		return JSON.toJSONString(resultMap);
	}

	//查询群组属性(从容联那边去查询)
	@RequestMapping(value="/queryGroupDetail", method = {RequestMethod.GET,RequestMethod.POST},produces = {
			"application/json;charset=UTF-8"})
	@ResponseBody
	public Object queryGroupDetail(String groupId,String userId){
		Map<String,Object> resultMap = groupChatService.queryGroupDetail(groupId,userId);
		return JSON.toJSONString(resultMap);
	}

	//纯测试接口
	@RequestMapping(value = "/test", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object test() {
		Map<String,Object> resultMap = new HashMap<String, Object>();
			DBCollection userTalbe = DetaDiv.getCollection("AuraUser");
			BasicDBObject where = new BasicDBObject();
			where.put("mobile", "18100800685");
			DBObject userObject = userTalbe.findOne(where);
			System.out.println(userObject.get("_id").toString());
			resultMap.put("result",1);
			resultMap.put("_id",userObject.get("_id").toString());
			return JSON.toJSONString(resultMap);
	}
	//纯测试接口
	@RequestMapping(value = "/test1", method = { RequestMethod.GET, RequestMethod.POST },produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object test1(String userId) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
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
		System.out.println(jsonObject2.getString("cash"));
		System.out.println(jsonObject2.getString("lls"));
		System.out.println(jsonObject2.getString("smallCash"));
		System.out.println(jsonObject2.getString("llq"));
		return JSON.toJSONString(resultMap);
	}

}
