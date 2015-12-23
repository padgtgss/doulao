package common.util.db;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import common.var.constants.SystemConfig;

import java.net.UnknownHostException;

/**
 * @Description: 数据库链接
 * @anthor: shi_lin
 * @CreateTime: 2015-12-04
 */

public class DBCollection {

    public static Mongo mongo;
    public static Morphia morphia;
    public static Datastore datastore;

    static {
        try {
            mongo = new Mongo(SystemConfig.MONGO_HOST,SystemConfig.MONGO_PORT);
            morphia = new Morphia();
            datastore = morphia.createDatastore(mongo,SystemConfig.MONGO_DBNAME);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }



}
