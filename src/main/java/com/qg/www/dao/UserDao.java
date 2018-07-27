package com.qg.www.dao;

import com.qg.www.beans.User;

import java.util.List;
/**
 * @author net
 * @version 1.0
 * 用户dao接口
 */
public interface UserDao {
    /**
     * 注册账号；里面会包含isExist判断是否已经注册
     * @param email 邮箱
     * @param password 密码
     * @param nickName 昵称
     * @return 成功注册的用户或者是NULL
     */
    User addUser(String email, String password, String nickName) ;
    /**
     * 判断该邮箱是否已经注册
     * @param email 邮箱
     * @return 已经注册
     */
    boolean isExist(String email) ;
    /**
     * 登录方法，里面会判断是否存在，存在才判断账号密码是否匹配，成功则返回用户对象
     * @param email 邮箱
     * @param password 密码
     * @return 登录用户或者NULL
     */
    User login(String email,String password) ;
    /**
     * 查找返回所有用户集合用于用户列表展示
     * @return 用户集合
     */
    List<User> queryAllUser();
    /**
     * 用户修改密码
     * @param userId 当前登录的用户的ID
     * @param password 新密码
     * @return 修改密码是否成功
     */
    boolean modifyPassWord(int userId,String password);
    /**
     * 修改自己的昵称
     * @param userId 当前登录用户ID
     * @param newNickName 新的昵称
     * @return 返回修改成功的用户
     */
    boolean modifyNickName(int userId,String newNickName);
    /**
     *
     * @param status 权限
     * @param userIdBy  被操作人的ID
     * @return 修改成功后的用户
     */
    boolean modifyStatus(int status, int userIdBy);
    /**
     *
     * @param userId 当前用户的ID
     * @return 当前用户的基本信息
     */
    User queryUser(int userId);

    /**
     * 重置密码
     * @param email  邮箱
     * @param password 密码
     * @return 是否重置成功
     */
    boolean resetPassword(String email,String password);
}
