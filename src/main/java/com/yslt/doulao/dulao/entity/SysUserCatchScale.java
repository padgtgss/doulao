package com.yslt.doulao.dulao.entity;

/**
 * @Description: 抽奖关系系数
 * @anthor: shi_lin
 * @CreateTime: 2015-11-18
 */

public class SysUserCatchScale {
    private String id ;
    private String userLeave;
    private String scale;  //系数

    //============================getter and setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserLeave() {
        return userLeave;
    }

    public void setUserLeave(String userLeave) {
        this.userLeave = userLeave;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
