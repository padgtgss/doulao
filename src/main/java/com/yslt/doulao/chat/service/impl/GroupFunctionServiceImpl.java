package com.yslt.doulao.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.chat.service.GroupFunctionService;
import common.entity.BaseBusinessException;
import common.util.*;
import common.util.db.BaseDao;
import common.util.db.DetaDiv;
import common.var.constants.SystemConstant;
import common.var.exception.GroupError;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: com.yslt.doulao.chat.service.impl
 * @author: li_bo
 * @CreateTime: 2015-12-15
 */
@Service
public class GroupFunctionServiceImpl implements GroupFunctionService{

    @Resource
    private BaseDao baseDao;

    /**
     * 群召唤
     * @param groupId
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> groupCall(String groupId, String userId,String content) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        //检查参数是否为空
        if(groupId==null || userId==null || "".equals(groupId) || "".equals(userId)){
            throw new BaseBusinessException(GroupError.PARAM_IS_NULL);
        }
        if(content==null){
            content="";
        }
        //检查是否有这个群
        DBObject groupObject = GroupUtil.checkIfHavaGroup(groupId);
        if(groupObject==null){
            throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
        }
        //检查是否有userId这个用户
        DBObject userObject = AuraUserUtil.getDbObjectByUserId(userId);
        if(userObject==null){
            throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
        }
        //检查是否是群成员
   /*     if(GroupUtil.checkIfIsGroupMember(groupId,userId)==null){
            throw new BaseBusinessException(GroupError.NOT_GROUPMEMBER);
        }*/
        boolean flag = true;
        //做记录
        BasicDBObject doc = new BasicDBObject();
        doc.put("userId",userId);
        //扣流量石的数量
        int num = Integer.parseInt(SetNewUtil.getGlobalPro(58));
        if(num<=0){
            throw new BaseBusinessException(GroupError.SHOULD_GREATER_THAN_0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT);
        doc.put("createTime",sdf.format(new Date()));
        doc.put("countent",0-num);
        doc.put("seconds",0);
        doc.put("message","群召唤扣除");
        String desciption = "普通成员发群召唤扣除"+num+"颗流量石";
        //检查是否是群主或者伪群主
        if(GroupUtil.checkIfMasterOrShamMaster(groupId,userId)!=null){
            desciption = "群主免流量石发召唤";
            flag = false;
        }
        //钻卡等级
        int drLevel = AuraUserUtil.getUserDiamondLevel(userId);
        if (drLevel==4){
            desciption = "黑钻免流量石发召唤";
            flag = false;
        }
        if(flag==true){
            //若没有特权则向PHP请求扣流量石
            //设置给PHP的请求包体
            MD5Util md5 = new MD5Util();
            String hash = md5.getMD5("username"+userObject.get("mobile") +"num"+ num + "action"+"consumells"+"appkey"+"X7J28D9CK3"+"ysltllb");
            Map<String,Object> mapForPHP = new HashMap<>();
            mapForPHP.put("username",userObject.get("mobile"));
            mapForPHP.put("num",num);
            mapForPHP.put("action", "consumells");
            mapForPHP.put("appkey", "X7J28D9CK3");
            mapForPHP.put("hash", hash);
            String jsonStr = HttpUtil.post(SystemConstant.PHP_API_URL, mapForPHP);
            JSONObject jsonObject2 = JSONObject.parseObject(jsonStr);
            int resultCode = Integer.parseInt(jsonObject2.getString("status"));
            if(resultCode==0){
                resultMap.put("result","0");
                resultMap.put("message","php返回状态为0");
                return resultMap;
            }
            if (resultCode==2){
                resultMap.put("result","2");
                String dataStr = jsonObject2.getString("data");
                JSONObject jsonObject3 = JSONObject.parseObject(dataStr);
                String reason = jsonObject3.getString("reason");
                resultMap.put("message",reason);
                return resultMap;
            }
            if(resultCode!=1){
                throw new BaseBusinessException(GroupError.PHP_NOT_SUCCESS);
            }
        }
        //设置容联请求包体
        Map<String,Object> mapForRL = new HashMap<>();
        mapForRL.put("pushType",2);
        mapForRL.put("appId",SystemConstant.DOULAO_APP_ID);
        mapForRL.put("sender",userId);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(groupId);
        mapForRL.put("receiver",arrayList);
        mapForRL.put("msgType",1);
        //msgContent
        Map<String,Object> map = new HashMap<>();
        map.put("msgType","9998");
        map.put("msgName","群召唤");
        map.put("msg",content);
        map.put("bizKey","\"\"");
        mapForRL.put("msgContent", JSON.toJSONString(map));
        //msgDomain
        String msgDomain = MsgUtil.getMsgDomain(userObject,groupObject);
        mapForRL.put("msgDomain",msgDomain);
        //向容联发起推送群消息的请求
        String result = HttpUtil.pushGroupMsg(mapForRL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if(!"000000".equals(jsonObject.getString("statusCode"))){
            resultMap.put("result",999);
            resultMap.put("message","容联请求成功，但返回代码不是000000");
            resultMap.put("statusCode",jsonObject.getString("statusCode"));
            return resultMap;
        }
        //存扣除流量石记录
        if(flag==true){
            DBCollection collection = DetaDiv.getCollection("TrafficMoney");
            collection.insert(doc);
        }
        resultMap.put("result","1");
        resultMap.put("message",desciption);
        return resultMap;
    }
}
