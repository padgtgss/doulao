package com.yslt.doulao.dulao.pojo;

import com.yslt.doulao.dulao.enums.OneCatchTypeEnum;
import com.yslt.doulao.dulao.enums.PrivilegeCardTypeEnum;

/**
 * @Description: OneCatchFromPHPPojo
 * @anthor: shi_lin
 * @CreateTime: 2015-11-19
 */

public class OneCatchFromPHPPojo {
    private OneCatchTypeEnum oneCatchTypeEnum;   //转盘抽奖类型
    private PrivilegeCardTypeEnum privilegeCardTypeEnum;  //特权卡类型
    private String value;                        //转盘抽奖所得值
    private String cash;                         //分享出去的现金值
    private String targetId;                    //现金详情ID
    private String nocash;                     // 是否满足整额体现
    private String shareregCashId;             //分享现金Id


    //===============================getter and setter==========================


    public OneCatchTypeEnum getOneCatchTypeEnum() {
        return oneCatchTypeEnum;
    }

    public void setOneCatchTypeEnum(OneCatchTypeEnum oneCatchTypeEnum) {
        this.oneCatchTypeEnum = oneCatchTypeEnum;
    }

    public PrivilegeCardTypeEnum getPrivilegeCardTypeEnum() {
        return privilegeCardTypeEnum;
    }

    public void setPrivilegeCardTypeEnum(PrivilegeCardTypeEnum privilegeCardTypeEnum) {
        this.privilegeCardTypeEnum = privilegeCardTypeEnum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getNocash() {
        return nocash;
    }

    public void setNocash(String nocash) {
        this.nocash = nocash;
    }

    public String getShareregCashId() {
        return shareregCashId;
    }

    public void setShareregCashId(String shareregCashId) {
        this.shareregCashId = shareregCashId;
    }
}
