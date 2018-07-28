package com.qg.www.beans;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户实体类
 *
 * @author net
 * @version 1.0
 */
@Getter
@Setter
public class User {
    /**
     * 用户ID
     */
    @Expose
    private int userId;
    /**
     * 用户邮箱也是用户名
     */
    @Expose private String email;
    /**
     * 用户登录密码
     */
    @Expose private String password;
    /**
     * 用户昵称
     */
    @Expose private String nickName;
    /**
     * 用户权限字段
     */
    @Expose private int status;
    /**
     * 用户名
     */
    @Expose private String verifycode;
}
