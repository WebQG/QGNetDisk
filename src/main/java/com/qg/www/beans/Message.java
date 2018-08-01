package com.qg.www.beans;

import com.google.gson.annotations.Expose;
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
    @Expose
    @SerializedName("time")
    private String time;
    /**
     * 消息的内容；
     */
    @Expose
    @SerializedName("content")
    private String content;
    /**
     * 操作人ID
     */
    @Expose
    @SerializedName("operatorid")
    private int operatorId;
    /**
     * 操作人昵称
     */
    @Expose
    @SerializedName("operatorname")
    private String operatorName;
    /**
     *用户ID；
     */
    @Expose
    @SerializedName("userid")
    private int userId;
    /**
     * 用户昵称
     */
    @Expose
    @SerializedName("username")
    private String userName;
    /**
     * 动作名称；
     */
    @Expose
    @SerializedName("action")
    private String action;
    /**
     * 根组别名称；
     */
    @Expose
    @SerializedName("rootpath")
    private String rootPath;
}

