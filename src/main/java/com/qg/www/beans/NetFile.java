package com.qg.www.beans;

import lombok.Getter;
import lombok.Setter;

/**
 * @author linxu
 * @version 1.0
 * 文件类；
 */
@Getter
@Setter
public class NetFile {
    /**
     * 文件ID
     */
    private int fileId;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 父节点ID
     */
    private int fatherId;
    /**
     * 用户ID
     */
    private int userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 修改时间
     */
    private long modifyTime;
    /**
     * 下载次数统计
     */
    private int downloadTimes;
    /**
     * 文件大小
     */
    private long fileSize;
}
