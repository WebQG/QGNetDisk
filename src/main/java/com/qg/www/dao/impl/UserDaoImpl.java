package com.qg.www.dao.impl;

import com.qg.www.enums.UserStatus;
import com.qg.www.beans.User;
import com.qg.www.dao.UserDao;
import com.qg.www.utils.DbPoolConnection;
import com.qg.www.utils.SqlCloseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author net
 * @version 1.0
 * 用户dao层的实现
 */
public class UserDaoImpl implements UserDao {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    boolean flag=false;

    /**
     * 注册账号；里面会包含isExist判断是否已经注册
     *
     * @param email    邮箱
     * @param password 密码
     * @param nickName 昵称
     * @return 成功注册的用户或者是NULL
     */
    @Override
    public User addUser(String email, String password, String nickName) {
        try {
            connection = DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("INSERT INTO user (email,password,nickname,status)VALUES (?,?,?,?);");
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,nickName);
            preparedStatement.setInt(4,Integer.parseInt(UserStatus.ORDINARY_USER.getUserStatus()));
            //实现注册的过程；
            preparedStatement.execute();
        } catch (SQLException e) {
            //TODO 数据库异常后期需要的异常处理，暂时不弄
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        //返回注册成功后登录的用户；
        return  login(email,password);
    }

    /**
     * 判断该邮箱是否已经注册
     *
     * @param email 邮箱
     * @return 已经注册
     */
    @Override
    public boolean isExist(String email) {
        try {
            connection=DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("SELECT user_id FROM user where email=?;");
            preparedStatement.setString(1,email);
            rs=preparedStatement.executeQuery();
            while(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            //TODO 数据库异常后期需要的异常处理，暂时不弄
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return false;
    }

    /**
     * 登录方法，里面会判断是否存在，存在才判断账号密码是否匹配，成功则返回用户对象
     *
     * @param email    邮箱
     * @param password 密码
     * @return 登录用户或者NULL
     */
    @Override
    public User login(String email, String password) {
        User user = null;
        try {
            connection=DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("SELECT * FROM user WHERE email=?;");
            preparedStatement.setString(1,email);
            rs=preparedStatement.executeQuery();
            while(rs.next()){
                //判断密码是否匹配；
                if(password.equals(rs.getString("password"))) {
                    user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    String nickName=rs.getString("nickname");
                    int userID=rs.getInt("user_id");
                    int status=rs.getInt("status");
                    System.out.println(userID+""+status);
                    user.setNickName(nickName);
                    user.setStatus(status);
                    user.setUserId(userID);
                    System.out.println(user.getUserId());
                }
            }
        } catch (SQLException e) {
            //TODO 数据库异常后期需要的异常处理，暂时不弄
            e.printStackTrace();
        } finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return user;
    }

    /**
     * 查找返回所有用户集合用于用户列表展示
     *
     * @return 用户集合
     */
    @Override
    public List<User> queryAllUser() {
        List<User> userList=new ArrayList<>();
        try {
            connection=DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("SELECT * FROM user;");
            rs=preparedStatement.executeQuery();
            while (rs.next()){
                User user=new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setNickName(rs.getString("nickname"));
                user.setStatus(rs.getInt("status"));
                user.setUserId(rs.getInt("user_id"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return userList;
    }

    /**
     * 用户修改密码
     *
     * @param userId   当前登录的用户的ID
     * @param password 新密码
     * @return 修改密码是否成功
     */
    @Override
    public boolean modifyPassWord(int userId, String password) {
        try {
            connection=DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("UPDATE user SET password=? WHERE user_id=?;");
            preparedStatement.setString(1,password);
            preparedStatement.setInt(2,userId);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return false;
    }

    /**
     * 修改自己的昵称
     *
     * @param userId      当前登录用户ID
     * @param newNickName 新的昵称
     * @return 返回修改成功的用户
     */
    @Override
    public boolean modifyNickName(int userId, String newNickName) {
        try {
            connection=DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("UPDATE user SET nickname=? WHERE user_id=?;");
            preparedStatement.setString(1,newNickName);
            preparedStatement.setInt(2,userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            //TODO 捕获更新错误
            e.printStackTrace();
        } finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return false;
    }

    /**
     * @param status   权限
     * @param userIdBy 被操作人的ID
     * @return 修改成功后的用户
     */
    @Override
    public boolean modifyStatus(int status, int userIdBy) {
        try {
            connection=DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("UPDATE user SET status=? WHERE user_id=?;");
            preparedStatement.setInt(1,status);
            preparedStatement.setInt(2,userIdBy);
            flag = preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return flag;
    }
    /**
     *
     * @param userId 当前用户的ID
     * @return 当前用户信息
     */
    @Override
    public User queryUser(int userId) {
        User user=new User();
        try {
            connection = DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE user_id =?;");
            preparedStatement.setInt(1,userId);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setNickName(rs.getString("nickname"));
                user.setStatus(rs.getInt("status"));
                user.setUserId(rs.getInt("user_id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return null;
    }

    /**
     * 重置密码
     *
     * @param email    邮箱
     * @param password 密码
     * @return 重置密码后的用户
     */
    @Override
    public boolean resetPassword(String email, String password) {
        try {
            connection=DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("UPDATE user set password=? where email=?;");
            preparedStatement.setString(1,password);
            preparedStatement.setString(2,email);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //TODO 后期使用日志
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return false;
    }
}
