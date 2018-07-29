package com.qg.www.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用以包装JSON中的data;
 *所有字段统一为小写，用于区别。
 * @author linxu
 * @version 1.0
 */
@Setter
@Getter
public class Data {
    @Expose
    private String email;

    @Expose
    private String password;

    @SerializedName("nickname")
    @Expose
    private String nickName;
    @Expose
    @SerializedName("newnickname")
    private String newNickName;
    @Expose
    @SerializedName("verifycode")
    private String verifyCode;
    @Expose
    @SerializedName("users")
    private List<User> users;
    @Expose
    @SerializedName("filepath")
    private String filePath;

    @SerializedName("fileid")
    private int fileId;

    @SerializedName("userid")
    private int userId;

    private int status;
    @Expose
    private User user;
    @Expose
    private List<NetFile> files;

    public Data() {
    }

    /**
     * 初始化用户数据的方法；
     *
     * @param user 用户
     */
    public Data(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickName = user.getNickName();
        this.status = user.getStatus();
        this.userId = user.getUserId();
    }
}
