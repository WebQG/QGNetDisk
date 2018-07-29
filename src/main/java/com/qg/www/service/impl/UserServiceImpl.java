package com.qg.www.service.impl;

import com.qg.www.beans.User;
import com.qg.www.dao.impl.UserDaoImpl;
import com.qg.www.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author net
 * @version 1.0
 * 用户业务实现层；
 */
public class UserServiceImpl implements UserService {
    @Override
    public User register(String email, String password, String nickName,String unSafePassword) {
        if (email != null && password != null && nickName != null) {
            UserDaoImpl userDao = new UserDaoImpl();
            //存在返回空；
            if (userDao.isExist(email)) {
                return null;
            } else {
                //返回一个注册成功后的用户
                return userDao.addUser(email, password, nickName);
            }
        }
        return null;
    }

    @Override
    public User login(String email, String password) {
        if (email != null && password != null) {
            UserDaoImpl userDao = new UserDaoImpl();
            return userDao.login(email, password);
        } else {
            return null;
        }
    }

    @Override
    public User modifyNickName(String newNickName, int userId) {
        if (newNickName != null) {
            UserDaoImpl userDao = new UserDaoImpl();
            if (userDao.modifyNickName(userId, newNickName)) {
                return userDao.queryUser(userId);
            }
        }
        return null;
    }

    @Override
    public User modifyPassWord(String password, int userId) {
        if (password != null) {
            UserDaoImpl userDao = new UserDaoImpl();
            userDao.modifyPassWord(userId, password);
            return userDao.queryUser(userId);
        }
        return null;
    }

    @Override
    public boolean modifyStatus(int status, int userIdBy) {
        UserDaoImpl userDao = new UserDaoImpl();
        if (userDao.modifyStatus(status, userIdBy)) {
            return true;
        }
        return false;
    }

    @Override
    public List<User> queryAllUser() {
        UserDaoImpl userDao = new UserDaoImpl();
        List<User> userList = new ArrayList<>();
        userList.addAll(userDao.queryAllUser());
        return userList;
    }

    @Override
    public boolean resetPassword(String email, String password) {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.resetPassword(email, password);
    }
}
