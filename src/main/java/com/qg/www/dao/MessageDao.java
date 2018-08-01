package com.qg.www.dao;

import com.qg.www.beans.Message;

import java.util.List;

/**
 * @author net
 * @version 1.5
 * 消息的dao接口；
 */
public interface MessageDao {
    /**
     * 得到某人的文件动态
     *
     * @return 动态列表
     */
    List<Message> listMessages();

    /**
     * @param time       动作发生的时间
     * @param operatorId 被操作人的ID
     * @param userId     操作人的ID
     * @param content    动态内容
     * @param root       根目录
     * @param action     动作
     * @return 是否添加成功
     */
    boolean addMessage(String time, int operatorId, int userId, String content, String root, String action);

    /**
     * 清空消息动态
     *
     * @return 是否操作成功
     */
    boolean cleanMessage();
}
