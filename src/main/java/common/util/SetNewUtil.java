package common.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import common.util.db.DetaDiv;

/**
 * @Description: common.util
 * @author: li_bo
 * @CreateTime: 2015-12-15
 */
public class SetNewUtil {

    public static String getGlobalPro(int type){
        DBCollection collection = DetaDiv.getCollection("setNew");
        BasicDBObject where = new BasicDBObject();
        where.put("type",type);
        DBObject dbObject = collection.findOne(where);
        return dbObject.get("pro").toString();
    }
}
