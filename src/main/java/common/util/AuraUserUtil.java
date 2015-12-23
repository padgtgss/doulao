package common.util;

import com.mongodb.*;

import common.entity.BaseBusinessException;
import common.util.db.DetaDiv;
import common.var.exception.DefaultError;
import common.var.exception.GroupError;
import common.var.constants.SystemConstant;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 关于用户的工具方法
 * @author Libo
 *
 */
public class AuraUserUtil {

	//根据userId拿出用户dbObject
	public static DBObject getDbObjectByUserId(String userId){
		if(userId==null){
			throw new BaseBusinessException(DefaultError.SYSTEM_INTERNAL_ERROR);
		}
		DBCollection collection = DetaDiv.getCollection("AuraUser");
		DBObject dbObject = collection.findOne(new BasicDBObject("_id",userId));
		return dbObject;
	}

	//根据mobile拿出用户dbObject
	public static DBObject getDbObjectByMobile(String mobile){
		if(mobile==null){
			throw new BaseBusinessException(DefaultError.SYSTEM_INTERNAL_ERROR);
		}
		DBCollection collection = DetaDiv.getCollection("AuraUser");
		DBObject dbObject = collection.findOne(new BasicDBObject("mobile",mobile));
		return dbObject;
	}


	//根据userId拿出子账户Id
	public static String getSubAccountSidByUserId(String userId){
		if(userId==null){
			throw new BaseBusinessException(DefaultError.SYSTEM_INTERNAL_ERROR);
		}
		DBCollection collection = DetaDiv.getCollection("t_subAccounts");
		BasicDBObject where = new BasicDBObject();
		where.put("userId",userId);
		DBObject dbObject = collection.findOne(where);
		if (dbObject!=null){
			String subAccountSid = dbObject.get("subAccountSid").toString();
			return  subAccountSid;
		}
		return null;
	}

	//根据userId拿出子账户
	public static DBObject getSubAccountByUserId(String userId){
		if(userId==null){
			throw new BaseBusinessException(DefaultError.SYSTEM_INTERNAL_ERROR);
		}
		DBCollection collection = DetaDiv.getCollection("t_subAccounts");
		BasicDBObject where = new BasicDBObject();
		where.put("userId",userId);
		DBObject dbObject = collection.findOne(where);
		return dbObject;
	}

	//根据userId拿出用户钻卡等级
	public static int getUserDiamondLevel(String userId){
		if(userId==null || "".equals(userId)){
			throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
		}
		int drLevel = -1;
		DBCollection collection = DetaDiv.getCollection("t_userDr");
		BasicDBObject where = new BasicDBObject();
		where.put("userId",userId);
		SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT);
		where.put("activeTime",new BasicDBObject(QueryOperators.GTE,sdf.format(new Date())));
		DBCursor cursor = collection.find(where).sort(new BasicDBObject("drLevel",-1));
		if(cursor.size()>0){
			drLevel = Integer.parseInt(cursor.next().get("drLevel").toString());
		}
		return drLevel;
	}

	//根据钻卡等级拿出建群数目
	public static int getCreateGroupLimitNum(int drLevel){
		DBCollection collection = DetaDiv.getCollection("DiamondTable");
		BasicDBObject where = new BasicDBObject();
		where.put("dLevelType",drLevel);
		DBObject doc = collection.findOne(where);
		if(doc==null) {
			throw new BaseBusinessException(GroupError.DIAMOND_NOT_EXISTS);
		}
		return Integer.parseInt(doc.get("groupNum").toString());
	}
}
