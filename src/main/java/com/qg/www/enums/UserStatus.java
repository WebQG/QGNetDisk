package com.qg.www.enums;
/**
 * @author net
 * @version 1.3
 * 用户常量枚举类；
 */
public enum UserStatus {

    /**
     * 超级管理员的status
     */
    SUPER_ADMIN(3),
    /**
     * 普通管理员的status
     */
    ORDINARY_ADMIN(2),
    /**
     * 普通用户的status
     */
    ORDINARY_USER(1);

    int userStatus;
    UserStatus(int userStatus){
        this.userStatus = userStatus;
    }
    public int getUserStatus(){
        return userStatus;
    }
}
