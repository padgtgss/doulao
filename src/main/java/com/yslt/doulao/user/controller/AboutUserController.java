package com.yslt.doulao.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.yslt.doulao.info.entity.User;
import com.yslt.doulao.user.service.AboutUserService;
import common.util.enums.AvailableEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description: com.yslt.doulao.user.controller
 * @author: li_bo
 * @CreateTime: 2015-12-03
 */
@Controller
@RequestMapping("/aboutUser")
public class AboutUserController {

    @Resource
    private AboutUserService aboutUserService;

    /**
     * 验证密码
     * @param mobile
     * @param password
     * @return
     */
    @RequestMapping(value = "/validatePwd", method = { RequestMethod.GET, RequestMethod.POST },produces = {
            "application/json;charset=UTF-8" })
    @ResponseBody
    public Object validatePwd(String mobile , String password) {
        Map<String,Object> resultMap = aboutUserService.validatePwd(mobile, password);
        return JSON.toJSONString(resultMap);
    }

    /**
     *
     * @param oldMobile 旧手机号
     * @param newMobile 新手机号
     * @param validateCode 用户输入的验证码
     * @return
     */
    @RequestMapping(value = "/getValidateCode", method = { RequestMethod.GET, RequestMethod.POST },produces = {
            "application/json;charset=UTF-8" })
    @ResponseBody
    public Object getValidateCode(String oldMobile,String newMobile , String validateCode,String mei,String UUID) {
        Map<String,Object> resultMap = aboutUserService.getValidateCode(oldMobile, newMobile,validateCode,mei,UUID);
        return JSON.toJSONString(resultMap);
    }
}
