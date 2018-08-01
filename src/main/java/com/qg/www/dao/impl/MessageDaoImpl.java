package com.qg.www.dao.impl;

import com.qg.www.beans.Message;
import com.qg.www.dao.MessageDao;
import com.qg.www.utils.DbPoolConnectionUtil;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return false;
    }

    /**
     * 清空消息动态
     *
     * @return 是否操作成功
     */
    @Override
    public boolean cleanMessage() {
        return false;
    }
}
