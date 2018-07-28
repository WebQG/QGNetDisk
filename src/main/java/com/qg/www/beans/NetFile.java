package com.qg.www.beans;

import com.google.gson.annotations.Expose;
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
    @Expose private int fileId;
    /**
     * 文件名
     */
    @Expose private String fileName;
    /**
     * 父节点ID
     */
    @Expose private int fatherId;
    /**
     * 用户ID
     */
    @Expose private int userId;
    /**
     * 用户名
     */
    @Expose private String userName;
    /**
     * 修改时间
     */
    @Expose private String modifyTime;
    /**
     * 下载次数统计
     */
    @Expose private int downloadTimes;
    /**
     * 文件大小
     */
    @Expose private long fileSize;

    /**
     * 文件的相对路径
     */
    @Expose private String realPath;
}
