package com.yslt.doulao.user.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yslt.doulao.user.service.AboutUserService;
import common.entity.BaseBusinessException;
import common.util.AuraUserUtil;
import common.util.RegexUtil;
import common.util.db.DetaDiv;
import common.util.db.MonDB;
import common.var.exception.AboutUserError;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: com.yslt.doulao.user.service.impl
 * @author: li_bo
 * @CreateTime: 2015-12-03
 */
@Service
public class AboutUserServiceImpl implements AboutUserService{

    /**
     * 指定的手机号验证密码
     * @param mobile 手机号
     * @param password 密码
     * @return
     */
    @Override
    public Map<String,Object> validatePwd(String mobile, String password) {
        //检查参数是否为空
        if (mobile==null&&password==null){
            throw new BaseBusinessException(AboutUserError.PARAM_IS_NULL);
        }
        //检查手机号格式
        if(RegexUtil.regexMobile(mobile)==false){
            throw new BaseBusinessException(AboutUserError.REGEX_MOBILE_ERROR);
        }
        DBObject doc = AuraUserUtil.getDbObjectByMobile(mobile);
        if(doc==null){
            throw new BaseBusinessException(AboutUserError.CANNOT_FIND_DATA);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        if(doc.get("password").toString().equals(password)){
            map.put("result",1);
            map.put("message","密码正确");
        }else{
            map.put("result",0);
            map.put("message","密码不正确");
        }
        return map;
    }

    @Override
    public Map<String,Object> getValidateCode(String oldMobile, String newMobile, String validateCode,String mei,String UUID) {
        //要转成jsonObject的map
        Map<String,Object> map = new HashMap<String,Object>();
        //检查参数是否为空
        if(oldMobile==null && newMobile==null && validateCode==null){
            throw new BaseBusinessException(AboutUserError.PARAM_IS_NULL);
        }
        //检查旧手机号和新手机号的格式
        if(RegexUtil.regexMobile(oldMobile)==false || RegexUtil.regexMobile(newMobile)==false){
            throw new BaseBusinessException(AboutUserError.REGEX_MOBILE_ERROR);
        }
        //检测用户
        DBObject doc = AuraUserUtil.getDbObjectByMobile(oldMobile);
        if(doc==null){
            throw new BaseBusinessException(AboutUserError.CANNOT_FIND_DATA);
        }
        //若数据库已有新手机号，就不允许通过
        DBObject docByMoble = AuraUserUtil.getDbObjectByMobile(newMobile);
        if(docByMoble!=null){
            map.put("result", 3);
            map.put("message","已存在要求绑定的手机");
            return map;
        }
        //拿出用户的userId
        String userId = doc.get("_id").toString();
        if(userId==null){
            throw new BaseBusinessException(AboutUserError.CANNOT_FIND_DATA);
        }
        //取出数据库的验证码
        DBCollection t_mobileVerification = DetaDiv.getCollection("t_mobileVerification");
        BasicDBObject where3 = new BasicDBObject();
        where3.put("mobile",newMobile);
        DBObject dbObject = t_mobileVerification.findOne(where3);
        if(dbObject==null){
            map.put("result",2);
            map.put("message","未生成过验证码");
            return map;
        }
        String nowCode = dbObject.get("validateCode").toString();
        if(nowCode==null || "".equals(nowCode)){
            map.put("result",2);
            map.put("message","验证码已失效");
        }else{
            if(nowCode.equals(validateCode)){
                //更换手机绑定
                MonDB monDB = new MonDB();
                BasicDBObject doc2 = new BasicDBObject();
                if(mei!=null&&!"".equals(mei)){
                    doc2.put("mei",mei);
                }
                if(UUID != null && !"".equals(UUID)){
                    doc2.put("uuid",UUID);
                }
                doc2.put("mei","");
                doc2.put("uuid","");
                doc2.put("mobile",newMobile);
                monDB.update("AuraUser",new BasicDBObject("mobile",oldMobile),doc2);
                //去UUIDTable表里面清除掉userId的数据
                DBCollection collection = DetaDiv.getCollection("UUIDTable");
                BasicDBObject where = new BasicDBObject();
                where.put("userId",userId);
                collection.remove(where);
                //在UUIDTable表里面加入新的mei或者UUId
/*                BasicDBObject doc3 = new BasicDBObject();
                doc3.put("userId",userId);
                doc3.put("isUpdate",true);
                if(mei!=null&!"".equals(mei)){
                    doc3.put("mei",mei);
                }
                if(UUID!=null&&!"".equals(UUID)){
                    doc3.put("uuId",UUID);
                }
                doc3.put("mei","");
                doc3.put("uuId","");
                collection.update(where,doc3,true,false);*/
                //返回信息
                map.put("result", 1);
                map.put("message","更换手机绑定成功");
            }else{
                map.put("result",0);
                map.put("message","验证码输入错误");
            }
        }
        return map;
    }
}
