package com.qg.www.service.impl;

import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.User;
import com.qg.www.dao.impl.FileDaoImpl;
import com.qg.www.dao.impl.UserDaoImpl;
import com.qg.www.enums.Status;
import com.qg.www.service.UserService;
import com.qg.www.utils.DigestUtil;
import com.qg.www.utils.RandomVerifyCodeUtil;
import com.qg.www.utils.SendMailUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author net
 * @version 1.0
 * 用户业务实现层；
 */
public class UserServiceImpl implements UserService {
    @Override
    public DataPack register(String email, String password, String nickName, String unSafePassword, Map<String, String> map, String verifyCode) {
        //创建数据打包器；
        DataPack dataPack = new DataPack();
        UserDaoImpl userDao = new UserDaoImpl();
        if (map == null || !verifyCode.equals(map.get(email))) {
            dataPack.setStatus(Status.VERIFYCODE_WROSE.getStatus());
            dataPack.setData(null);
        } else if (!userDao.isExist(email)) {
            //注册用户
            UserServiceImpl userService = new UserServiceImpl();
            User user = userDao.addUser(email, password, nickName);
            //邮箱是否存在；
            if (null != user) {
                //邮箱未注册；
                user.setPassword(unSafePassword);
                Data datas = new Data(user);
                dataPack.setStatus(Status.NORMAL.getStatus());
                dataPack.setData(datas);
            }
        } else {
            //邮箱已经被注册
            dataPack.setStatus(Status.EMIAL_ISREGISTER.getStatus());
            dataPack.setData(null);
        }
        return dataPack;
    }

    @Override
    public DataPack login(User user) {
        UserDaoImpl userDao = new UserDaoImpl();
        DataPack dataPack = new DataPack();
        Data data;
        //解析数据；
        String email = user.getEmail();
        //加密前的密码；
        String unSafePassword = user.getPassword();
        //对密码进行加密；
        String password = DigestUtil.md5(user.getPassword());
        //邮箱和密码不为空；
        if (email != null && password != null) {
            //邮箱存在；
            if (userDao.isExist(email)) {
                //返回登录结果
                User newUser = userDao.login(email, password);
                //判断登录状态；
                if (null == newUser) {
                    //密码不正确；
                    dataPack.setData(null);
                    dataPack.setStatus(Status.PASSWORD_WROSE.getStatus());
                    return dataPack;
                } else {
                    data = new Data(newUser);
                    data.setPassword(unSafePassword);
                    dataPack.setData(data);
                    dataPack.setStatus(Status.NORMAL.getStatus());
                    return dataPack;
                }
            } else {
                //邮箱不存在；
                dataPack.setStatus(Status.EMAIL_NOEXIST.getStatus());
                dataPack.setData(null);
                return dataPack;
            }
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

    /**
     * 通过用户ID找到用户并且返回
     *
     * @param userId 用户ID
     * @return 用户
     */
    @Override
    public User getUserByUserId(int userId) {
        if (userId > 0) {
            UserDaoImpl userDao = new UserDaoImpl();
            return userDao.queryUser(userId);
        }
        return null;
    }

    /**
     * 发送邮件
     *
     * @param email      邮箱信息；
     * @param verifyCode 验证码
     * @param isRegister
     * @return 封装数据；
     */
    @Override
    public DataPack sendMail(String email, String verifyCode, String isRegister) {
        //构造数据打包器；
        DataPack dataPack = new DataPack();
        //判断是否是注册用户；
        if ((null != email) && (null != isRegister)) {
            UserDaoImpl userDao = new UserDaoImpl();
            //邮箱未注册；
            if (!userDao.isExist(email)) {
                try {
                    //发送邮件；
                    SendMailUtil.sendMail(email, verifyCode);
                    dataPack.setStatus(Status.NORMAL.getStatus());
                    return dataPack;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //返回错误，不发送邮箱验证码；
                dataPack.setStatus(Status.EMIAL_ISREGISTER.getStatus());
                return dataPack;
            }

        } else {
            //则是忘记密码的重置；
            if (new UserDaoImpl().isExist(email)) {
                //存在邮箱，则发送验证码；
                try {
                    SendMailUtil.sendMail(email, verifyCode);
                    dataPack.setStatus(Status.NORMAL.getStatus());
                    return dataPack;
                } catch (Exception e) {
                    //TODO 以后可以采用日志在此捕捉邮件发送异常；
                }
            } else {
                //返回邮箱不存在；
                dataPack.setStatus(Status.EMAIL_NOEXIST.getStatus());
                return dataPack;
            }
        }
        return dataPack;
    }

    /**
     * 判断邮箱验证码是否匹配；
     *
     * @param data 数据
     * @param map  全局存储器
     * @return 包装数据
     */
    @Override
    public DataPack validateMail(Data data, Map<String, String> map) {
        //获取邮箱和验证码
        String email = data.getEmail();
        String verifyCode = data.getVerifyCode();
        //创建打包器；
        DataPack dataPack = new DataPack();
        //判断是否匹配；
        if (null != map && verifyCode.equals(map.get(email))) {
            dataPack.setStatus(Status.NORMAL.getStatus());
            map.remove(email);
            return dataPack;
        }else {
            dataPack.setStatus(Status.VERIFYCODE_WROSE.getStatus());
            return dataPack;
        }
    }
}
