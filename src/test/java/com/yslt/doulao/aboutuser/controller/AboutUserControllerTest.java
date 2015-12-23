package com.yslt.doulao.aboutuser.controller;

import common.util.AESUtil;
import common.util.HttpUtil;
import common.util.MD5Util;
import common.util.SignatureUtil;
import common.var.constants.SystemConstant;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: com.yslt.doulao.aboutuser.controller
 * @author: li_bo
 * @CreateTime: 2015-12-03
 */
public class AboutUserControllerTest {

    //测试更绑手机号
    @Test
    public void getValidateCode() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/aboutUser/getValidateCode";
        map.put("oldMobile","18733334444");
        map.put("newMobile","13540455035");
        map.put("validateCode","533040");
        map.put("mei","123");
        map.put("UUID","123");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString, SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试密码
    @Test
    public void validatePwd() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/aboutUser/validatePwd";
        map.put("mobile","18100800685");
        map.put("password","123456");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString, SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }
}
