/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.yslt.doulao.common;

import common.util.db.BaseDao;
import common.util.db.BaseDaoImpl;
import common.util.enums.AvailableEnum;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: UserTest
 * @Author: lin.shi
 * @CreateTime: 2015-12-05 17:12
 */
public class UserTest extends AbstractTestNGSpringContextTests {

    BaseDao baseDao = new BaseDaoImpl();
    @Test
    public void test()throws Exception{
        System.out.println("start------");
        this.save();
        System.out.println("end------");

    }


    public void update(){
        Map<String,Object> params= new HashMap<String,Object>();
        Map<String,Object> params1= new HashMap<String,Object>();

        params.put("username","wangwu2222");
        params.put("password","12333");
        params1.put("availableEnum", AvailableEnum.UNAVAILABLE);
        params1.put("password","11111");

        baseDao.update(User.class,"5662cd378b3b25a62e0fc3f8",params1);

    }


    public void remove(){

        Map<String,Object> params= new HashMap<String,Object>();
        params.put("username","wangwu2222");
        params.put("password","11");
        baseDao.remove(User.class,params);

    }


    public void merge(){
        User user = baseDao.getEntityById(User.class, "5661409b0f3e809899f066c6");
        user.setUsername("merge");
        user.setPassword("merge123");
        baseDao.merge(user);
    }

    public void save(){
        User user = new User();
        user.setPassword("008989");
        user.setUsername("000000");
        user.setAge(33);
        baseDao.save(user);
    }
}