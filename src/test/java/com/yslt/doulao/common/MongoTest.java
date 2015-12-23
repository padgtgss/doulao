package com.yslt.doulao.common;

import common.util.db.BaseDaoImpl;
import common.util.enums.AvailableEnum;
import common.util.pojo.DomainPage;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: MongoTest
 * @anthor: shi_lin
 * @CreateTime: 2015-12-04
 */

public class MongoTest extends BaseDaoImpl {



    public   void save(){
        User u = new User();
        u.setUsername("1111");
        u.setPassword("12333");
        u.setCreateTime(new Date());
        u.setUpdateTime(new Date());
        u.setAvailableEnum(AvailableEnum.AVAILABLE);
        Object o = ds.save(u);

    }


    @Test
    public void test(){
        System.out.println("234234239478");
        this.save();
        System.out.println("234234239478");

    }

    public void query(){
        List<User> users = ds.find(User.class).asList();

        User user = users.get(0);
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println(format.format(user.getCreateTime()));
        System.out.println("234234239478");
    }


    public void find(){
       BaseDaoImpl  dao = new BaseDaoImpl();
        DomainPage<User> username = dao.getEntitiesByPage(User.class, "username", "77777", 2l, 2l);
        System.out.println(username.getDomains().size());

    }
}
