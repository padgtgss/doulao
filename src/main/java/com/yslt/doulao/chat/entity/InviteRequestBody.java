package com.yslt.doulao.chat.entity;

import java.util.List;

/**
 * @Description: 容联邀请时的请求包体对象
 * @author: li_bo
 * @CreateTime: 2015-11-30
 */
public class InviteRequestBody {

    private String groupId;
    private List<MembersRequestBody> members;
    private String confirm;
    private String declared;

    public List<MembersRequestBody> getMembers() {
        return members;
    }

    public void setMembers(List<MembersRequestBody> members) {
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getDeclared() {
        return declared;
    }

    public void setDeclared(String declared) {
        this.declared = declared;
    }

}
