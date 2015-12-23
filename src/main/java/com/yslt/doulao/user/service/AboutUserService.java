package com.yslt.doulao.user.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Description: com.yslt.doulao.user.service
 * @author: li_bo
 * @CreateTime: 2015-12-03
 */
public interface AboutUserService {

    Map<String,Object> validatePwd(String mobile,String password);

    Map<String,Object> getValidateCode(String oldMobile,String newMobile,String validateCode,String mei,String UUID);
}
