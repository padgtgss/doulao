package com.yslt.doulao.chat.entity;

/**
 * @Description: 容联邀请时的请求包体对象（在InviteRequestBody里）
 * @author: li_bo
 * @CreateTime: 2015-11-30
 */
public class MembersRequestBody {
    private String member;

    public MembersRequestBody(String memberStr){
        this.member = memberStr;
    }

    public String getMemberStr() {
        return member;
    }

    public void setMemberStr(String memberStr) {
        this.member = memberStr;
    }
}
