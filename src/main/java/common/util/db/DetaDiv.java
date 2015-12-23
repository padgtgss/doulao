package common.util.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import common.var.constants.SystemConfig;

/**
 * @Description: DetaDiv
 * @anthor: shi_lin
 * @CreateTime: 2015-11-10
 */

public class DetaDiv {
	private DB db = null;

	private static DetaDiv detaDiv = new DetaDiv();

	public DetaDiv() {
		init();
	}

	private void init() {
		try {
			MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
			builder.autoConnectRetry(false);
			builder.socketKeepAlive(true);
			builder.connectionsPerHost(200);
			builder.maxWaitTime(50000);
			builder.socketTimeout(30000);
			builder.connectTimeout(50000);
			builder.threadsAllowedToBlockForConnectionMultiplier(10);
			MongoClientOptions opt = builder.build();
			// MongoClient mongo = new MongoClient(new
			// ServerAddress("localhost", 27017), opt);
			// MongoClient mongo = new MongoClient(new
			// ServerAddress("182.92.164.219", 27017), opt);
			// MongoClient mongo = new MongoClient(new
			// ServerAddress("122.0.114.161", 27017), opt);
			// MongoClient mongo = new MongoClient(new
			// ServerAddress("122.0.114.144", 27017), opt);
			// MongoClient mongo = new MongoClient(new
			// ServerAddress("192.168.1.187", 27017), opt);
			//MongoClient mongo = new MongoClient(new ServerAddress("123.57.149.93", 27017), opt);
			MongoClient mongo = new MongoClient(new ServerAddress(SystemConfig.MONGO_HOST, SystemConfig.MONGO_PORT), opt);
			db = mongo.getDB(SystemConfig.MONGO_DBNAME);

			/*-- 数据库集群链接开始 --*/
			
/*			  MongoOptions opt = new MongoOptions(); opt.safe=true;
			  opt.connectionsPerHost=200; opt.connectTimeout=30000;
			  opt.maxWaitTime=50000; opt.autoConnectRetry=false;
			  opt.socketTimeout = 30000;
			  opt.threadsAllowedToBlockForConnectionMultiplier = 10;
			  ServerAddress sap0 = new ServerAddress("10.172.231.233",27017);
			  ServerAddress sap1 = new ServerAddress("10.173.33.181",27017);
			  ServerAddress sap2 = new ServerAddress("10.172.244.2",27017);
			  List<ServerAddress> list = new ArrayList<ServerAddress>();
			  list.add(sap0); list.add(sap1); list.add(sap2); Mongo mongo=new
			  Mongo(list,opt); db = mongo.getDB("auramain");*/
			 
			/*-- 数据库集群链接结束 --*/
		} catch (Exception e) {
			// log error
		}
	}

	public static DBCollection getCollection(String tableName) {
		DBCollection table = null;
		DB db = detaDiv.db;
/*		if (db.authenticate("portaura", "portaura303".toCharArray())) {
			table = db.getCollection(tableName);
		}*/
		if (db.authenticate(SystemConfig.MONGO_USERNAME, SystemConfig.MONGO_PASSWORD.toCharArray())) {
			table = db.getCollection(tableName);
		}
		return table;
	}

	public static DB getDB() {
		return detaDiv.db;
	}
}
