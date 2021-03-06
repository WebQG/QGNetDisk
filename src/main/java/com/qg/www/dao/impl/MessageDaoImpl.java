package com.qg.www.dao.impl;

import com.qg.www.beans.Message;
import com.qg.www.dao.MessageDao;
import com.qg.www.utils.DbPoolConnectionUtil;
import com.qg.www.utils.SqlCloseUtil;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    /**
     * 得到某人的文件动态
     *
     * @return 动态列表
     */
    @Override
    public List<Message> listMessages() {
        List<Message> messageList=new LinkedList<>();
        try {
            connection =  DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("SELECT * from message;");
            rs=preparedStatement.executeQuery();
            while(rs.next()){
                Message message=new Message();
                message.setContent(rs.getString("content"));
                message.setOperatorId(rs.getInt("operator_id"));
                message.setUserId(rs.getInt("user_id"));
                message.setTime(rs.getString("time"));
                message.setAction(rs.getString("action"));
                message.setRootPath(rs.getString("root"));
                messageList.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return messageList;

    }

    /**
     * @param time       动作发生的时间
     * @param operatorId 被操作人的ID
     * @param userId     操作人的ID
     * @param content    动态内容
     * @param root       根目录
     * @param action     动作
     * @return 是否添加成功
     */
    @Override
    public boolean addMessage(String time, int operatorId, int userId, String content, String root, String action) {
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO message (time,operator_id," +
                    "user_id, content, root, action) VALUE (?,?,?,?,?,?)");
            preparedStatement.setString(1,time);
            preparedStatement.setInt(2,operatorId);
            preparedStatement.setInt(3,userId);
            preparedStatement.setString(4,content);
            preparedStatement.setString(5,root);
            preparedStatement.setString(6,action);
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
     * 清空消息动态
     *
     * @return 是否操作成功
     */
    @Override
    public boolean cleanMessage() {
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM message");
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return false;
    }
}
