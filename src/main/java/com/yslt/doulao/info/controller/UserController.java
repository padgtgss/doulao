package com.yslt.doulao.info.controller;

import com.alibaba.fastjson.JSONArray;
import com.yslt.doulao.dulao.service.UserCatchCashDetailService;
import com.yslt.doulao.info.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: UserController
 * @anthor: shi_lin
 * @CreateTime: 2015-11-20
 */

@Controller
@RequestMapping("/X20151118")
public class UserController {

    @Resource
  private UserCatchCashDetailService userCatchCashDetailService;
    @Resource
    private UserService  userService;
    /**
     * 获取用户现金
     * @param userId
     * @return
     */
    @RequestMapping(value = "/cash", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
            "application/json;charset=UTF-8" })
    @ResponseBody
    public Object userCashDetail(String userId){
        return JSONArray.toJSONString(userCatchCashDetailService.getCash(userId));
    }
    @RequestMapping(value = "/cash/detail", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
            "application/json;charset=UTF-8" })
    @ResponseBody
    public Object getSmallCashDetail(String userId,String recordtype){
      return  userService.getCashDetail(userId,recordtype);

    }
}
