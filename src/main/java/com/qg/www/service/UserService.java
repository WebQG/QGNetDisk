package com.qg.www.service;
import com.qg.www.beans.User;
import java.util.List;

/**
 * @author net
 * @version 1.0
 * 用户dao接口
 */
public interface UserService {
    /**
     * 注册
     * @param email
     * @param password
     * @param nickName
     * @param unSafePassword
     * @return
     */
    User register(String email, String password, String nickName,String unSafePassword);

    /**
     *
     * @param email
     * @param password
     * @return
     */
    User login(String email,String password);

    /**
     * 修改昵称
     * @param newNickName
     * @param userId
     * @return
     */
    User modifyNickName(String newNickName,int userId);

    /**
     * 修改密码
     * @param password
     * @param userId
     * @return
     */
    User modifyPassWord(String password,int userId);

    /**
     * 修改权限
     * @param status
     * @param userIdBy
     * @return
     */
    boolean modifyStatus(int status,int userIdBy);

    /**
     * 查询用户
     * @return
     */
    List<User> queryAllUser();

    /**
     * 返回重置密码
     * @param email
     * @param password
     * @return
     */
    boolean resetPassword(String email,String password);

}
