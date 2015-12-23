package com.yslt.doulao.chat.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.yslt.doulao.chat.entity.SubAccounts;

/**
 * @Description: com.yslt.doulao.chat.dao
 * @author: li_bo
 * @CreateTime: 2015-11-27
 */
public interface SubAccountsDao {
    void insert(SubAccounts subAccounts);

    DBObject findOne(String userId, String subAccountSid);

    BasicDBObject updateByUp(SubAccounts subAccounts);
}
