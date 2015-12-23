package common.util;

import com.mongodb.*;

import common.entity.BaseBusinessException;
import common.util.db.DetaDiv;
import common.util.db.MonDB;
import common.var.exception.GroupError;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 与群相关的工具方法
 * @author Libo
 *
 */
public class GroupUtil {

	/**
	 * 检查是否已经是群成员
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public static DBObject checkIfIsGroupMember(String groupId,String userId){
		DBCollection collection = DetaDiv.getCollection("t_groupMember");
		BasicDBObject where = new BasicDBObject();
		where.put("status","1");
		where.put("groupId",groupId);
		where.put("memberId",userId);
		DBObject dbObject = collection.findOne(where);
		return  dbObject;
	}

	/**
	 * 检查是否在申请中
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public static DBObject checkIfIsApplying(String groupId,String userId){
		DBCollection collection = DetaDiv.getCollection("t_groupMember");
		BasicDBObject where = new BasicDBObject();
		where.put("status","3");
		where.put("groupId",groupId);
		where.put("memberId",userId);
		DBObject dbObject = collection.findOne(where);
		return  dbObject;
	}

	/**
	 * 检查是否已经有群-成员关系数据
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public static DBObject checkIfInsertGroupMember(String groupId,String userId){
		DBCollection collection = DetaDiv.getCollection("t_groupMember");
		BasicDBObject where = new BasicDBObject();
		where.put("groupId", groupId);
		where.put("memberId", userId);
		DBObject dbObject = collection.findOne(where);
		return dbObject;
	}

	/**
	 * 检查是否已经有群
	 * @param groupId
	 */
	public static DBObject checkIfHavaGroup(String groupId){
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBObject where = new BasicDBObject();
		where.put("_id",groupId);
		DBObject dbObject = collection.findOne(where);
		return dbObject;
	}

	/**
	 * 根据群号groupNum检查是否已经有群
	 * @param groupNum
	 * @return
	 */
	public static DBObject checkIfHavaGroupByGroupNum(String groupNum){
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBObject where = new BasicDBObject();
		where.put("groupNum",groupNum);
		DBObject dbObject = collection.findOne(where);
		return dbObject;
	}

	/**
	 * 检查是否是这个群的群主
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public static DBObject checkIsGroupMaster(String groupId,String userId){
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBObject where = new BasicDBObject();
		where.put("_id",groupId);
		where.put("master",userId);
		DBObject dbObject = collection.findOne(where);
		return dbObject;
	}

	/**
	 * 检查是否是这个群的管理员
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public static boolean checkIsGroupManager(String groupId,String userId){
		DBCollection collection = DetaDiv.getCollection("t_groupMember");
		BasicDBObject where = new BasicDBObject();
		where.put("status","1");
		where.put("ifManager","1");
		where.put("memberId",userId);
		where.put("groupId",groupId);
		int a = collection.find(where).count();
		if(a>0){
			return true;
		}
		return false;
	}

	/**
	 * 检查是否是这个群的伪群主
	 * @param groupId
	 * @param shamMaster
	 * @return
	 */
	public static DBObject checkIfGroupShamMaster(String groupId,String shamMaster){
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBObject where= new BasicDBObject();
		where.put("_id",groupId);
		where.put("shamMaster",shamMaster);
		DBObject dbObject = collection.findOne(where);
		return dbObject;
	}

	/**
	 * 检查是否是群主或伪群主
	 */
	public static DBObject checkIfMasterOrShamMaster(String groupId,String userId){
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBList list = new BasicDBList();
		list.add(new BasicDBObject("master",userId));
		list.add(new BasicDBObject("shamMaster",userId));
		BasicDBObject where = new BasicDBObject();
		where.put("_id",groupId);
		where.put(QueryOperators.OR,list);
		DBObject dbObject = collection.findOne(list);
		return dbObject;
	}

	/**
	 * 根据用户Id获取容联那边有群主权限的用户id
	 * @param userId
	 * @return 如果返回不为空则表示这个用户作为群主或者伪群主，是有权限的，把容联那边指定的群主id返回。
	 * 		   如果返回为null，则表示这个用户是没有权限的。
	 */
	public static String getRLGroupMasterByUserId(String groupId,String userId){
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBObject where1 = new BasicDBObject();
		where1.put("master",userId);
		where1.put("_id",groupId);
		DBObject doc1 = collection.findOne(where1);
		if(doc1!=null){
			return userId;
		}
		BasicDBObject where2 = new BasicDBObject();
		where2.put("shamMaster",userId);
		where2.put("_id",groupId);
		DBObject doc2 = collection.findOne(where2);
		if(doc2!=null){
			return doc2.get("master").toString();
		}
		return null;
	}

	/**
	 * 找出master和shamMaster相同的群的id并打印出来
	 * @param userId
	 */
	public static void getGroupIdWhenMasterEqShamMaster(String userId){
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBObject where = new BasicDBObject();
		where.put("master",userId);
		where.put("shamMaster",userId);
		DBCursor cursor = collection.find(where);
		while(cursor.hasNext()){
			DBObject doc = cursor.next();
			System.out.println(doc.get("_id").toString());
		}
	}

	/**
	 * 获取群分类代号对应的群分类名称
	 * @param groupCategory
	 * @return
	 */
	public static String getNameByGroupCategory(String groupCategory){
		if(groupCategory==null||"".equals(groupCategory)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		DBCollection collection = DetaDiv.getCollection("t_groupCategory");
		BasicDBObject where = new BasicDBObject();
		where.put("gcCode",groupCategory);
		DBObject doc = collection.findOne(where);
		if(doc!=null){
			return doc.get("gcName").toString();
		}else{
			throw new BaseBusinessException(GroupError.CATEGORY_NOT_EXISTS);
		}
	}

	/**
	 * 获取我有群主权限的群数量
	 * @param userId
	 * @return
	 */
	public static int getCreateGroupNum(String userId){
		if(userId==null||"".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBList dbList = new BasicDBList();
		dbList.add(new BasicDBObject("master",userId));
		dbList.add(new BasicDBObject("shamMaster",userId));
		BasicDBObject where = new BasicDBObject();
		where.put(QueryOperators.OR,dbList);
		int num = collection.find(where).count();
		return num;
	}

	/**
	 * 获取我有群主权限的群Cursor
	 * @param userId
	 * @return
	 */
	public static DBCursor getCreateGroupCursor(String userId){
		if(userId==null||"".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		DBCollection collection = DetaDiv.getCollection("t_group");
		BasicDBList dbList = new BasicDBList();
		dbList.add(new BasicDBObject("master",userId));
		dbList.add(new BasicDBObject("shamMaster",userId));
		BasicDBObject where = new BasicDBObject();
		where.put(QueryOperators.OR,dbList);
		DBCursor cur = collection.find(where);
		return cur;
	}

	/*public static void dealWithGrabRedNum(){
		DBCollection userTable = DetaDiv.getCollection("AuraUser");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		BasicDBObject where = new BasicDBObject();
		where.put("grabRedTime",new BasicDBObject(QueryOperators.LT,sdf.format(new Date())));
		where.put("grabRedNum",new BasicDBObject(QueryOperators.GT,0));
		BasicDBObject doc = new BasicDBObject();
		doc.put("grabRedNum",0);
		int a = userTable.find(where).count();
		MonDB monDB = new MonDB();
		monDB.update("AuraUser",where,doc);
		System.out.println("--执行了"+a+"条数据的操作--");
	}*/

}
