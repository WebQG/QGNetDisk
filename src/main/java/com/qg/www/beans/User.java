package com.qg.www.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author net
 * @version 1.0
 */
@Getter
@Setter
public class User implements Serializable {
    /**
     * 用户ID
     */
    @Expose
    @SerializedName("userid")
    private int userId;
    /**
     * 用户邮箱也是用户名
     */
    @Expose
    @SerializedName("email")
    private String email;
    /**
     * 用户登录密码
     */
    @Expose
    @SerializedName("password")
    private String password;
    /**
     * 用户昵称
     */
    @Expose
    @SerializedName("nickname")
    private String nickName;
    /**
     * 用户权限字段
     */
    @Expose
    @SerializedName("status")
    private int status;

}
