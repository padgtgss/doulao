/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.yslt.doulao.aboutuser.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import common.util.db.DetaDiv;
import common.util.db.MonDB;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: v等级升级
 * @Author: lin.shi
 * @CreateTime: 2015-12-06 17:37
 */
public class Test {
    @Resource
       private MonDB monDB;
    DBCollection auraUser = DetaDiv.getCollection("AuraUser");
    public void upgrade(String userId,int vLeavel){

        DBObject currentUser = auraUser.findOne(new BasicDBObject("_id", userId));
        String bossId = currentUser.get("boss").toString();
        if("".equals(bossId) || bossId == null) return;

        //获取上一级
        for(;;){
            if(this.checkUserLeavel(bossId,vLeavel)){
                currentUser = auraUser.findOne(new BasicDBObject("_id", bossId));
                bossId = currentUser.get("boss").toString();
                if("".equals(bossId) || bossId == null) break;
            }else break;
        }


    }


    public boolean checkUserLeavel(String bossId,int vLeavel){
        DBObject bossUser = auraUser.findOne(new BasicDBObject("_id", bossId));
        Object bossVLeavel = bossUser.get("user_v_leavel");
        if(bossVLeavel !=null && !"".equals(bossVLeavel)){
            if(Integer.parseInt(bossVLeavel.toString()) != vLeavel )
                return false;  //推荐用户等级 远 大于当前用户等级，无法为推荐用户升级
        }


        DBCursor under = auraUser.find(new BasicDBObject("boss", bossId));
        if(under == null || under.size() <3) return false;  //被推荐人数不足3个

        int underUserCount = 0;
        while (under.hasNext()){
            DBObject user = under.next();
            Object leavel = user.get("user_v_leavel");
            if(leavel != null && !"".equals(leavel)){
                if(Integer.parseInt(leavel.toString()) == vLeavel)
                    underUserCount ++ ;
            }
        }
        if(underUserCount <3) return false;  //被推荐用户合格等级人数不足3 个

        //为推荐用户提升等级
        monDB.update("AuraUser",new BasicDBObject("_id",bossId),new BasicDBObject("user_v_leavel",1),1);
        return true;
    }




    public void checkUser2Leave(String userId,int diamondLeave){
       /* if(diamondLeave == 4){   //黑钻用户直接到V0
            this.setLeavel2V0(userId);
        }else if(diamondLeave == 3){  //红钻用户*/
            DBObject user = auraUser.findOne(new BasicDBObject("_id", userId));
            long time = Long.valueOf(user.get("createdAt").toString());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new Date(time));
             calendar1.add(Calendar.YEAR,1);



        if(calendar1.getTime().getTime() > new Date().getTime()){
            return;
        }

      //  }

    }

    @org.junit.Test
    public void test(){
        this.checkUser2Leave("55f787f60cf29799a6d0d2ba",3);
    }


}