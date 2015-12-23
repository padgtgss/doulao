package com.yslt.doulao.info.entity;

/**
 * @Description: User
 * @anthor: shi_lin
 * @CreateTime: 2015-11-20
 */

public class User {
    private String id;
    private String mobile;
    private String userLeave;
    private String experience;

    public User() {

    }

    public User(String id, String mobile, String userLeave, String experience) {
        this.id = id;
        this.mobile = mobile;
        this.userLeave = userLeave;
        this.experience = experience;
    }
    //=======================getter and setter=======================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserLeave() {
        return userLeave;
    }

    public void setUserLeave(String userLeave) {
        this.userLeave = userLeave;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
