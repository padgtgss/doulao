package com.yslt.doulao.dulao.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.yslt.doulao.dulao.enums.AdvertisementTypeEnum;
import com.yslt.doulao.dulao.service.AdvertisementService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: AdvertisementController
 * @anthor: shi_lin
 * @CreateTime: 2015-11-26
 */
@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {

    @Resource
    private AdvertisementService advertisementService;

    @RequestMapping(method = RequestMethod.POST, produces = {
            "application/json;charset=UTF-8" })
    @ResponseBody
    public Object saveAdvertisement(Long startTime,Long endTime,String type ,@RequestParam(value = "img", required = false) MultipartFile img){
        String info = "";
        Map<String,Object > m = new HashMap<String, Object>();
        m.put("status",1);
        m.put("data", ImmutableMap.of("result", false));
        if (startTime == null){
            info = "startTime is not a null ";
            m.put("info",info);

        }
        if (endTime == null){
            info = "endTime is not a null ";

        }
        if (type == null || "".equals(type)){
            info = "type is not a null ";

        }
        if (img == null ){
            info = "img is not a null ";

        }

        m.put("info",info);
        if(!"".equals(info)){
            m.put("status",0);
            m.put("info",info);
            return JSON.toJSONString(m);
        }else {
            AdvertisementTypeEnum typeEnum = AdvertisementTypeEnum.getByType(Integer.parseInt(type));
            info =  advertisementService.save(startTime,endTime,typeEnum,img);
            m.put("data", ImmutableMap.of("result", true));
            if(info != null){
                m.put("status",0);
                m.put("data", ImmutableMap.of("result", false));
            }
            m.put("info",info);
            return JSON.toJSONString(m);
        }

    }


    /**
     * 接口app端异常测试
     *
     * @return
     */
    @RequestMapping(value = "/tt", method = { RequestMethod.GET },produces = {
            "application/json;charset=UTF-8" })
    @ResponseBody
    public Object test() {

        return new Date();

        //	return JSON.toJSONString(ImmutableMap.of("status", "0", "message", "需要花费10个经验值哦！"));
		/*if (1 == 1) {
			throw new BaseBusinessException(SysError.PASSWORD_ERROR);
		}
		return new Date();*/
    }

}
