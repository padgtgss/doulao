package com.yslt.doulao.dulao.controller;


import common.util.HttpUtil;
import common.util.SignatureUtil;
import common.var.constants.SystemConstant;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: com.yslt.doulao.dulao.controller
 * @anthor: shi_lin
 * @CreateTime: 2015-11-24
 */
public class CashConrollerTest  {

    @Test
    public void testExcute() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        String url ="http://localhost:8888/doulao/X20151118/cashDetail";
        map.put("appId", SystemConstant.APP_ID);
        map.put("userId","55ddc53e0cf29799a6c81f1e");

        SignatureUtil.createSignStrings(map);
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }
}