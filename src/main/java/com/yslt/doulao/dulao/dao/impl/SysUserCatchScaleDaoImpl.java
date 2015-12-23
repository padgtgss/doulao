package com.yslt.doulao.dulao.dao.impl;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.dulao.dao.SysUserCatchScaleDao;
import com.yslt.doulao.dulao.entity.SysUserCatchScale;
import common.util.db.DetaDiv;
import org.springframework.stereotype.Repository;


/**
 * @Description: SysUserCatchScaleDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-18
 */
@Repository
public class SysUserCatchScaleDaoImpl implements SysUserCatchScaleDao {
    private final String TABLE_NAME = "sys_user_catch_scale";

    @Override
    public SysUserCatchScale getByUserleave(String userLeave) {
        DBCollection dbCollection = DetaDiv.getCollection(TABLE_NAME);
        DBObject query = new BasicDBObject();
        query.put("userLeave", userLeave);
        DBObject one = dbCollection.findOne(query);
        SysUserCatchScale sysUserCatchScale =  new SysUserCatchScale();
        sysUserCatchScale.setScale(one.get("scale").toString());
        sysUserCatchScale.setUserLeave(userLeave);
        sysUserCatchScale.setId(one.get("_id").toString());
        return  sysUserCatchScale;
    }
}


