package com.yslt.doulao.chat.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yslt.doulao.chat.dao.GroupDao;
import com.yslt.doulao.chat.entity.Group;

import common.entity.BaseBusinessException;
import common.util.GroupUtil;
import common.util.db.DetaDiv;
import common.util.db.MonDB;
import common.var.exception.GroupError;
import common.var.constants.SystemConstant;

import org.springframework.stereotype.Repository;

@Repository
public class GroupDaoImpl  implements GroupDao {

	private final String TABLE_NAME = "t_group";
	
	@Resource
	private MonDB monDB;
	
	@Override
	public Group insert(Group group) {
		if(group==null){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject dbObject = new BasicDBObject();
		if(group.getId()!=null){
			dbObject.put("_id",group.getId());
		}
		dbObject.put("officialType",group.getOfficialType());
		if(group.getGroupName()!=null){
			dbObject.put("groupName", group.getGroupName());
		}else{
			dbObject.put("groupName", "");
		}
		if(group.getMaster()!= null){
			dbObject.put("master", group.getMaster());
		}else{
			dbObject.put("master", "");
		}
		if(group.getShamMaster()!=null){
			dbObject.put("shamMaster",group.getShamMaster());
		}else{
			dbObject.put("shamMaster","");
		}
		if(group.getImgPath()!=null){
			dbObject.put("imgPath", group.getImgPath());
		}else{
			dbObject.put("imgPath", "");
		}
		if(group.getCategoryCode()!=null){
			dbObject.put("categoryCode", group.getCategoryCode());
		}else{
			dbObject.put("categoryCode", "0");
		}
		SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT);
		dbObject.put("createTime", sdf.format(new Date()));
		dbObject.put("updateTime", "");
		if(group.getType()!=null){
			dbObject.put("type", group.getType());
		}else{
			dbObject.put("type", "0");
		}
		if(group.getPermission()!=null){
			dbObject.put("permission", group.getPermission());
		}else{
			dbObject.put("permission", "0");
		}
		if(group.getDeclared()!=null){
			dbObject.put("declared", group.getDeclared());
		}else{
			dbObject.put("declared", "");
		}
		if(group.getGroupNum()!=null){
			dbObject.put("groupNum",group.getGroupNum());
		}
		collection.insert(dbObject);
		group.setId(dbObject.get("_id").toString());
		group.setMaster(dbObject.get("master").toString());
		group.setImgPath(dbObject.get("imgPath").toString());
		group.setCategoryCode(dbObject.get("categoryCode").toString());
		group.setCreateTime(dbObject.get("createTime").toString());
		group.setType(dbObject.get("type").toString());
		group.setPermission(dbObject.get("permission").toString());
		group.setDeclared(dbObject.get("declared").toString());
		group.setOfficialType(dbObject.get("officialType").toString());
		group.setShamMaster(dbObject.get("shamMaster").toString());
		group.setGroupNum(dbObject.get("groupNum").toString());
		return group;
	}

