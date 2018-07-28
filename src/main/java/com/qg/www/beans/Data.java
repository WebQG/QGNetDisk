package com.qg.www.beans;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import sun.nio.ch.Net;

import java.io.File;
import java.util.List;

/**
 * 用以包装JSON中的data;
 *
 * @author linxu
 * @version 1.0
 */
@Setter
@Getter
public class Data {
    @Expose private String email;
    @Expose private String password;
    @Expose private String nickname;
    private int userid;
    private int status;
    @Expose private String newnickname;
    @Expose private String verifycode;
    @Expose private List<User> users;
    @Expose private List<NetFile> files;

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
        this.nickname = user.getNickName();
        this.status = user.getStatus();
        this.userid = user.getUserId();
    }
}
