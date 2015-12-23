package com.yslt.doulao.dulao.controller;


import common.util.AESUtil;
import common.util.HttpUtil;
import common.util.MD5Util;
import common.util.SignatureUtil;

import common.var.constants.SystemConstant;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: com.yslt.doulao.dulao.controller
 * @anthor: shi_lin
 * @CreateTime: 2015-11-21
 */
public class OneCatchControllerTest  {
    @Test
    public void testExcute() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        String url ="http://localhost:8888/doulao/X20151118/turn";
        map.put("appId", SystemConstant.APP_ID);
        map.put("userId","53cf64e40cf2639aa552fb47");
        map.put("extraCost","1");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }





}