	@Override
	public int update(Group group) {
		BasicDBObject where = new BasicDBObject();
		if(group==null){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		if(group.getId()==null){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		String groupId = group.getId();
		where.put("_id",groupId);
		BasicDBObject dbObject = new BasicDBObject();
		if(group.getGroupName()!=null){
			dbObject.put("groupName", group.getGroupName());
		}
		if(group.getMaster()!=null){
			dbObject.put("master", group.getMaster());
		}
		if(group.getImgPath()!=null){
			dbObject.put("imgPath", group.getImgPath());
		}
		if(group.getCategoryCode()!=null){
			dbObject.put("categoryCode", group.getCategoryCode());
		}
		SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT);
		dbObject.put("updateTime", sdf.format(new Date()));
		if(group.getType()!=null){
			dbObject.put("type", group.getType());
		}
		if(group.getPermission()!=null){
			dbObject.put("permission", group.getPermission());
		}
		if(group.getDeclared()!=null){
			dbObject.put("declared", group.getDeclared());
		}
		if(group.getGroupNum()!=null){
			dbObject.put("groupNum",group.getGroupNum());
		}
		monDB.update(TABLE_NAME, where, dbObject);
		return 1;
	}

	@Override
	public Group findOne(String groupId) {
		if(groupId==null){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject where = new BasicDBObject();
		where.put("_id",groupId);
		DBObject dbObject = collection.findOne(where);
		Group group = new Group();
		if(dbObject != null){
			group.setId(dbObject.get("_id").toString());
			group.setGroupName(dbObject.get("groupName").toString());
			group.setMaster(dbObject.get("master").toString());
			group.setImgPath(SystemConstant.SERVER_IMAGE_PATH+dbObject.get("imgPath").toString());
			group.setCategoryCode(dbObject.get("categoryCode").toString());
			String categoryName = GroupUtil.getNameByGroupCategory(dbObject.get("categoryCode").toString());
			group.setCategoryName(categoryName);
			group.setCreateTime(dbObject.get("createTime").toString());
			group.setType(dbObject.get("type").toString());
			group.setPermission(dbObject.get("permission").toString());
			group.setDeclared(dbObject.get("declared").toString());
			group.setOfficialType(dbObject.get("officialType").toString());
			group.setShamMaster(dbObject.get("shamMaster").toString());
			group.setGroupNum(dbObject.get("groupNum").toString());

		}
		return group;
	}

	@Override
	public Group findOneByGroupNum(String groupNum) {
		if(groupNum==null){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject where = new BasicDBObject();
		where.put("groupNum",groupNum);
		DBObject dbObject = collection.findOne(where);
		Group group = new Group();
		if(dbObject != null){
			group.setId(dbObject.get("_id").toString());
			group.setMaster(dbObject.get("master").toString());
			group.setImgPath(dbObject.get("imgPath").toString());
			group.setCategoryCode(dbObject.get("categoryCode").toString());
			String categoryName = GroupUtil.getNameByGroupCategory(dbObject.get("categoryCode").toString());
			group.setCategoryName(categoryName);
			group.setCreateTime(dbObject.get("createTime").toString());
			group.setType(dbObject.get("type").toString());
			group.setPermission(dbObject.get("permission").toString());
			group.setDeclared(dbObject.get("declared").toString());
			group.setOfficialType(dbObject.get("officialType").toString());
			group.setShamMaster(dbObject.get("shamMaster").toString());
			group.setGroupNum(dbObject.get("groupNum").toString());
		}
		return group;
	}

	@Override
	public List<Group> findList(Group group) {
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject where = new BasicDBObject();
		if(group.getGroupName()!=null){
			Pattern pattern = Pattern.compile("^.*"+group.getGroupName()+".*$", Pattern.CASE_INSENSITIVE);//设置模糊查询
			where.put("groupName", pattern);
		}
		if(group.getMaster()!=null){
			where.put("master", group.getMaster());
		}
		if(group.getCategoryCode()!=null){
			where.put("categoryCode", group.getCategoryCode());
		}
		if(group.getShamMaster()!=null){
			where.put("shamMaster",group.getShamMaster());
		}
		if(group.getGroupNum()!=null) {
			Pattern pattern = Pattern.compile("^.*"+group.getGroupNum()+".*$", Pattern.CASE_INSENSITIVE);//设置模糊查询
			where.put("groupNum",pattern);
		}
		DBCursor dbCursor = collection.find(where);
		List<Group> groupList = new ArrayList<Group>();
		while(dbCursor.hasNext()){
			DBObject dbObject = dbCursor.next();
			Group g = new Group();
			g.setId(dbObject.get("_id").toString());
			g.setMaster(dbObject.get("master").toString());
			g.setImgPath(dbObject.get("imgPath").toString());
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
			groupList.add(g);
		}
		return groupList;
	}

	@Override
	public DBCursor findList2(Group group) {
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		BasicDBObject where = new BasicDBObject();
		if(group.getGroupName()!=null){
			Pattern pattern = Pattern.compile("^.*"+group.getGroupName()+".*$", Pattern.CASE_INSENSITIVE);//设置模糊查询
			where.put("groupName", pattern);
		}
		if(group.getMaster()!=null){
			where.put("master", group.getMaster());
		}
		if(group.getCategoryCode()!=null){
			where.put("categoryCode", group.getCategoryCode());
		}
		if(group.getGroupNum()!=null){
			where.put("groupNum",group.getGroupNum());
		}
		DBCursor dbCursor = collection.find(where);
		return dbCursor;
	}

	@Override
	public int delete(String groupId) {
		if(groupId==null||"".equals(groupId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
		DBCollection collection1 = DetaDiv.getCollection("t_groupMember");
		BasicDBObject where = new BasicDBObject();
		where.put("_id",groupId);
		BasicDBObject where1 = new BasicDBObject();
		where1.put("groupId",groupId);
		collection.remove(where);
		collection1.remove(where1);
		return 1;
	}


}
