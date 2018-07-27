package com.qg.www.beans;

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
    private int userId;
    /**
     * 用户邮箱也是用户名
     */
    private String email;
    /**
     * 用户登录密码
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户权限字段
     */
    private int status;
    /**
     * 用户名
     */
    private String verifycode;
}
