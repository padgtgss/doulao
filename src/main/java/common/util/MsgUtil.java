package common.util;

import com.alibaba.fastjson.JSON;
import com.mongodb.DBObject;
import common.entity.BaseBusinessException;
import common.var.constants.GroupConstant;
import common.var.constants.SystemConstant;
import common.var.exception.GroupError;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: common.util
 * @author: li_bo
 * @CreateTime: 2015-12-16
 */
public class MsgUtil {

    public static String getMsgDomain(DBObject userObject,DBObject groupObject){
        Map<String,Object> map = new HashMap<>();
        if(userObject==null){
            throw new BaseBusinessException(GroupError.USER_NOT_EXISTS);
        }
        if(groupObject==null){
            throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
        }
        map.put("chatting_id", GroupConstant.GROUPCALL_ID);
        //发送者id
        map.put("user_id",userObject.get("_id").toString());
        //发送者昵称
        map.put("user_nickname", ((DBObject) userObject.get("profile")).get("screenName").toString());
        //发送者性别
        int gender = 1;
        if(userObject.get("gender")!=null) {
            gender = Integer.parseInt(userObject.get("gender").toString());
        }else{
            gender = 1;
        }
        map.put("user_gender",gender);
        //发送者头像
        String imgPath = "";
        boolean flag1 = false;
        if(userObject.get("profile")==null){
            flag1 =true;
        }
        if(((DBObject)userObject.get("profile")).get("avatar")==null){
            flag1=true;
        }
        if(((DBObject)(((DBObject)userObject.get("profile")).get("avatar"))).get("small")==null){
            flag1=true;
        }
        if(flag1==false) {
            imgPath = ((DBObject) (((DBObject) userObject.get("profile")).get("avatar"))).get("small").toString();
        }else{
            imgPath = ((DBObject)(((DBObject)userObject.get("profile")).get("avatar"))).get("smallPath").toString();
        }
        if(imgPath==null ||"".equals(imgPath) || "-".equals(imgPath)){
            imgPath = ((DBObject)(((DBObject)userObject.get("profile")).get("avatar"))).get("smallPath").toString();
        }
        if(imgPath!=null){
            if(imgPath.startsWith("http")){
                imgPath = ((DBObject)(((DBObject)userObject.get("profile")).get("avatar"))).get("smallPath").toString();
            }
        }
        map.put("user_avatar",SystemConstant.SERVER_IMAGE_PATH+imgPath);
        //群id
        map.put("to_id",groupObject.get("_id").toString());
        //群名称
        map.put("to_nickname",groupObject.get("groupName").toString());
        //群头像
        map.put("to_avatar",SystemConstant.SERVER_IMAGE_PATH+groupObject.get("imgPath").toString());

        /*String dm = map.toString();
        dm = dm.replaceAll("\\[","\\{");
        dm = dm.replaceAll("\\]", "\\}");*/
        return JSON.toJSONString(map);
    }

    public static String getMsgDomainList(DBObject groupObject){
        Map<String,Object> map = new HashMap<>();
        if(groupObject==null){
            throw new BaseBusinessException(GroupError.GROUP_NOT_EXISTS);
        }
        map.put("chatting_id", GroupConstant.GROUPCALL_ID);
        //群id
        map.put("to_id",groupObject.get("_id").toString());
        //群名称
        map.put("to_nickname",groupObject.get("groupName").toString());
        //群头像
        map.put("to_avatar",SystemConstant.SERVER_IMAGE_PATH+groupObject.get("imgPath").toString());
        //头像
        String imgPath = "";
        boolean flag1 = false;
        /*String dm = map.toString();
        dm = dm.replaceAll("\\[","\\{");
        dm = dm.replaceAll("\\]", "\\}");*/
        return JSON.toJSONString(map);
    }
}
