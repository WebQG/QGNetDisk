package com.qg.www.service;

import com.qg.www.beans.DataPack;

/**
 * @author net
 * @version 1.5
 * 消息dao层接口；
 */
public interface MessageService {
    /**
     * 得到某人的文件动态
     *
     * @return 动态列表
     */
    DataPack listMessages();

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

    /**
     * 根据用户ID实现轮询查找用户的最新信息，且为被超级管理员删除
     * @param userID
     * @return
     */
    DataPack reportMessage(int userID);
}
