package com.qg.www.dao.impl;

import com.qg.www.beans.Message;
import com.qg.www.dao.MessageDao;
import com.qg.www.enums.MessageActions;
import com.qg.www.utils.DbPoolConnectionUtil;
import com.qg.www.utils.SqlCloseUtil;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
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
            preparedStatement=connection.prepareStatement("SELECT *from message ;");
            rs=preparedStatement.executeQuery();
            while(rs.next()){
                Message message=new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setContent(rs.getString("content"));
                message.setOperatorId(rs.getInt("operatorid"));
                message.setUserId(rs.getInt("userid"));
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
    @Override
    public boolean addMessage(String time, int operatorId, int userId, String content, String root, String action) {
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO message  (time,operator_id," +
                    "user_id, content, root, action,status) VALUES (?,?,?,?,?,?,?) ;");
            preparedStatement.setString(1,time);
            preparedStatement.setInt(2,operatorId);
            preparedStatement.setInt(3,userId);
            preparedStatement.setString(4,content);
            preparedStatement.setString(5,root);
            preparedStatement.setString(6,action);
            preparedStatement.setInt(7,0);
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
            preparedStatement = connection.prepareStatement("DELETE FROM message ;");
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
     * 查询用户文件被超管删除的信息；
     *
     * @param userId 用户 ID
     * @return 消息列表；
     */
    @Override
    public List<Message> listUserMessage(int userId) {
        List<Message> messageList = new LinkedList<>();
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM message where user_id=?;");
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                //如果是管理员删除某人的文件，则获取数据；
                if ((rs.getInt("operator_id") > rs.getInt("user_id")) && MessageActions.DELETE_FILE.getAction().equals(rs.getString("action"))&&rs.getInt("status")==0) {
                    message.setMessageId(rs.getInt("message_id"));
                    message.setContent(rs.getString("content"));
                    message.setOperatorId(rs.getInt("operatorid"));
                    message.setUserId(rs.getInt("userid"));
                    message.setTime(rs.getString("time"));
                    message.setAction(rs.getString("action"));
                    message.setRootPath(rs.getString("root"));
                    messageList.add(message);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlCloseUtil.close(connection, preparedStatement, rs);
        }
        return messageList ;
    }

    /**
     * 改变消息状态；
     *
     * @param messageList 消息列表；
     * @return 是否成功小改。
     */
    @Override
    public boolean modifyMessageStatus(List<Message> messageList) {
        Iterator <Message>iterator=messageList.iterator();
        try {
                while (iterator.hasNext()) {
                    connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
                    preparedStatement = connection.prepareStatement("UPDATE message set status = ? where message_id=?;");
                    preparedStatement.setInt(1,1);
                    preparedStatement.setInt(2,iterator.next().getMessageId());
                    preparedStatement.executeUpdate();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return true;
    }
}
