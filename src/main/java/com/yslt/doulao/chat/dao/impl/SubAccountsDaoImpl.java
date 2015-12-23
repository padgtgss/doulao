package com.yslt.doulao.chat.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.chat.dao.SubAccountsDao;
import com.yslt.doulao.chat.entity.SubAccounts;
import common.util.db.DetaDiv;
import org.springframework.stereotype.Repository;

/**
 * @Description: com.yslt.doulao.chat.dao.impl
 * @author: li_bo
 * @CreateTime: 2015-11-27
 */
@Repository
public class SubAccountsDaoImpl implements SubAccountsDao {
    private final String TABLE_NAME = "t_subAccounts";

    @Override
    public void insert(SubAccounts subAccounts) {
        DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
        BasicDBObject doc = new BasicDBObject();
        doc.put("userId",subAccounts.getUserId());
        doc.put("subAccountSid",subAccounts.getSubAccountSid());
        doc.put("subToken",subAccounts.getSubToken());
        doc.put("dateCreated",subAccounts.getDateCreated());
        doc.put("voipAccount",subAccounts.getVoipAccount());
        doc.put("voipPwd",subAccounts.getVoipPwd());
        collection.insert(doc);
    }

    @Override
    public DBObject findOne(String userId, String subAccountSid) {
        DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
        BasicDBObject where = new BasicDBObject();
        where.put("userId",userId);
        where.put("subAccountSid",subAccountSid);
        DBObject dbObject = collection.findOne(where);
        return dbObject;
    }

    @Override
    public BasicDBObject updateByUp(SubAccounts subAccounts) {
        DBCollection collection = DetaDiv.getCollection(TABLE_NAME);
        BasicDBObject where = new BasicDBObject();
        where.put("userId",subAccounts.getUserId());
        BasicDBObject doc = new BasicDBObject();
        doc.put("userId",subAccounts.getUserId());
        doc.put("subAccountSid",subAccounts.getSubAccountSid());
        doc.put("subToken",subAccounts.getSubToken());
        doc.put("dateCreated",subAccounts.getDateCreated());
        doc.put("voipAccount",subAccounts.getVoipAccount());
        doc.put("voipPwd",subAccounts.getVoipPwd());
        collection.update(where,doc,true,false);
        return doc;
    }

/*    public static DBObject updateByUp2(SubAccounts subAccounts) {
        if(subAccounts==null){
            throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
        }
        DBCollection collection = DetaDiv.getCollection("t_subAccounts");
        BasicDBObject where = new BasicDBObject();
        where.put("userId",subAccounts.getUserId());
        BasicDBObject doc = new BasicDBObject();
        doc.put("userId",subAccounts.getUserId());
        doc.put("subAccountSid",subAccounts.getSubAccountSid());
        doc.put("subToken",subAccounts.getSubToken());
        doc.put("dateCreated",subAccounts.getDateCreated());
        doc.put("voipAccount",subAccounts.getVoipAccount());
        doc.put("voipPwd",subAccounts.getVoipPwd());
        collection.update(where,doc,true,false);
        return doc;
    }

    public static void main(String[] args){
        SubAccounts subAccounts = new SubAccounts();
        subAccounts.setUserId("53e622730cf28718690a5c81");
        subAccounts.setSubAccountSid("1133d09d981d11e5bb61ac853d9d52fd");
        subAccounts.setSubToken("3f322aebf19b07998bea52dda815ddc8");
        subAccounts.setDateCreated("2015-12-01 19:17:11");
        subAccounts.setVoipAccount("8000900900000023");
        subAccounts.setVoipPwd("cIQTObl2");
        updateByUp2(subAccounts);
    }*/

}
