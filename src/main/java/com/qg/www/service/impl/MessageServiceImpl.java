package com.qg.www.service.impl;

import com.qg.www.beans.Data;
import com.qg.www.beans.Message;
import com.qg.www.dao.impl.MessageDaoImpl;
import com.qg.www.service.MessageService;
import com.qg.www.beans.DataPack;

import java.util.List;

public class MessageServiceImpl implements MessageService {
    MessageDaoImpl messageDao = new MessageDaoImpl();

    /**
     * 得到某人的文件动态
     *
     * @return 动态列表
     */
    @Override
    public DataPack listMessages() {
        // 得到数据库中的message信息
        List<Message> messages = messageDao.listMessages();

        // 查询数据库得到操作人与被操作人昵称
        UserServiceImpl userService = new UserServiceImpl();
        for(Message message : messages){
            message.setOperatorName(userService.getUserByUserId(message.getOperatorId()).getNickName());
            if(message.getUserId() != 0){
                message.setUserName(userService.getUserByUserId(message.getUserId()).getNickName());
            }
        }

        // 得到当前日期
        Data data = new Data();
        data.setMessages(messages);
        // 打包
        DataPack dataPack = new DataPack();
        dataPack.setData(data);
        return dataPack;
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
        return messageDao.addMessage(time, operatorId,userId, content, root, action);
    }

    /**
     * 清空消息动态
     *
     * @return 是否操作成功
     */
    @Override
    public boolean cleanMessage() {
        return messageDao.cleanMessage();
    }

    /**
     * 根据用户ID实现轮询查找用户的最新信息，且为被超级管理员删除
     *
     * @param userID
     * @return
     */
    @Override
    public DataPack reportMessage(int userID) {
        return null;
    }
}
