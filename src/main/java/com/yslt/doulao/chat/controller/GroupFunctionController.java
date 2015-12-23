package com.yslt.doulao.chat.controller;

import com.alibaba.fastjson.JSON;
import com.yslt.doulao.chat.service.GroupFunctionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description: com.yslt.doulao.chat.controller
 * @author: li_bo
 * @CreateTime: 2015-12-15
 */
@Controller
@RequestMapping("/groupfunc")
public class GroupFunctionController {

    @Resource
    private GroupFunctionService groupFunctionService;

    //群召唤
    @RequestMapping(value="/groupCall", method = {RequestMethod.GET,RequestMethod.POST},produces = {
            "application/json;charset=UTF-8"})
    @ResponseBody
    public Object groupCall(String groupId, String userId,String content){
        Map<String,Object> resultMap = groupFunctionService.groupCall(groupId, userId , content);
        return JSON.toJSONString(resultMap);
    }

}
