package com.qg.www.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.annotation.WebServlet;

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
    @Expose
    @SerializedName("fileid")
    private int fileId;

    /**
     * 文件名
     */
    @Expose
    @SerializedName("filename")
    private String fileName;

    /**
     * 父节点ID
     */
    @Expose
    @SerializedName("fatherid")
    private int fatherId;
    /**
     * 用户ID
     */
    @Expose
    @SerializedName("userid")
    private int userId;

    /**
     * 用户名
     */
    @Expose
    @SerializedName("username")
    private String userName;

    /**
     * 修改时间
     */
    @Expose
    @SerializedName("modifytime")
    private String modifyTime;

    /**
     * 下载次数统计
     */
    @Expose
    @SerializedName("downloadtimes")
    private int downloadTimes;

    /**
     * 文件大小
     */
    @Expose
    @SerializedName("filesize")
    private long fileSize;

    /**
     * 文件的路径
     */
    @Expose
    @SerializedName("realpath")
    private String realPath;
}
