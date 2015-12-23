package common.util.db;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 数据库操作
 * 
 * @author zhangt
 *
 */
@Component
public class MonDB {

	/**
	 *
	 * 对数据库进行更新
	 * 
	 * @param tableName
	 *            表名
	 * @param oldDB
	 *            条件
	 * @param newDB
	 *            更新内容
	 * @param t
	 *            更新方式(0=正常，1=做加法)
	 */
	public void update(String tableName, BasicDBObject oldDB, BasicDBObject newDB) {
		DBCollection table = DetaDiv.getCollection(tableName);
		table.update(oldDB, new BasicDBObject("$set", newDB), false, true);
	}

	public void update(String tableName, BasicDBObject oldDB, BasicDBObject newDB, int t) {
		DBCollection table = DetaDiv.getCollection(tableName);
		String type = "$set";
		if (t == 1) {
			type = "$inc";
		}
		table.update(oldDB, new BasicDBObject(type, newDB), false, true);
	}

	/**
	 * 查询
	 * 
	 * @param tableName
	 *            表名
	 * @param ob
	 *            查询条件
	 * @return
	 */
	public DBCursor findOb(String tableName, DBObject ob) {
		DBCollection table = DetaDiv.getCollection(tableName);
		return table.find(ob);
	}

	/**
	 * 插入
	 * 
	 * @param tableName
	 *            表民
	 * @param ob
	 *            插入内容
	 */
	public void insertOb(String tableName, DBObject ob) {
		DBCollection table = DetaDiv.getCollection(tableName);
		table.insert(ob);
	}

	/**
	 * 插入
	 * 
	 * @param tableName
	 *            表民
	 * @param ob
	 *            插入内容
	 */
	public void insertOb(String tableName, List<DBObject> obs) {
		DBCollection table = DetaDiv.getCollection(tableName);
		table.insert(obs);
	}
}
