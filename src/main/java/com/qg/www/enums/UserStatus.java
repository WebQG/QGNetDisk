package com.qg.www.enums;
public enum UserStatus {

    /**
     * 超级管理员的status
     */
    SUPER_ADMIN("3"),
    /**
     * 普通管理员的status
     */
    ORDINARY_ADMIN("2"),
    /**
     * 普通用户的status
     */
    ORDINARY_USER("1");

    String userStatus;
    UserStatus(String userStatus){
        this.userStatus = userStatus;
    }
    public String getUserStatus(){
        return userStatus;
    }
}
