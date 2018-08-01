package com.qg.www.beans;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author linxu
 * @version 1.5
 * 动态消息实体类；
 */
@Setter
@Getter
public class Message {
    /**
     * 消息的编号；
     */
    @SerializedName("messageid")
    private int messageId;
    /**
     * 消息的发生时间；
     */
    @SerializedName("time")
    private String time;
    /**
     * 消息的内容；
     */
    @SerializedName("content")
    private String content;
    /**
     * 操作人ID
     */
    @SerializedName("operatorid")
    private int operatorId;
    /**
     *用户ID；
     */
    @SerializedName("userid")
    private int userId;
    /**
     * 动作名称；
     */
    @SerializedName("action")
    private String action;
    /**
     * 根组别名称；
     */
    @SerializedName("rootpath")
    private String rootPath;
}

