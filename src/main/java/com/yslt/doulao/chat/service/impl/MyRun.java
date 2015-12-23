package com.yslt.doulao.chat.service.impl;

import java.util.List;

/**
 * @Description: com.yslt.doulao.chat.service.impl
 * @author: li_bo
 * @CreateTime: 2015-12-17
 */
public class MyRun implements Runnable{

    private String groupId;
    private List<String> userIdList;
    private String type;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public MyRun(String groupId,List<String> userIdList,String type){
        this.groupId = groupId;
        this.userIdList = userIdList;
        this.type = type;
    }

    @Override
    public void run() {
            System.out.println("进入发送群提示线程");
            GroupChatServiceImpl gcsi = new GroupChatServiceImpl();
            gcsi.groupRemind(groupId,userIdList,type);
    }
}
