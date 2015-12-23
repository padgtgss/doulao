package com.yslt.doulao.info.controller;


import common.util.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: com.yslt.doulao.info.controller
 * @anthor: shi_lin
 * @CreateTime: 2015-11-21
 */
public class UserControllerTest  {

    @Test
    public void test()throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        String url ="http://localhost:8888/doulao/X20151118/cash";
        map.put("userId","55ddc53e0cf29799a6c81f1e");
     //   map.put()
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }
}