package com.qg.www.service;

import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;

import java.util.List;

public interface FileService {

    /**
     *
     * 得到该文件或文件夹创建人的权限
     *
     * @param fileId 文件或文件夹的ID
     * @return 文件或文件夹创建人的ID
     */
    User getUserStatusByFileId(int fileId);

    /**
     *
     * 查询所有文件
     *
     * @return 经过排序的所有文件列表
     */
    List<NetFile> listAllFile();

    /**
     * 添加文件或者文件夹
     *
     * @param fileName   文件名
     * @param userName   用户名
     * @param userId     用户ID
     * @param fatherId   父目录ID
     * @param realPath   路径
     * @param modifyTime 最后修改时间
     * @param fileSize   文件大小
     * @return 是否成功添加；
     */
    boolean addFile(String fileName, String userName, int userId, int fatherId, String realPath, long modifyTime, long fileSize);

    /**
     *
     * 得到当前文件目录下的文件列表
     *
     * @param fileId 当前文件目录的ID
     * @return 当前文件目录下的所有文件列表
     */
    List<NetFile> listFile(int fileId);

    /**
     *
     * 删除文件或文件夹
     *
     * @param fileId 文件或文件夹的ID
     * @return 是否删除成功
     */
    boolean deleteFile(int fileId);


}