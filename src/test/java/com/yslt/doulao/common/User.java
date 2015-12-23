package com.yslt.doulao.common;

import com.google.code.morphia.annotations.Entity;
import common.util.pojo.BaseDomain;

/**
 * @Description: User
 * @anthor: shi_lin
 * @CreateTime: 2015-12-04
 */
@Entity(value = "User", noClassnameStored = true)
public class User extends BaseDomain {

    private String username;
    private String password;
    private Integer age;
    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
