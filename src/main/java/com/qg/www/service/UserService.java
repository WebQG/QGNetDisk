package com.qg.www.service;

import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.User;

import java.util.List;
import java.util.Map;

/**
 * @author net
 * @version 1.0
 * 用户dao接口
 */
public interface UserService {
    /**
     * 注册
     *
     * @param email          邮箱
     * @param password       密码
     * @param nickName       昵称
     * @param unSafePassword 未加密的密码
     * @param verifyCode     验证码；
     * @return 封装数据；
     */
    DataPack register(String email, String password, String nickName, String unSafePassword, Map<String, String> map, String verifyCode);

    /**
     * 登录方法
     *
     * @param user 用户；
     * @return 包装数据
     */
    DataPack login(User user);

    /**
     * 修改昵称
     *
     * @param newNickName 昵称
     * @param userId      用户ID
     * @return 新用户
     */
    User modifyNickName(String newNickName, int userId);

    /**
     * 修改密码
     *
     * @param password 新密码
     * @param userId   用户ID
     * @return 新用户
     */
    User modifyPassWord(String password, int userId);

    /**
     * 修改权限
     * @param data 数据源；
     * @return 是否成功
     */
    DataPack modifyStatus(Data data);

    /**
     * 查询用户
     *
     * @return
     */
    List<User> queryAllUser();

    /**
     * 返回重置密码
     *
     * @param email
     * @param password
     * @return
     */
    boolean resetPassword(String email, String password);

    /**
     * 通过用户ID找到用户并且返回
     *
     * @param userId 用户ID
     * @return 用户
     */
    User getUserByUserId(int userId);

    /**
     * 发送邮件
     *
     * @param email      邮箱信息；
     * @param verifyCode 注册码
     * @param isRegister 是否注册
     * @return 封装数据；
     */
    DataPack sendMail(String email, String verifyCode, String isRegister);

    /**
     * 判断邮箱验证码是否匹配；
     *
     * @param data 数据
     * @param map  全局存储器
     * @return 包装数据
     */
    DataPack validateMail(Data data, Map<String, String> map);

}
