package com.deepinto.entity.mall;

import java.util.Date;

public class RegisterEntity {

    private String username;
    private String password;
    private Date ctime;
    private String phonet;
    private String phoneid;

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

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getPhonet() {
        return phonet;
    }

    public void setPhonet(String phonet) {
        this.phonet = phonet;
    }

    public String getPhoneid() {
        return phoneid;
    }

    public void setPhoneid(String phoneid) {
        this.phoneid = phoneid;
    }
}
