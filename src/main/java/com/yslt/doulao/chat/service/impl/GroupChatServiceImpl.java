package com.yslt.doulao.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.chat.dao.GroupDao;
import com.yslt.doulao.chat.dao.GroupMemberDao;
import com.yslt.doulao.chat.dao.SubAccountsDao;
import com.yslt.doulao.chat.dao.impl.SubAccountsDaoImpl;
import com.yslt.doulao.chat.entity.Group;
import com.yslt.doulao.chat.entity.GroupMember;
import com.yslt.doulao.chat.entity.SubAccounts;
import com.yslt.doulao.chat.service.GroupChatService;
import common.entity.BaseBusinessException;
import common.util.*;
import common.util.db.DetaDiv;
import common.util.db.MonDB;
import common.var.constants.GroupConstant;
import common.var.constants.SystemConstant;
import common.var.exception.GroupError;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GroupChatServiceImpl implements GroupChatService{

	@Resource
	private GroupDao groupDao;
	
	@Resource
	private GroupMemberDao groupMemberDao;

	@Resource
	private SubAccountsDao subAccountsDao;

	/**
	 *
	 * @param groupName         （容联）群名称
	 * @param groupMaster （容联）群主
	 * @param groupMember （容联）群成员
	 * @param categoryCode 群分类代号
	 * @param type （容联）群类型（群组类型
						0：临时组(上限100人)
						1：普通组(上限300人)
						2：普通组(上限500人)
						3：付费普通组 (上限1000人)
						4：付费VIP组（上限2000人））
	 * @param permission （容联）申请加入模式
						0：默认直接加入
						1：需要身份验证
						2：私有群组
	 * @param declared （容联）群公告
	 * @param imgPath 群头像相对路径
	 * @return
	 */
	@Override
	public Map<String,Object> createGroup(String groupName, String groupMaster, String[] groupMember,
			String categoryCode, String type, String permission, String declared,String imgPath) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//检查群名称参数groupName
		if(groupName==null || "".equals(groupName)){
			String screenName = ((DBObject)AuraUserUtil.getDbObjectByUserId(groupMaster).get("profile")).get("screenName").toString();
			groupName = screenName+"的群";
		}
		if(groupName.length()>50){
			groupName = groupName.substring(0, 50);
		}
		//检查群主参数groupMaster
			//检查参数是否为空
		if(groupMaster==null || "".equals(groupMaster)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//userIdList用于创建群成功后，线程里面发送群提示
		List<String> userIdList = new ArrayList<>();
			//检查是否有群主这个用户
		if(AuraUserUtil.getDbObjectByUserId(groupMaster)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		userIdList.add(groupMaster);
			//检查建群数是否超过限制
			//用户的钻卡等级
		int drLevel = AuraUserUtil.getUserDiamondLevel(groupMaster);
			//钻卡等级对应的最大建群数
		int limitGroupNum = AuraUserUtil.getCreateGroupLimitNum(drLevel);
			//已经创建的群数
		int createdGroupNum = GroupUtil.getCreateGroupNum(groupMaster);
			//判断
		if(createdGroupNum>=limitGroupNum){
			resultMap.put("result",0);
			resultMap.put("message","超过建群数限制");
			return resultMap;
		}
		//检查成员分类参数groupMember
		if(groupMember == null || "".equals(groupMember) ){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		if(groupMember.length<2){
			throw new BaseBusinessException(GroupError.BATCH_PARAM_TOO_SHORT);
		}
		if(groupMember.length>=2){
			for(String userId:groupMember){
				if(AuraUserUtil.getDbObjectByUserId(userId)==null){
					throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
				}
				userIdList.add(userId);
			}
		}
		//检查群分类代号参数categoryCode
		if(categoryCode==null || "".equals(categoryCode)){
			categoryCode="0";
		}
		DBCollection collection = DetaDiv.getCollection("t_groupCategory");
		BasicDBObject where = new BasicDBObject();
		where.put("gcCode", categoryCode);
		int findCount = collection.find(where).count();
		if(findCount<=0){
			throw new BaseBusinessException(GroupError.CATEGORY_NOT_EXISTS);
		}
		//检查群类型参数type
		if(!"0".equals(type)&& !"1".equals(type)&& !"2".equals(type)){
			throw new BaseBusinessException(GroupError.TYPE_NOT_EXISTS);
		}

		if(SystemConstant.FOMRAL_ACCOUNT_USERID.equals(groupMaster)){//官方帐号
			type="2";
		}else if(drLevel==4 && "2".equals(type)){//黑钻用户
			resultMap.put("result",0);
			resultMap.put("message","黑钻不能创建500人群");
			return resultMap;
		}else if(drLevel!=4 && "1".equals(type) && "2".equals(type)){//非黑钻用户
			resultMap.put("result",0);
			resultMap.put("message","非黑钻只能创建100人群");
			return resultMap;
		}
		//检查申请加入模式参数permission
		if(!"0".equals(permission)&& !"1".equals(permission)&& !"2".equals(permission)){
			throw new BaseBusinessException(GroupError.PERMISSION_NOT_EXISTS);
		}
		//检查群公告参数declared
		if(declared==null){
			declared = "";
		}
		if(declared.length()>200){
			declared = declared.substring(0, 200);
		}
		//检查群头像
		if(imgPath==null){
			imgPath = "";
		}
		if (imgPath.startsWith("http")){
			throw new BaseBusinessException(GroupError.FORMAT_URL_NOT_CORRECT);
		}
		//子账户相关
		/*SubAccounts subAccounts = getSubAccount(groupMaster);
		if (subAccounts==null){
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}
		String subAccountSid = subAccounts.getSubAccountSid();
		String subToken = subAccounts.getSubToken();*/
		//设置请求包体
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name",groupName);
		map.put("type", type);
		map.put("permission", permission);
		map.put("declard", declared);
		map.put("type", type);
		//向容联发送建群请求
		String result2 = HttpUtil.pushGroupUrlByMap(map, groupMaster, GroupConstant.REQUEST_METHOD_CREATE_GROUP);
		JSONObject jsonObject2 = JSONObject.parseObject(result2);
		String statusCode2 = jsonObject2.getString("statusCode");
		if (!"000000".equals(statusCode2)){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",statusCode2);
			return resultMap;
		}
		//容联创建群成功
		Group group = new Group();
		String groupId = jsonObject2.getString("groupId");
		group.setId(groupId);
		//产生群号
		String groupNum = RandomUtil.createRandomNumStr(8, 4);
		//群号去重
		while (GroupUtil.checkIfHavaGroupByGroupNum(groupNum)!=null){
			groupNum = RandomUtil.createRandomNumStr(8, 4);
		}
		group.setGroupNum(groupNum);
		group.setGroupName(groupName);
		group.setMaster(groupMaster);
		group.setCategoryCode(categoryCode);
		group.setType(type);
		group.setPermission(permission);
		group.setDeclared(declared);
		group.setImgPath(imgPath);
		if (SystemConstant.FOMRAL_ACCOUNT_USERID.equals(groupMaster)){
			group.setOfficialType("1");
		}else{
			group.setOfficialType("0");
		}
		group = groupDao.insert(group);
		//把用户加到群里面去, status为"1"表示成为群成员,confirm为"1"表示不需要【被邀请者】确认
		addMemberIntoGroup(groupMaster,groupId,"1","群主，建群时就加入的",groupMaster);
		addMembersIntoGroup(groupMember, group.getId(), "1", "建群时就加入的", groupMaster, "1");
		resultMap.put("result",1);
		resultMap.put("message","建群成功");
		resultMap.put("groupId",group.getId());
		resultMap.put("groupName",group.getGroupName());
		resultMap.put("groupNum",groupNum);
		//线程用于发送群提示
		MyRun myRun = new MyRun(groupId,userIdList,"0");
		Thread thread = new Thread(myRun);
		thread.start();
		return resultMap;
	}

	/**
	 * 批量加群（先经过容联）
	 * @param groupMember 批量加群的用户
	 * @param groupId 群id
	 * @param status 状态
	 * @param remark 备注/申请理由
	 * @return
	 */
	public List<GroupMember> addMembersIntoGroup(String[] groupMember,String groupId,String status,String remark,String inviterId,String ifConfirm){
		List<GroupMember> groupMemberList = new ArrayList<GroupMember>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
			//获取子账户
			/*SubAccounts subAccounts = getSubAccount(inviterId);
			if(subAccounts==null){
				throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
			}
			String subAccountSid = subAccounts.getSubAccountSid();
			String subToken = subAccounts.getSubToken();*/
			//获取昵称
			String screenName = ((DBObject)AuraUserUtil.getDbObjectByUserId(inviterId).get("profile")).get("screenName").toString();
			//获取群名称
			String groupName = GroupUtil.checkIfHavaGroup(groupId).get("groupName").toString();
			//邀请理由
			String inviteCause = screenName+"邀请您加入："+groupName;
			//生成请求包体，第三个参数confirm解释：0->需要【被邀请者】确认，1->不需要【被邀请者】确认。
			String xmlStr = FormatStructure.objectToXml(groupId,groupMember,ifConfirm,inviteCause);
			//向容联发送管理员邀请用户加入群的请求
			String result = HttpUtil.pushGroupUrlByObject(xmlStr, inviterId, GroupConstant.REQUEST_METHOD_INVITE_USER);
			JSONObject jsonObject2 = JSONObject.parseObject(result);
			String statusCode = jsonObject2.getString("statusCode");
			if (!"000000".equals(statusCode)){
				throw new BaseBusinessException(GroupError.RL_NOT_000000);
			}
			for(String memberId : groupMember){
				GroupMember gm = new GroupMember();
				gm.setGroupId(groupId);
				gm.setMemberId(memberId);
				gm.setRemark(remark);
				gm.setInviterId(inviterId);
				gm.setStatus(status);
				BasicDBObject doc = new BasicDBObject();
				doc.put("setUpReceive","0");
				doc.put("setUpTalk","0");
				gm.setSettings(doc);
				//根据groupId和userId查询是否有群-成员数据
				DBObject dbObject = GroupUtil.checkIfInsertGroupMember(groupId, memberId);
				if(dbObject==null){
					gm = groupMemberDao.insert(gm);
				}else{
					gm.setId(dbObject.get("_id").toString());
					int k = groupMemberDao.update(gm);
					gm = groupMemberDao.findOne(gm);
				}
				if(gm.getId()==null){
					throw new BaseBusinessException(GroupError.NO_ID);
				}
				groupMemberList.add(gm);
			}
			return groupMemberList;

	}

	/**
	 * 批量加群（不经过容联，直接进数据库）
	 * @param groupMember 批量加群的用户
	 * @param groupId 群id
	 * @param status 状态
	 * @param remark 备注/申请理由
	 * @return
	 */
	public List<GroupMember> addMembersIntoGroupOnDB(String[] groupMember,String groupId,String status,String remark,String inviterId){
		List<GroupMember> groupMemberList = new ArrayList<GroupMember>();



			//获取子账户
			/*SubAccounts subAccounts = getSubAccount(inviterId);
			if(subAccounts==null){
				throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
			}
			String subAccountSid = subAccounts.getSubAccountSid();
			String subToken = subAccounts.getSubToken();*/
			//获取昵称
			//String screenName = ((DBObject)AuraUserUtil.getDbObjectByUserId(inviterId).get("profile")).get("screenName").toString();
			//获取群名称
			String groupName = GroupUtil.checkIfHavaGroup(groupId).get("groupName").toString();
			//邀请理由
			//String inviteCause = screenName+"邀请您加入："+groupName;
			/*//生成请求包体，第三个参数confirm解释：0->需要【被邀请者】确认，1->不需要【被邀请者】确认。
			String xmlStr = FormatStructure.objectToXml(groupId,groupMember,ifConfirm,inviteCause);
			//向容联发送管理员邀请用户加入群的请求
			String result = HttpUtil.pushGroupUrlByObject(xmlStr, subAccountSid, subToken, GroupConstant.REQUEST_METHOD_INVITE_USER);
			JSONObject jsonObject2 = JSONObject.parseObject(result);
			String statusCode = jsonObject2.getString("statusCode");
			if (!"000000".equals(statusCode)){
				throw new BaseBusinessException(GroupError.RL_NOT_000000);
			}*/
			for(String memberId : groupMember){
				GroupMember gm = new GroupMember();
				gm.setGroupId(groupId);
				gm.setMemberId(memberId);
				gm.setRemark(remark);
				gm.setInviterId(inviterId);
				gm.setStatus(status);
				//根据groupId和userId查询是否有群-成员数据
				DBObject dbObject = GroupUtil.checkIfInsertGroupMember(groupId, memberId);
				if(dbObject==null){
					gm = groupMemberDao.insert(gm);
				}else{
					gm.setId(dbObject.get("_id").toString());
					int k = groupMemberDao.update(gm);
					gm = groupMemberDao.findOne(gm);
				}
				if(gm.getId()==null){
					throw new BaseBusinessException(GroupError.NO_ID);
				}
				groupMemberList.add(gm);
			}
			return groupMemberList;
	}

	/**
	 * 单人加群
	 * @param userId 用户id
	 * @param groupId 群id
	 * @param status 传入的状态
	 * @param remark 备注/申请理由
	 * @return
	 */
	public GroupMember addMemberIntoGroup(String userId, String groupId,String status,String remark,String inviterId){
		GroupMember gm = new GroupMember();
		gm.setGroupId(groupId);
		gm.setMemberId(userId);
		gm.setRemark(remark);
		gm.setInviterId(inviterId);
		gm.setStatus(status);
		DBObject dbObject = GroupUtil.checkIfInsertGroupMember(groupId,userId);
		//根据groupId和userId查询是否有群-成员数据
		if(dbObject == null){
			BasicDBObject doc = new BasicDBObject();
			doc.put("setUpReceive","0");
			doc.put("setUpTalk","0");
			gm.setSettings(doc);
			groupMemberDao.insert(gm);
			gm = groupMemberDao.findOne(gm);
		}else{
			int k = groupMemberDao.update(gm);
			gm = groupMemberDao.findOne(gm);
		}
		if(gm.getId()==null){
			throw new BaseBusinessException(GroupError.NO_ID);
		}
		return gm;
	}

	/**
	 * 获取群信息
	 * @param groupId 群的id
	 * @return
	 */
	@Override
	public Map<String,Object> getGroupInfo(String groupId) {
		if(groupId==null){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBObject where = new BasicDBObject();
		where.put("_id",groupId);
		DBObject dbObject = collection.findOne(where);
		Map<String, Object> map = new HashMap<String, Object>();
		if(dbObject==null){
			map.put("result","0");
			map.put("message","无此群");
			return map;
		}
		//群基本信息
		map.put("groupId", dbObject.get("_id").toString());
		map.put("groupName",dbObject.get("groupName").toString());
		map.put("master",dbObject.get("master").toString());
		map.put("shamMaster",dbObject.get("shamMaster").toString());
		map.put("official",dbObject.get("shamMaster").toString());
		map.put("imgPath",SystemConstant.SERVER_IMAGE_PATH+dbObject.get("imgPath").toString());
		map.put("categoryCode",dbObject.get("categoryCode").toString());
		map.put("categoryName",GroupUtil.getNameByGroupCategory(dbObject.get("categoryCode").toString()));
		map.put("type",dbObject.get("type").toString());
		map.put("permission",dbObject.get("permission").toString());
		map.put("declared",dbObject.get("declared").toString());
		//群成员
		GroupMember gm = new GroupMember();
		gm.setGroupId(dbObject.get("_id").toString());
		gm.setStatus("1");
		List<GroupMember> gmList = groupMemberDao.findListIdNameImg(gm);
		map.put("members",gmList);
		return map;
	}

	/**
	 * 个人主动申请加入群
	 * @param groupId 群id
	 * @param userId 申请加入群的用户
	 * @param declared 申请理由
	 * @return
	 */
	@Override
	public Map<String,Object> joinGroup(String groupId, String userId, String declared) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//检查参数是否为空
		if(groupId==null || userId == null || "".equals(groupId) || "".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查处理申请理由declared
		if(declared!=null){
			if(declared.length()>50){
				declared = declared.substring(0,50);
			}
		}
		//检查用户是否存在
		if(AuraUserUtil.getDbObjectByUserId(userId)==null) {
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//检查群是否存在
		DBObject groupDBObject = GroupUtil.checkIfHavaGroup(groupId);
		if(groupDBObject==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查是否已经是群成员
		if(GroupUtil.checkIfIsGroupMember(groupId,userId)!=null){
			throw new BaseBusinessException(GroupError.ALREADY_IS_GROUPMEMBER);
		}
		//获取群的申请加入模式，来决定用户是否需要验证
		String permission = GroupUtil.checkIfHavaGroup(groupId).get("permission").toString();
		String ifConfirm = null;
		if("0".equals(permission)){//permission为"0"表示默认直接加入
			ifConfirm = "1";
		}else if("1".equals(permission)){//permission为"1"表示需要身份验证
			ifConfirm = "2";
		}else{
			resultMap.put("result",0);
			resultMap.put("message","该群是私有群组，不允许加入");
			return  resultMap;
		}
		//获取子账户
		/*SubAccounts subAccounts = getSubAccount(userId);
		String subAccountSid = "";
		String subToken = "";
		if(subAccounts!=null){
			subAccountSid = subAccounts.getSubAccountSid();
			subToken = subAccounts.getSubToken();
		}else{
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}*/
		//设置请求包体
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("groupId",groupId);
		map.put("declared",declared);
		//向容联发送申请加入群的请求
		String result = HttpUtil.pushGroupUrlByMap(map,userId,GroupConstant.REQUEST_METHOD_JOIN_GROUP);
		JSONObject jsonObject = JSONObject.parseObject(result);
		String statusCode = jsonObject.getString("statusCode");
		if (!"000000".equals(statusCode)){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",statusCode);
			return resultMap;
		}
		//把用户加到群里面去，主动申请状态为"2"
		GroupMember gm = addMemberIntoGroup(userId,groupId,ifConfirm,declared,null);
		if(gm!=null){
			resultMap.put("result",1);
			resultMap.put("message","操作成功");
			return resultMap;
		}else{
			throw new BaseBusinessException(GroupError.UNKNOWN_ERROR);
		}
	}

	/**
	 * 我创建的群列表
	 * @param userId 我的id
	 * @return
	 */
	@Override
	public Map<String ,Object> myGroupList(String userId) {
		if(userId==null || "".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		Group g = new Group();
		g.setMaster(userId);
		List<Group> listGroup = groupDao.findList(g);
		Group g1 = new Group();
		g1.setShamMaster(userId);
		List<Group> listGroup2 = groupDao.findList(g1);
		for (Group group0 : listGroup2){
			listGroup.add(group0);
		}
		Map map = new HashMap();
		int num = 1;
		for (Group group : listGroup){
			map.put(""+num,group);
			num++;
		}
/*		while(dbCursor.hasNext()){
			DBObject dbObject = dbCursor.next();
			map.put("id",dbObject.get("_id").toString());
			map.put("groupName",dbObject.get("_id").toString());
			map.put("imgPath", SystemConstant.SERVER_IMAGE_PATH+dbObject.get("imgPath").toString());
			listMap.add(map);
		}*/
		String jsonText = JSON.toJSONString(map,true);
		JSONObject jsonObject = JSONObject.parseObject(jsonText);
		return jsonObject;
	}

	/**
	 * 我是成员，不是群主的群列表
	 * @param userId 我的id
	 * @return
	 */
	@Override
	public Map<String ,Object> IAmMember(String userId) {
		if(userId==null || "".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		GroupMember gm = new GroupMember();
		gm.setStatus("1");
		gm.setMemberId(userId);
		List<GroupMember> list = groupMemberDao.findList(gm);
		Map map = new HashMap();
		int i = 1;
		for (GroupMember groupMember : list) {
			String groupId = groupMember.getGroupId();
			Group group = groupDao.findOne(groupId);
			if(group==null){
				continue;
			}
			map.put("" + i, group);
			i++;
		}
		return map;
	}

	/**
	 * 我在群内的群列表接口
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public Map<String, Object> groupAboutMe(String userId) {
		if(userId==null||"".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		List<Group> all = new ArrayList<Group>();
		//我是群主/伪群主的群
/*		DBCursor cur = GroupUtil.getCreateGroupCursor(userId);
		while(cur.hasNext()){
			DBObject dbObject = cur.next();
			Group g = new Group();
			g.setId(dbObject.get("_id").toString());
			g.setMaster(dbObject.get("master").toString());
			g.setImgPath(SystemConstant.SERVER_IMAGE_PATH+dbObject.get("imgPath").toString());
			g.setCategoryCode(dbObject.get("categoryCode").toString());
			String categoryName = GroupUtil.getNameByGroupCategory(dbObject.get("categoryCode").toString());
			g.setCategoryName(categoryName);
			g.setCreateTime(dbObject.get("createTime").toString());
			g.setType(dbObject.get("type").toString());
			g.setPermission(dbObject.get("permission").toString());
			g.setDeclared(dbObject.get("declared").toString());
			g.setOfficialType(dbObject.get("officialType").toString());
			g.setShamMaster(dbObject.get("shamMaster").toString());
			g.setGroupNum(dbObject.get("groupNum").toString());
			g.setGroupName(dbObject.get("groupName").toString());
			g.setFlag("master");
			all.add(g);
		}*/
		//我是群成员的群
		GroupMember groupMember = new GroupMember();
		groupMember.setStatus("1");
		groupMember.setMemberId(userId);
		List<GroupMember> groupMemberList = groupMemberDao.findList(groupMember);
		//合并
		for(GroupMember gm : groupMemberList){
			String groupId = gm.getGroupId();
			Group g = groupDao.findOne(groupId);
			if(g==null){
				continue;
			}
			g.setFlag("member");
			all.add(g);
		}
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",1);
		resultMap.put("message","获取信息成功");
		resultMap.put("num",all.size());
		resultMap.put("data",all);
		return resultMap;
	}

	/**
	 * 批量邀请用户加入群
	 * @param groupId 群id
	 * @param senderId	邀请者
	 * @param receiverIds 被邀请者
	 * @param confirm （容联）是否需要被确认->"0"，需要被确认。"1"，不需要被确认。
	 * @return
	 */
	@Override
	public Map<String,Object> inviteGroup(String groupId, String senderId, String[] receiverIds,String confirm) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//检查参数是否为空
		if(groupId==null || senderId==null || receiverIds==null || confirm==null || "".equals(groupId) || "".equals(senderId) || "".equals(confirm)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查confirm参数
		if(!"0".equals(confirm) && !"1".equals(confirm)){
			throw new BaseBusinessException(GroupError.PARAM_IS_FAULT);
		}
		//检查是否有这个用户
		if(AuraUserUtil.getDbObjectByUserId(senderId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//检查是否有这个群
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查权限
		String masterId = GroupUtil.getRLGroupMasterByUserId(groupId,senderId);
		if(masterId==null){
			//检查是否是群成员
			if(GroupUtil.checkIfIsGroupMember(groupId,senderId)==null){
				throw new BaseBusinessException(GroupError.NOT_POWER_OF_INVITING);
			}else{
				masterId = GroupUtil.checkIfHavaGroup(groupId).get("master").toString();
			}
		}
		//检查被邀请者
		List<String> userIdList = new ArrayList<>();
		if(receiverIds ==null){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		if(receiverIds.length<=0){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		for (String receiverId:receiverIds){
			//检查被邀请的人是否兜捞用户
			if(AuraUserUtil.getDbObjectByUserId(receiverId)==null){
				throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
			}
			//检查是否已经是群成员
			if(GroupUtil.checkIfIsGroupMember(groupId,receiverId)!=null){
				throw new BaseBusinessException(GroupError.ALREADY_IS_GROUPMEMBER);
			}
			userIdList.add(receiverId);
		}
		//获取子账户
		/*SubAccounts subAccounts = getSubAccount(senderId);
		if(subAccounts==null){
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}
		String subAccountSid = subAccounts.getSubAccountSid();
		String subToken = subAccounts.getSubToken();*/
		//获取昵称（用senderId）
		String screenName = ((DBObject)AuraUserUtil.getDbObjectByUserId(senderId).get("profile")).get("screenName").toString();
		//获取群名称
		String groupName = GroupUtil.checkIfHavaGroup(groupId).get("groupName").toString();
		//邀请理由
		String inviteCause = screenName+"邀请您加入："+groupName;
		//生成请求包体，第三个参数confirm解释：0->需要【被邀请者】确认，1->不需要【被邀请者】确认。
		String xmlStr = FormatStructure.objectToXml(groupId,receiverIds,confirm,inviteCause);
		//向容联发送管理员邀请用户加入群的请求（用masterId）
		String result = HttpUtil.pushGroupUrlByObject(xmlStr, masterId, GroupConstant.REQUEST_METHOD_INVITE_USER);
		JSONObject jsonObject2 = JSONObject.parseObject(result);
		String statusCode = jsonObject2.getString("statusCode");
		if (!"000000".equals(statusCode)){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",statusCode);
			return resultMap;
		}
		String confirmOnDB = null;
		if("0".equals(confirm)){
			confirmOnDB = "2";
		}else if("1".equals(confirm)){
			confirmOnDB = "1";
		}
		//加入到群-成员表中，status为"3"表示是邀请状态,ifConfirm为"0"表示需要【被邀请者】确认(记录邀请者为senderId)
		List<GroupMember> list = addMembersIntoGroupOnDB(receiverIds, groupId, confirmOnDB, inviteCause, senderId);
		//线程用于发送群提示
		if("1".equals(confirm)){
			MyRun myRun = new MyRun(groupId,userIdList,"0");
			Thread thread = new Thread(myRun);
			thread.start();
		}
		resultMap.put("result",1);
		resultMap.put("message","成功");
		return resultMap;
	}

	/**
	 * 批量强制把用户拉入群
	 * @param groupId 群id
	 * @param senderId 群主id/伪群主id
	 * @param receiverIds 被批量拉入群的用户id
	 * @return
	 */
	@Override
	public Map<String,Object> letJoinGroup(String groupId, String senderId, String[] receiverIds) {
		//检查参数是否为空
		if(groupId==null || senderId==null || receiverIds==null || "".equals(groupId) || "".equals(senderId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//检查是否有管理员这个用户
		if(AuraUserUtil.getDbObjectByUserId(senderId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//检查权限
		String masterId = GroupUtil.getRLGroupMasterByUserId(groupId,senderId);
		if(masterId==null){
			throw new BaseBusinessException(GroupError.NOT_MASTER_NOR_SHAMMASTER);
		}else{
			senderId = masterId;
		}
		//检查是否有这个群
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查被邀请者
		List<String> userIdList = new ArrayList<>();
		if(receiverIds ==null){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		if(receiverIds.length<=0){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		for (String receiverId:receiverIds){
			//检查被邀请的人是否兜捞用户
			if(AuraUserUtil.getDbObjectByUserId(receiverId)==null){
				throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
			}
			//检查是否已经是群成员
			if(GroupUtil.checkIfIsGroupMember(groupId,receiverId)!=null){
				throw new BaseBusinessException(GroupError.ALREADY_IS_GROUPMEMBER);
			}
			userIdList.add(receiverId);
		}
		//获取子账户
		/*SubAccounts subAccounts = getSubAccount(senderId);
		if(subAccounts==null){
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}
		String subAccountSid = subAccounts.getSubAccountSid();
		String subToken = subAccounts.getSubToken();*/
		//获取昵称
		String screenName = ((DBObject)AuraUserUtil.getDbObjectByUserId(senderId).get("profile")).get("screenName").toString();
		//获取群名称
		String groupName = GroupUtil.checkIfHavaGroup(groupId).get("groupName").toString();
		//邀请理由
		String inviteCause = screenName+"邀请您加入："+groupName;
		//生成请求包体，第三个参数confirm解释：0->需要【被邀请者】确认，1->不需要【被邀请者】确认。
		String xmlStr = FormatStructure.objectToXml(groupId,receiverIds,"1",inviteCause);
		//向容联发送管理员邀请用户加入群的请求
		String result = HttpUtil.pushGroupUrlByObject(xmlStr, senderId, GroupConstant.REQUEST_METHOD_INVITE_USER);
		JSONObject jsonObject2 = JSONObject.parseObject(result);
		String statusCode = jsonObject2.getString("statusCode");
		if (!"000000".equals(statusCode)){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",statusCode);
			return resultMap;
		}
		//加入到群-成员表中，status为"1"表示是入群状态,ifConfirm为"0"表示需要【被邀请者】确认
		List<GroupMember> list = addMembersIntoGroupOnDB(receiverIds,groupId,"1",inviteCause,senderId);
		if(list!=null){
			if(list.size()>0){
				//线程用于发送群提示
				MyRun myRun = new MyRun(groupId,userIdList,"0");
				Thread thread = new Thread(myRun);
				thread.start();
				resultMap.put("result",1);
				resultMap.put("message","成功");
				return resultMap;
			}
		}
		resultMap.put("result",0);
		resultMap.put("message","失败");
		return resultMap;
	}

	/**
	 * 创建子账户
	 * @param userId 用户id
	 * @return
	 */
	public SubAccounts createSubAccounts(String userId){
		if(userId==null || "".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//向容联发送创建子账户请求
		String result = HttpUtil.pushAboutSubAccounts(userId, GroupConstant.SUBACCOUNTS_METHOD_CREATE_SUBACCOUNT);
		JSONObject jsonObject = JSONObject.parseObject(result);
		String statusCode = jsonObject.getString("statusCode");
		//容联创建子账户成功
		if("000000".equals(statusCode)){
			//拿出subAccountSid
			JSONObject jsonObject1 = jsonObject.getJSONObject("SubAccount");
			//根据userId和子账户id查数据库，如果有了就不再增加子账户
			SubAccounts subAccounts = new SubAccounts();
			subAccounts.setUserId(userId);
			subAccounts.setSubAccountSid(jsonObject1.getString("subAccountSid"));
			subAccounts.setSubToken(jsonObject1.getString("subToken"));
			subAccounts.setDateCreated(jsonObject1.getString("dateCreated"));
			subAccounts.setVoipAccount(jsonObject1.getString("voipAccount"));
			subAccounts.setVoipPwd(jsonObject1.getString("voipPwd"));
			SubAccountsDaoImpl s = new SubAccountsDaoImpl();
			s.updateByUp(subAccounts);
			return subAccounts;
		}else{
			throw new BaseBusinessException(GroupError.RL_CREATE_SUBACCOUNT_FAILED);
		}
	}

	/**
	 * 获取子账户的Id和Token，数据库没有就根据userId向容联申请
	 * （合并AuraUserUtil.getSubAccountByUserId和createSubAccounts两个方法）
	 * @param userId 用户id
	 * @return
	 */
	public SubAccounts getSubAccount(String userId){
		if(userId==null || "".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		String result = HttpUtil.pushAboutSubAccounts(userId, GroupConstant.SUBACCOUNTS_METHOD_QUERY_SUBAACOUNT);
		JSONObject jsonObject = JSONObject.parseObject(result);
		String flag = "";
		if("000000".equals(jsonObject.getString("statusCode"))){
			if (jsonObject.getString("SubAccount")==null || "".equals(jsonObject.getString("SubAccount"))){
				flag = "0";
			}else{
				flag = "1";
			}
			SubAccounts subAccounts = new SubAccounts();
			if(flag.equals("1")){
				JSONObject jsonObject1 = jsonObject.getJSONObject("SubAccount");
				subAccounts.setUserId(userId);
				subAccounts.setSubAccountSid(jsonObject1.getString("subAccountSid").toString());
				subAccounts.setSubToken(jsonObject1.getString("subToken").toString());
				subAccounts.setDateCreated(jsonObject1.getString("dateCreated").toString());
				subAccounts.setVoipAccount(jsonObject1.getString("voipAccount").toString());
				subAccounts.setVoipPwd(jsonObject1.getString("voipPwd").toString());
				//取出一次保存一次
				SubAccountsDaoImpl subAccountsDaoImpl = new SubAccountsDaoImpl();
				BasicDBObject doc = subAccountsDaoImpl.updateByUp(subAccounts);
				return subAccounts;
			}else if (flag.equals("0")){
				subAccounts = createSubAccounts(userId);
				return subAccounts;
			}else{
				throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
			}
		}else{
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}
	}

	/**
	 * 修改群资料
	 * @param groupId 需要修改的群的id
	 * @param userId 修改这个群的用户
	 * @param permission 申请加入模式 0：默认直接加入 1：需要身份验证  2：私有群组
	 * @param groupName 群名称
	 * @param declared 公告
	 * @param categoryCode 分类
	 * @param imgPath 群头像
	 * @return
	 */
	@Override
	public Map<String,Object> modifyGroup(String groupId, String userId, String permission, String groupName, String declared, String categoryCode, String imgPath) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//检查groupId
		if(groupId==null || "".equals(groupId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查是否存在userId这个用户
		if(userId==null || "".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		if(AuraUserUtil.getDbObjectByUserId(userId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//检查权限
		String masterId = GroupUtil.getRLGroupMasterByUserId(groupId,userId);
		if(masterId==null){
			throw new BaseBusinessException(GroupError.NOT_MASTER_NOR_SHAMMASTER);
		}else{
			userId = masterId;
		}
		//检查申请验证方式permission
		if(permission==null || "".equals(permission)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		if(!"0".equals(permission) && !"1".equals(permission) && !"2".equals(permission)){
			throw new BaseBusinessException(GroupError.PERMISSION_NOT_EXISTS);
		}
		//检查群公告declared
		if(declared!=null){
			if(declared.length()>200){
				declared = declared.substring(0, 200);
			}
		}
		//检查群分类categoryCode
		if(categoryCode!=null){
			DBCollection collection = DetaDiv.getCollection("t_groupCategory");
			BasicDBObject where = new BasicDBObject();
			where.put("gcCode", categoryCode);
			int findCount = collection.find(where).count();
			if(findCount<=0){
				throw new BaseBusinessException(GroupError.CATEGORY_NOT_EXISTS);
			}
		}
		//检查群头像路径imgPath
		if(imgPath!=null){
			if(imgPath.startsWith("http")){
				throw new BaseBusinessException(GroupError.FORMAT_URL_NOT_CORRECT);
			}
		}
		//拿出用户的子账户
		/*SubAccounts subAccounts = getSubAccount(userId);
		String subAccountSid = "";
		String subToken = "";
		if(subAccounts!=null){
			subAccountSid = subAccounts.getSubAccountSid();
			subToken = subAccounts.getSubToken();
		}else{
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}*/
		//设置请求包体
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("groupId",groupId);
		map.put("permission",permission);
		map.put("name",groupName);
		map.put("declared",declared);
		//向容联发起修改群组属性的请求
		String result = HttpUtil.pushGroupUrlByMap(map,userId,GroupConstant.REQUEST_METHOD_MODIFY_GROUP);
		JSONObject jsonObject = JSONObject.parseObject(result);
		String statusCode = jsonObject.getString("statusCode");
		if (!"000000".equals(statusCode)){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",statusCode);
			return resultMap;
		}
		Group group = new Group();
		group.setId(groupId);
		if (permission!=null){
			group.setPermission(permission);
		}
		if(groupName!=null){
			group.setGroupName(groupName);
		}
		if(declared!=null){
			group.setDeclared(declared);
		}
		if(categoryCode!=null){
			group.setCategoryCode(categoryCode);
		}
		if(imgPath!=null){
			group.setImgPath(imgPath);
		}
		int ifSuccess = groupDao.update(group);

		if(ifSuccess==1){
			resultMap.put("result",1);
			resultMap.put("message","操作成功");
			return resultMap;
		}else{
			resultMap.put("result",0);
			resultMap.put("message","操作失败");
			return resultMap;
		}
	}

	/**
	 * 主动退出群
	 * @param groupId 群id
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public Map<String ,Object> logoutGroup(String groupId, String userId){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//检查参数是否为空
		if(groupId==null || userId==null || "".equals(groupId) || "".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查是否有这个群
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查是否是群成员
		if(GroupUtil.checkIfIsGroupMember(groupId,userId)==null){
			throw new BaseBusinessException(GroupError.NOT_GROUPMEMBER);
		}
		//获取子账户
		/*SubAccounts subAccounts = getSubAccount(userId);
		String subAccountSid = "";
		String subToken = "";
		if(subAccounts!=null){
			subAccountSid = subAccounts.getSubAccountSid();
			subToken = subAccounts.getSubToken();
		}else{
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}*/
		//设置请求包体
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("groupId",groupId);
		//向容联发送请求群成员主动退出群方法
		String result = HttpUtil.pushGroupUrlByMap(map,userId,GroupConstant.REQUEST_METHOD_LOGOUT_GROUP);
		JSONObject jsonObject = JSONObject.parseObject(result);
		String statusCode = jsonObject.getString("statusCode");
		if (!"000000".equals(statusCode)){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",statusCode);
			return resultMap;
		}
		GroupMember gm = new GroupMember();
		gm.setGroupId(groupId);
		gm.setMemberId(userId);
		gm.setStatus("0");
		int a = groupMemberDao.update(gm);
		if(a!=1){
			throw new BaseBusinessException(GroupError.FAILED_UPDATE);
		}
		//设置返回
		resultMap.put("result",1);
		resultMap.put("message","请求成功");
		return resultMap;
	}

	/**
	 * 解散群
	 * @param userId 用户id
	 * @param groupId 群id
	 * @return
	 */
	@Override
	public Map<String, Object> deleteGroup(String groupId, String userId) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//检查参数是否为空
		if(userId==null||groupId==null||"".equals(groupId)||"".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查是否有这个用户
		if(AuraUserUtil.getDbObjectByUserId(userId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//检查是否有这个群
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//获取到群主id
		String masterId = GroupUtil.getRLGroupMasterByUserId(groupId, userId);
		if(masterId==null){
			throw new BaseBusinessException(GroupError.NOT_MASTER_NOR_SHAMMASTER);
		}
		userId = masterId;
		//拿出子账户
		/*SubAccounts subAccounts = getSubAccount(userId);
		String subAccountSid = "";
		String subToken = "";
		if(subAccounts!=null){
			subAccountSid = subAccounts.getSubAccountSid();
			subToken = subAccounts.getSubToken();
		}*/
		//设置请求包体
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);
		String result = HttpUtil.pushGroupUrlByMap(map, userId, GroupConstant.REQUEST_METHOD_DELETE_GROUP);
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(!"000000".equals(jsonObject.getString("statusCode"))){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",jsonObject.getString("statusCode"));
			return resultMap;
		}
		int resultCode = groupDao.delete(groupId);
		if(resultCode==1){
			resultMap.put("result",1);
			resultMap.put("message","删除群成功");
		}else{
			resultMap.put("result",0);
			resultMap.put("message","删除群失败");
		}
		return resultMap;
	}

	/**
	 * 批量删除群成员
	 * @param groupId 群id
	 * @param userId 操作者id
	 * @param memberIds 要被批量删除的群id
	 * @return
	 */
	@Override
	public Map<String, Object> deleteGroupMember(String groupId, String userId, String[] memberIds) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//检查参数是否为空
		if(groupId==null || userId==null || memberIds==null ||"".equals(groupId) ||"".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查是否有这个群
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查用户
		if(AuraUserUtil.getDbObjectByUserId(userId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//检查用户作为群主
		String masterId = GroupUtil.getRLGroupMasterByUserId(groupId,userId);
		if(masterId==null){
			throw new BaseBusinessException(GroupError.NOT_MASTER_NOR_SHAMMASTER);
		}
		userId = masterId;
		//检查memberIds
		for(String memberId : memberIds){
			//检查是否存在这个用户
			if(AuraUserUtil.getDbObjectByUserId(memberId)==null){
				throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
			}
			//检查是否是群成员
			if(GroupUtil.checkIfIsGroupMember(groupId,memberId)==null){
				throw new BaseBusinessException(GroupError.NOT_GROUPMEMBER);
			}
		}
		//拿出子账户
		/*SubAccounts subAccounts = getSubAccount(userId);
		if(subAccounts==null){
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}
		String subAccountSid = subAccounts.getSubAccountSid();
		String subToken = subAccounts.getSubToken();*/
		//请求包体
		String xmlStr = FormatStructure.objectToXml2(groupId,memberIds);
		//向容联发送踢人请求
		String result = HttpUtil.pushGroupUrlByObject(xmlStr, userId, GroupConstant.REQUEST_METHOD_DELETE_GROUP_MEMBER);
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(!"000000".equals(jsonObject.getString("statusCode"))){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",jsonObject.getString("statusCode"));
			return resultMap;
		}
		for(String memberId:memberIds){
			GroupMember gm = new GroupMember();
			gm.setStatus("0");
			gm.setRemark("被请出群");
			gm.setGroupId(groupId);
			gm.setMemberId(memberId);
			SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT);
			gm.setQuitTime(sdf.format(new Date()));
			groupMemberDao.update(gm);
		}
		resultMap.put("result",1);
		resultMap.put("message","删除群成员成功");
		return resultMap;
	}

	/**
	 * 群主处理申请
	 * @param groupId 群id
	 * @param masterId	操作者id
	 * @param askerId 申请人id
	 * @param confirm "0":通过 "1":拒绝
	 * @return
	 */
	@Override
	public Map<String, Object> askJoin(String groupId, String masterId, String askerId, String confirm) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//检查参数是否为空
		if(groupId==null || masterId==null || askerId==null || confirm==null
				|| "".equals(groupId) || "".equals(masterId) || "".equals(askerId) || "".equals(confirm)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查是否有这个群
		if(GroupUtil.checkIfHavaGroup(groupId)==null) {
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查masterId
			//检查是否有这个用户
		if(AuraUserUtil.getDbObjectByUserId(masterId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
			//拿出群主id
		String masterId2 = GroupUtil.getRLGroupMasterByUserId(groupId,masterId);
		if(masterId2==null){
			throw new BaseBusinessException(GroupError.NOT_MASTER_NOR_SHAMMASTER);
		}
		masterId = masterId2;
		//检查askerId
			//检查是否有这个用户
		if(AuraUserUtil.getDbObjectByUserId(askerId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
			//检查是否处于申请状态
		if(GroupUtil.checkIfIsApplying(groupId, askerId)==null){
			throw new BaseBusinessException(GroupError.NOT_APPLYING);
		}
		List<String> userIdList = new ArrayList<>();
		userIdList.add(askerId);
		//检查confirm
		if(!"0".equals(confirm) || !"1".equals(confirm)){
			throw new BaseBusinessException(GroupError.PARAM_RL_NOT_ACCEPT);
		}
		//拿出操作者子账户
		/*SubAccounts subAccounts = getSubAccount(masterId);
		if(subAccounts==null){
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}
		String subAccountSid = subAccounts.getSubAccountSid();
		String subToken = subAccounts.getSubToken();*/
		//拿出请求者子账户
		/*SubAccounts subAccounts1 = getSubAccount(askerId);
		if(subAccounts1==null){
			throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
		}
		String voipAccount = subAccounts1.getVoipAccount();*/
		//设置请求包体
		Map<String,Object> requestMap = new HashMap<String, Object>();
		requestMap.put("groupId",groupId);
		requestMap.put("asker",askerId);
		requestMap.put("confirm",confirm);
		//向容联发送群主处理请求
		String result = HttpUtil.pushGroupUrlByMap(requestMap,masterId,GroupConstant.REQUEST_METHOD_ASK_JOIN);
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(!"000000".equals(jsonObject.getString("statusCode"))){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",jsonObject.getString("statusCode"));
			return resultMap;
		}
		if("0".equals(confirm)){
			//同意入群
			addMemberIntoGroup(askerId,groupId,"1",null,masterId);
			resultMap.put("result",1);
			resultMap.put("message","成功加入群");
			//线程用于发送群提示
			MyRun myRun = new MyRun(groupId,userIdList,"0");
			Thread thread = new Thread(myRun);
			thread.start();
			return resultMap;
		}else if("1".equals(confirm)){
			//拒绝入群
			GroupMember gm = new GroupMember();
			gm.setGroupId(groupId);
			gm.setMemberId(askerId);
			int k = groupMemberDao.delete(gm);
			if(k==1){
				resultMap.put("result",1);
				resultMap.put("message","成功拒绝加入群");
			}else{
				throw new BaseBusinessException(GroupError.UNKNOWN_ERROR);
			}
			return resultMap;
		}else{
			throw new BaseBusinessException(GroupError.UNKNOWN_ERROR);
		}
	}

	/**
	 * 查询群
	 * @param queryContent
	 * @return
	 */
	@Override
	public Map<String, Object> queryGroup(String queryContent) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<Group> allList = new ArrayList<Group>();
		//检查参数是否为空
		if(queryContent==null ||"".equals(queryContent)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		if(RegexUtil.regexEightNum(queryContent)){
			Group group = new Group();
			group.setGroupNum(queryContent);
			List<Group> listByGroupNum = groupDao.findList(group);
			for(Group g : listByGroupNum){
				allList.add(g);
			}
		}
		Group group1 = new Group();
		group1.setGroupName(queryContent);
		List<Group> listByGroupName = groupDao.findList(group1);
		for(Group g : listByGroupName){
			allList.add(g);
		}
		resultMap.put("result",1);
		resultMap.put("message","查询群列表成功");
		resultMap.put("data",allList);
		return resultMap;
	}

	/**
	 * 修改群名片
	 * @param groupId 群id
	 * @param userId 成员id
	 * @param remarkName 群昵称
	 * @return
	 */
	@Override
	public Map<String, Object> modifyGroupMember(String groupId, String userId, String remarkName) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//检查参数是否为空
		if(groupId==null || userId==null || "".equals(groupId) ||"".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查是否有这个群
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查是否有这个用户
		if(AuraUserUtil.getDbObjectByUserId(userId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//检查是否是群成员
		if(GroupUtil.checkIfIsGroupMember(groupId, userId)==null){
			throw new BaseBusinessException(GroupError.NOT_GROUPMEMBER);
		}
		GroupMember gm = new GroupMember();
		gm.setGroupId(groupId);
		gm.setMemberId(userId);
		if(remarkName!=null){
			gm.setRemarkName(remarkName);
		}
		int i = groupMemberDao.update(gm);
		if(i==1){
			resultMap.put("result",1);
			resultMap.put("message","修改成功");
		}else{
			resultMap.put("result",0);
			resultMap.put("message","修改失败");
		}
		return resultMap;
	}

	/**
	 * 禁言
	 * @param groupId 群id
	 * @param masterId 群主id/伪群主id
	 * @param memberId 成员id
	 * @param confirm （容联）是否禁言->"0"，可发言（默认）。"1",禁言。
	 * @return
	 */
	@Override
	public Map<String, Object> forbidSpeak(String groupId, String masterId, String memberId, String confirm) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//检查参数是否为空
		if(groupId==null||masterId==null||memberId==null||confirm==null||
				"".equals(groupId)||"".equals(masterId)||"".equals(memberId)||"".equals(confirm)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查confirm
		if(!"0".equals(confirm) && !"1".equals(confirm)){
			throw new BaseBusinessException(GroupError.PARAM_IS_FAULT);
		}
		//检查groupId
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查是否存在masterId这个用户
		if(AuraUserUtil.getDbObjectByUserId(masterId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//拿出真正的有权限的群主id,masterId2
		String masterId2 = GroupUtil.getRLGroupMasterByUserId(groupId,masterId);
		if(masterId2==null){
			throw new BaseBusinessException(GroupError.NOT_MASTER_NOR_SHAMMASTER);
		}
		//检查是否存在memberId这个用户
		if(AuraUserUtil.getDbObjectByUserId(memberId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//检查memberId是否在groupId这个群内
		if(GroupUtil.checkIfIsGroupMember(groupId,memberId)==null){
			throw new BaseBusinessException(GroupError.NOT_GROUPMEMBER);
		}
		//设置请求包体
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("groupId",groupId);
		map.put("member",memberId);
		map.put("operation",confirm);
		//向容联发送禁言或者解禁请求
		String result = HttpUtil.pushGroupUrlByMap(map,masterId2,GroupConstant.REQUEST_METHOD_FORBID_SPEAK);
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(!"000000".equals(jsonObject.getString("statusCode"))){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",jsonObject.getString("statusCode"));
			return resultMap;
		}
		BasicDBObject where = new BasicDBObject();
		where.put("groupId",groupId);
		where.put("memberId",memberId);
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("settings.setUpTalk",confirm);
		MonDB mondb = new MonDB();
		mondb.update("t_groupMember",where,dbObject);
		resultMap.put("result",1);
		if("0".equals(confirm)){
			resultMap.put("message","解除禁言成功");
		}else{
			resultMap.put("message","禁言成功");
		}
		return resultMap;
	}

	/**
	 * 用户设置群组消息接收规则
	 * @param groupId 群组id
	 * @param memberId 群成员id
	 * @param rule "0"->接收消息 "1"->不接收消息
	 * @return
	 */
	@Override
	public Map<String, Object> setGroupMsg(String groupId, String memberId, String rule) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//检查参数是否为空
		if(groupId==null||memberId==null||rule==null||"".equals(groupId)||"".equals(memberId)||"".equals(rule)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查rule
		if(!"0".equals(rule) && !"1".equals(rule)){
			throw new BaseBusinessException(GroupError.PARAM_IS_FAULT);
		}
		//检查是否有groupId这个群
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查是否有memberId这个用户
		if(AuraUserUtil.getDbObjectByUserId(memberId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//检查是否是成员关系
		if(GroupUtil.checkIfIsGroupMember(groupId,memberId)==null){
			throw new BaseBusinessException(GroupError.NOT_GROUPMEMBER);
		}
		//设置请求包体
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("groupId",groupId);
		map.put("rule",rule);
		//向容联发送接收消息规则的请求
		String result = HttpUtil.pushGroupUrlByMap(map,memberId,GroupConstant.REQUEST_METHOD_SET_GROUP_MSG);
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(!"000000".equals(jsonObject.getString("statusCode"))){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",jsonObject.getString("statusCode"));
			return resultMap;
		}
		BasicDBObject where = new BasicDBObject();
		where.put("groupId",groupId);
		where.put("memberId",memberId);
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("settings.setUpReceive",rule);
		MonDB mondb = new MonDB();
		mondb.update("t_groupMember",where,dbObject);
		resultMap.put("result",1);
		if("0".equals(rule)){
			resultMap.put("message","成功设置接收消息");
		}else if("1".equals(rule)){
			resultMap.put("message","成功设置不接收消息");
		}
		return resultMap;
	}

	/**
	 * 查询群成员（从容联那边去查询）
	 * @param groupId 群id
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public Map<String, Object> queryMember(String groupId, String userId) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//检查是否有此群
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查是否有这个用户
		if(AuraUserUtil.getDbObjectByUserId(userId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//设置请求包体
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("groupId",groupId);
		//向容联发送查询群成员的请求
		String result = HttpUtil.pushGroupUrlByMapReturnXml(map, userId, GroupConstant.REQUEST_METHOD_QUERY_MEMEBER);
		List<GroupMember> gmList = new ArrayList<GroupMember>();
		try {
			Document doc = DocumentHelper.parseText(result);
			Element root = doc.getRootElement();
			System.out.println("root element:"+root.getName());
			Element statusCode = root.element("statusCode");
			if(!"000000".equals(statusCode.getData())){
				resultMap.put("result",999);
				resultMap.put("message","容联请求成功，但返回代码不是000000");
				resultMap.put("statusCode",statusCode.getData());
				return resultMap;
			}
			Iterator iterator = root.elementIterator("members");
			while (iterator.hasNext()){
				Element e = (Element)iterator.next();
				Iterator iterator1 = e.elementIterator("member");
				while (iterator1.hasNext()){
					Element e2 = (Element)iterator1.next();
					String memberId = e2.elementTextTrim("voipAccount");
					GroupMember gm = new GroupMember();
					gm.setGroupId(groupId);
					gm.setMemberId(memberId);
					gm.setStatus("1");
					GroupMember gm2 = addMemberIntoGroup(memberId,groupId,"1",null,null);
					gmList.add(gm2);
				}
			}
		} catch (DocumentException e) {
			throw new BaseBusinessException(GroupError.XML_ANALIZE_FAILED);
		}
		resultMap.put("result",1);
		resultMap.put("message","成功");
		resultMap.put("data",gmList);
		return resultMap;
	}

	/**
	 * 查询群属性（从容联那边去查询）
	 * @param groupId 群id
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public Map<String, Object> queryGroupDetail(String groupId, String userId) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//检查参数是否为空
		if(groupId==null || userId==null || "".equals(groupId) || "".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		//检查是否有这个群
		if(GroupUtil.checkIfHavaGroup(groupId)==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//检查是否有userId这个用户
		if(AuraUserUtil.getDbObjectByUserId(userId)==null){
			throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
		}
		//设置请求包体
		Map<String,Object> map = new HashMap<>();
		map.put("groupId",groupId);
		//向容联发送查询群组属性的请求
		String xmlStr= HttpUtil.pushGroupUrlByMapReturnXml(map,userId,GroupConstant.REQUEST_METHOD_QUERY_GROUP_DETAIL);
		try {
			Document document = DocumentHelper.parseText(xmlStr);
			Element e1 = document.getRootElement();
			Element e_statusCode =  e1.element("statusCode");
			if(!"000000".equals(e_statusCode.getStringValue())){
				resultMap.put("result",999);
				resultMap.put("message","容联请求成功，但返回代码不是000000");
				resultMap.put("statusCode",e_statusCode.getData());
				return resultMap;
			}
			Element e_groupName = e1.element("name");
			Element e_master = e1.element("owner");
			Element e_type = e1.element("type");
			Element e_declared = e1.element("declared");
			Element e_count = e1.element("count");
			Element e_dateCreated = e1.element("dateCreated");
			Element e_permission = e1.element("permission");
			String groupName = e_groupName.getStringValue(); //群名称
			String master = e_master.getStringValue();	//群主id
			String type = e_type.getStringValue();	//类型
			String declared = e_declared.getStringValue();	//群公告
			int count = Integer.parseInt(e_count.getStringValue());	//群成员数量
			String dateCreated = e_dateCreated.getStringValue();	//创建群时间（时间戳）
			String permission = e_permission.getStringValue();	//申请加入模式（"0":默认直接加入，"1":需要身份验证）
			Group group = new Group();
			group.setId(groupId);
			if(groupName!=null && !"".equals(groupName)){
				group.setGroupName(groupName);
				resultMap.put("groupName",groupName);
			}
			if(master!=null && !"".equals(master)){
				group.setMaster(master);
				resultMap.put("master",master);
			}
			if(type!=null && !"".equals(type)){
				group.setType(type);
				resultMap.put("type",type);
			}
			if(declared!=null){
				group.setDeclared(declared);
				resultMap.put("declared",declared);
			}

			if(permission!=null && !"".equals(permission)){
				group.setPermission(permission);
				resultMap.put("permission",permission);
			}
			int k  = groupDao.update(group);
			if(k==1){
				resultMap.put("result",1);
				resultMap.put("message","从容联查询成功，修改也成功");
				return resultMap;
			}
			resultMap.put("result",0);
			resultMap.put("message","从容联查询成功，但修改本地数据库失败");
			return resultMap;
		} catch (DocumentException e) {
			throw new BaseBusinessException(GroupError.XML_ANALIZE_FAILED);
		}
	}

	/**
	 * 群提示
	 * @param groupId
	 * @param userIdList
	 * @param type
	 * @return
	 */
	public Map<String, Object> groupRemind(String groupId, List<String> userIdList,String type) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//检查参数是否为空
		if(groupId==null || userIdList==null || "".equals(groupId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		if(userIdList.size()<=0){
			throw new BaseBusinessException(GroupError.BATCH_PARAM_TOO_SHORT);
		}
		//检查是否有这个群
		DBObject groupObject = GroupUtil.checkIfHavaGroup(groupId);
		if(groupObject==null){
			throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
		}
		//设置容联请求包体
		Map<String,Object> mapForRL = new HashMap<>();
		mapForRL.put("pushType",2);
		mapForRL.put("appId",SystemConstant.DOULAO_APP_ID);
		mapForRL.put("sender",userIdList.get(0));
		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add(groupId);
		mapForRL.put("receiver",arrayList);
		mapForRL.put("msgType",1);
		//msgContent
		Map<String,Object> map = new HashMap<>();
		map.put("msgType","9997");
		map.put("msgName","进群提示");
		String screenNameAll = "";
		for(String userId:userIdList){
			DBObject userObject = AuraUserUtil.getDbObjectByUserId(userId);
			if(userObject==null){
				throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
			}
			String screenName = ((DBObject)userObject.get("profile")).get("screenName").toString();
			screenNameAll = screenNameAll+","+screenName;
		}
		screenNameAll = screenNameAll.substring(1);
		map.put("msg",screenNameAll);
		map.put("bizKey","\"\"");
		mapForRL.put("msgContent", JSON.toJSONString(map));
		//msgDomain
		String msgDomain = MsgUtil.getMsgDomainList(groupObject);
		mapForRL.put("msgDomain",msgDomain);
		//向容联发起推送群消息的请求
		String result = HttpUtil.pushGroupMsg(mapForRL);
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(!"000000".equals(jsonObject.getString("statusCode"))){
			resultMap.put("result",999);
			resultMap.put("message","容联请求成功，但返回代码不是000000");
			resultMap.put("statusCode",jsonObject.getString("statusCode"));
			return resultMap;
		}
		resultMap.put("result","1");
		resultMap.put("message","成功");
		return resultMap;
	}

}
