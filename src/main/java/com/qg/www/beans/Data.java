package com.qg.www.beans;

import lombok.Getter;
import lombok.Setter;

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
    private String email;
    private String password;
    private String nickname;
    private int userid;
    private int status;
    private String newnickname;
    private String verifycode;
    private List<User> userList;

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
