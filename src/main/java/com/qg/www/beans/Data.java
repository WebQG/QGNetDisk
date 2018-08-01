package com.qg.www.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用以包装JSON中的data;
 * 所有字段统一为小写，用于区别。
 *
 * @author linxu
 * @version 1.0
 */
@Setter
@Getter
public class Data {
    /**
     * 用户邮箱；
     */
    @Expose
    private String email;

    /**
     * 密码；
     */
    @Expose
    private String password;

    /**
     * 昵称
     */
    @SerializedName("nickname")
    @Expose
    private String nickName;

    /**
     * 新的昵称
     */
    @Expose
    @SerializedName("newnickname")
    private String newNickName;

    /**
     * 验证码
     */
    @Expose
    @SerializedName("verifycode")
    private String verifyCode;

    /**
     * 用户列表
     */
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

    @SerializedName("filename")
    private String fileName;

    @SerializedName("newfilename")
    private String newFileName;

    @SerializedName("operatorid")
    private int operatorID;

    @Expose
    private  List<Message> messages;

    private int status;

    @Expose
    private User user;

    @Expose
    private List<NetFile> files;

    @Expose
    private  List<Message> messages;

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
