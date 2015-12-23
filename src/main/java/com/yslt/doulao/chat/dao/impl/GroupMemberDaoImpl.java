package com.yslt.doulao.chat.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import common.entity.BaseBusinessException;
import common.util.AuraUserUtil;
import common.var.constants.SystemConstant;
import common.var.exception.GroupError;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yslt.doulao.chat.dao.GroupMemberDao;
import com.yslt.doulao.chat.entity.GroupMember;

import common.util.db.DetaDiv;
import common.util.db.MonDB;


@Repository
public class GroupMemberDaoImpl implements GroupMemberDao{

	private final String TABLE_NAME = "t_groupMember";
	
	@Resource
	private MonDB monDB;
	
	@Override
	public GroupMember insert(GroupMember groupMember) {
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject dbObject = new BasicDBObject();
		if(groupMember.getGroupId()!=null){
			dbObject.put("groupId", groupMember.getGroupId());
		}
		if(groupMember.getMemberId()!=null){
			dbObject.put("memberId",groupMember.getMemberId());
		}
		if(groupMember.getIfManager()==null){
			dbObject.put("ifManager","0");
		}else{
			dbObject.put("ifManager",groupMember.getIfManager());
		}
		if(groupMember.getInviterId()==null){
			dbObject.put("inviterId","");
		}else{
			dbObject.put("inviterId",groupMember.getInviterId());
		}
		dbObject.put("remarkName", "");
		dbObject.put("autoToMailList", "");
		SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT);
		dbObject.put("joinTime", sdf.format(new Date()));
		if(groupMember.getStatus()==null) {
			throw new BaseBusinessException(GroupError.NOT_SPECIFIED_FIELD);
		}else {
			dbObject.put("status",groupMember.getStatus());
		}
		dbObject.put("quitTime", "");
		dbObject.put("updateTime", "");
		if(groupMember.getRemark()==null){
			dbObject.put("remark","");
		}else{
			dbObject.put("remark",groupMember.getRemark());
		}
		if(groupMember.getSettings()!=null){
			dbObject.put("settings",groupMember.getSettings());
		}
		collection.insert(dbObject);
		GroupMember groupMember2 = new GroupMember();
		groupMember2.setId(dbObject.get("_id").toString());
		groupMember2.setGroupId(dbObject.get("groupId").toString());
		groupMember2.setMemberId(dbObject.get("memberId").toString());
		groupMember2.setRemarkName(dbObject.get("remarkName").toString());
		groupMember2.setAutoToMailList(dbObject.get("autoToMailList").toString());
		groupMember2.setJoinTime(dbObject.get("joinTime").toString());
		groupMember2.setStatus(dbObject.get("status").toString());
		groupMember2.setQuitTime(dbObject.get("quitTime").toString());
		groupMember2.setIfManager(dbObject.get("ifManager").toString());
		groupMember2.setRemark(dbObject.get("remark").toString());
		groupMember2.setInviterId(dbObject.get("inviterId").toString());
		groupMember2.setSettings((BasicDBObject)dbObject.get("settings"));
		return groupMember2;
	}

	@Override
	public List<GroupMember> findList(GroupMember groupMember) {
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject where = new BasicDBObject();
		if(groupMember.getGroupId()!=null){
			where.put("groupId", groupMember.getGroupId());
		}
		if(groupMember.getMemberId()!=null){
			where.put("memberId", groupMember.getMemberId());
		}
		if(groupMember.getStatus()!=null){
			where.put("status", groupMember.getStatus());
		}
		if(groupMember.getIfManager()!=null){
			where.put("ifManager",groupMember.getIfManager());
		}
		DBCursor dbCursor = collection.find(where);
		List<GroupMember> list = new ArrayList<GroupMember>();
		while(dbCursor.hasNext()){
			DBObject dbObject = dbCursor.next();
			GroupMember gm = new GroupMember();
			gm.setId(dbObject.get("_id").toString());
			gm.setGroupId(dbObject.get("groupId").toString());
			gm.setMemberId(dbObject.get("memberId").toString());
			gm.setRemarkName(dbObject.get("remarkName").toString());
			gm.setAutoToMailList(dbObject.get("autoToMailList").toString());
			gm.setJoinTime(dbObject.get("joinTime").toString());
			gm.setStatus(dbObject.get("status").toString());
			gm.setQuitTime(dbObject.get("quitTime").toString());
			gm.setIfManager(dbObject.get("ifManager").toString());
			gm.setRemark(dbObject.get("remark").toString());
			gm.setInviterId(dbObject.get("inviterId").toString());
			gm.setSettings((BasicDBObject)dbObject.get("settings"));
			list.add(gm);
		}
		return list;
	}

	@Override
	public List<GroupMember> findListIdNameImg(GroupMember groupMember) {
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject where = new BasicDBObject();
		if(groupMember.getGroupId()!=null){
			where.put("groupId", groupMember.getGroupId());
		}
		if(groupMember.getMemberId()!=null){
			where.put("memberId", groupMember.getMemberId());
		}
		if(groupMember.getStatus()!=null){
			where.put("status", groupMember.getStatus());
		}
		if(groupMember.getIfManager()!=null){
			where.put("ifManager",groupMember.getIfManager());
		}
		DBCursor dbCursor = collection.find(where);
		List<GroupMember> list = new ArrayList<GroupMember>();
		while(dbCursor.hasNext()){
			DBObject dbObject = dbCursor.next();
			GroupMember gm = new GroupMember();
			gm.setMemberId(dbObject.get("memberId").toString());
			gm.setRemarkName(dbObject.get("remarkName").toString());
			DBObject user = AuraUserUtil.getDbObjectByUserId(dbObject.get("memberId").toString());
			if(user==null){
				throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
			}
			//昵称
			gm.setScreenName(((DBObject)user.get("profile")).get("screenName").toString());
			//头像
			String imgPath = "";
			boolean flag1 = false;
			if(user.get("profile")==null){
				flag1 =true;
			}
			if(((DBObject)user.get("profile")).get("avatar")==null){
				flag1=true;
			}
			if(((DBObject)(((DBObject)user.get("profile")).get("avatar"))).get("small")==null){
				flag1=true;
			}
			if(flag1==false) {
				imgPath = ((DBObject) (((DBObject) user.get("profile")).get("avatar"))).get("small").toString();
			}else{
				imgPath = ((DBObject)(((DBObject)user.get("profile")).get("avatar"))).get("smallPath").toString();
			}
			if(imgPath==null ||"".equals(imgPath) || "-".equals(imgPath)){
				imgPath = ((DBObject)(((DBObject)user.get("profile")).get("avatar"))).get("smallPath").toString();
			}
			if(imgPath!=null){
				if(imgPath.startsWith("http")){
					imgPath = ((DBObject)(((DBObject)user.get("profile")).get("avatar"))).get("smallPath").toString();
				}
			}
			gm.setImgPath(SystemConstant.SERVER_IMAGE_PATH+imgPath);
			//性别
			if(((DBObject) user.get("profile")).get("gender")!=null) {
				gm.setGender(Integer.parseInt(((DBObject) user.get("profile")).get("gender").toString()));
			}else{
				gm.setGender(1);
			}
			list.add(gm);
		}
		return list;
	}

	@Override
	public int update(GroupMember groupMember) {
		BasicDBObject where = new BasicDBObject();
		if(groupMember.getGroupId()!=null){
			where.put("groupId", groupMember.getGroupId());
		}
		if(groupMember.getMemberId()!=null){
			where.put("memberId", groupMember.getMemberId());
		}
		BasicDBObject dbObject = new BasicDBObject();
		if(groupMember.getRemarkName()!=null){
			dbObject.put("remarkName", groupMember.getRemarkName());
		}
		if(groupMember.getAutoToMailList()!=null){
			dbObject.put("autoToMailList", groupMember.getAutoToMailList());
		}
		if(groupMember.getJoinTime()!=null){
			dbObject.put("joinTime", groupMember.getJoinTime());
		}
		if(groupMember.getStatus()!=null){
			dbObject.put("status", groupMember.getStatus());
		}
		if(groupMember.getQuitTime()!=null){
			dbObject.put("quitTime", groupMember.getQuitTime());
		}
		if(groupMember.getIfManager()!=null){
			dbObject.put("ifManager",groupMember.getIfManager());
		}
		if(groupMember.getRemark()!=null){
			dbObject.put("remark",groupMember.getRemark());
		}
		if(groupMember.getInviterId()!=null){
			dbObject.put("inviterId",groupMember.getInviterId());
		}
		if(groupMember.getSettings()!=null){
			dbObject.put("settings",groupMember.getSettings());
		}
		SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT);
		dbObject.put("updateTime", sdf.format(new Date()));
		monDB.update(TABLE_NAME, where, dbObject);
		return 1;
	}

	@Override
	public GroupMember findOne(GroupMember groupMember) {
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject where = new BasicDBObject();
		if(groupMember.getId()!=null){
			where.put("_id", new ObjectId(groupMember.getId()));
		}
		if(groupMember.getGroupId()!=null){
			where.put("groupId", groupMember.getGroupId());
		}
		if(groupMember.getMemberId()!=null){
			where.put("memberId", groupMember.getMemberId());
		}
		if(groupMember.getIfManager()!=null){
			where.put("ifManager",groupMember.getIfManager());
		}
		if(groupMember.getRemark()!=null){
			where.put("remark",groupMember.getRemark());
		}
		DBObject dbObject = collection.findOne(where);
		GroupMember gm = new GroupMember();
		if(dbObject!=null){
			gm.setId(dbObject.get("_id").toString());
			gm.setGroupId(dbObject.get("groupId").toString());
			gm.setMemberId(dbObject.get("memberId").toString());
			gm.setRemarkName(dbObject.get("remarkName").toString());
			gm.setAutoToMailList(dbObject.get("autoToMailList").toString());
			gm.setJoinTime(dbObject.get("joinTime").toString());
			gm.setStatus(dbObject.get("status").toString());
			gm.setQuitTime(dbObject.get("quitTime").toString());
			gm.setUpdateTime(dbObject.get("updateTime").toString());
			gm.setIfManager(dbObject.get("ifManager").toString());
			gm.setRemark(dbObject.get("remark").toString());
			gm.setInviterId(dbObject.get("inviterId").toString());
			gm.setSettings((BasicDBObject)dbObject.get("settings"));

			DBObject user = AuraUserUtil.getDbObjectByUserId(dbObject.get("memberId").toString());
			if(user==null){
				throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
			}
			//昵称
			gm.setScreenName(((DBObject)user.get("profile")).get("screenName").toString());
			//头像
			String imgPath = ((DBObject)(((DBObject)user.get("profile")).get("avatar"))).get("small").toString();
			if(imgPath==null ||"".equals(imgPath) || "-".equals(imgPath)){
				imgPath = ((DBObject)(((DBObject)user.get("profile")).get("avatar"))).get("smallPath").toString();
			}
			if(imgPath!=null){
				if(imgPath.startsWith("http")){
					imgPath = ((DBObject)(((DBObject)user.get("profile")).get("avatar"))).get("smallPath").toString();
				}
			}
			gm.setImgPath(SystemConstant.SERVER_IMAGE_PATH+imgPath);
			//性别
			if(((DBObject) user.get("profile")).get("gender")!=null) {
				gm.setGender(Integer.parseInt(((DBObject) user.get("profile")).get("gender").toString()));
			}
		}
		return gm;
	}

	@Override
	public int delete(GroupMember groupMember) {
		if(groupMember==null){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject where = new BasicDBObject();
		if(groupMember.getGroupId()!=null){
			where.put("groupId",groupMember.getGroupId());
		}
		if(groupMember.getMemberId()!=null){
			where.put("memberId",groupMember.getMemberId());
		}
		collection.remove(where);
		return 1;
	}

}
