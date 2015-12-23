package com.yslt.doulao.chat.entity;

/**
 * @Description: com.yslt.doulao.chat.entity
 * @author: li_bo
 * @CreateTime: 2015-11-27
 */
public class SubAccounts {

    private String userId;  //用户id，也对应到群主
    private String subAccountSid;   //子账户id
    private String subToken;        //子账户授权令牌
    private String dateCreated;     //创建时间
    private String voipAccount;
    private String voipPwd;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubAccountSid() {
        return subAccountSid;
    }

    public void setSubAccountSid(String subAccountSid) {
        this.subAccountSid = subAccountSid;
    }

    public String getSubToken() {
        return subToken;
    }

    public void setSubToken(String subToken) {
        this.subToken = subToken;
    }

    public String getVoipAccount() {
        return voipAccount;
    }

    public void setVoipAccount(String voipAccount) {
        this.voipAccount = voipAccount;
    }

    public String getVoipPwd() {
        return voipPwd;
    }

    public void setVoipPwd(String voipPwd) {
        this.voipPwd = voipPwd;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
