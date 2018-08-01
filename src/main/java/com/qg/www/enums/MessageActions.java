package com.qg.www.enums;

/**
 * 消息动作常量类；
 *
 * @author linxu
 * @version 1.5
 */
public enum MessageActions {
    /**
     * 上传文件动作；
     */
    UPLOAD_FILE("upload"),
    /**
     * 删除文件动作；
     */
    DELETE_FILE("delete"),
    /**
     * 重命名文件动作；
     */
    RENAME_FILE("rename"),
    /**
     * 提升成员为管理员动作；
     */
    UPGRADE("upgrade"),
    /**
     * 撤销管理员动作；
     */
    DOWNGRADE("downgrade");


    private String action;
    MessageActions(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
