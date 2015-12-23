package common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.dulao.pojo.CashPojo;
import com.yslt.doulao.dulao.pojo.LlqRecordPojo;
import common.util.db.DetaDiv;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: common.util
 * @author: li_bo
 * @CreateTime: 2015-12-09
 */
public class PHPUtil {

    /**
     * 获取流量券记录
     * @param userId
     * @return
     */
    public static List<LlqRecordPojo> getLlqRecord(String userId){
        DBCollection userTable = DetaDiv.getCollection("AuraUser");
        BasicDBObject where = new BasicDBObject();
        where.put("_id", userId);
        DBObject dbObject = userTable.findOne(where);
        if(dbObject == null){
            return null;
        }
        String mobile = dbObject.get("mobile").toString();
        MD5Util md5 = new MD5Util();
        String hash = md5.getMD5(mobile+"getllqrecords");
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("username", mobile));
        params.add(new BasicNameValuePair("action", "getllqrecords"));
        params.add(new BasicNameValuePair("appkey", "X7J28D9CK3"));
        params.add(new BasicNameValuePair("hash", hash));
        HttpUtil httpUtil = new HttpUtil();
        String s = httpUtil.getResponse2(params, "balance");
        s.replaceAll("\"", "\\\"");
        JSONObject jsonObject = JSONObject.parseObject(s);
        String status = jsonObject.getString("status");
        if("1".equals(status)){
            String array = jsonObject.getString("data");
            JSONArray jsonArray = JSONArray.parseArray(array);
            int size = jsonArray.size();
            List<LlqRecordPojo> list = new ArrayList<LlqRecordPojo>();
            for(int i = 0;i<size;i++){
                JSONObject jo = jsonArray.getJSONObject(i);
                LlqRecordPojo llqRecord = new LlqRecordPojo();
                llqRecord.setDescript(jo.getString("scash_descript"));
                llqRecord.setSource(jo.getString("scash_source"));
                llqRecord.setEditTime(jo.getString("edittime"));
                llqRecord.setNum(jo.getString("c_freeze"));
                list.add(llqRecord);
            }
            return list;
        }else {
            return null;
        }
    }

    /**
     * 获取现金、零钱、流量石、流量券
     * @param userId
     * @return
     */
    public static CashPojo cashAndCoinBalace(String userId){
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
        return cashPojo;
    }

    public static void main(String[] args){
        /*CashPojo cashPojo = cashAndCoinBalace("56286d680cf26a7a9b5baa16");
        System.out.println(cashPojo.getCash());
        System.out.println(cashPojo.getSmallCash());
        System.out.println(cashPojo.getLls());
        System.out.println(cashPojo.getLlq());*/

        /*List<LlqRecordPojo> list = getLlqRecord("56286d680cf26a7a9b5baa16");
        for(LlqRecordPojo llqRecord:list){
            System.out.println(llqRecord.getEditTime());
            System.out.println(llqRecord.getNum());
            System.out.println(llqRecord.getSource());
            System.out.println(llqRecord.getSource());
        }*/
    }
}
