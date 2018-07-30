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
     *模糊搜索文件列表；
     * @param fileId 文件ID
     * @param keyWord 关键字
     * @return 文件列表；
     */
    List<NetFile> searchFile(int fileId,String keyWord);

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
    boolean addFile(String fileName, String userName, int userId, int fatherId, String realPath, String modifyTime, long fileSize);

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

    /**
     * 通过文件ID查找父目录的ID；
     * @param fileId
     * @return
     */
    int getFatherId(int fileId);

    /**
     * 修改文件的名称，同时返回新的文件列表
     *
     * @param fileId      文件的ID
     * @param newFileName 新的文件名字
     * @return 新的文件列表
     */
    List<NetFile> renameFile(int fileId, String newFileName);

    /**
     *
     * 用户下载时使文件下载量加1
     *
     * @param realPath 文件的相对路径
     * @return 是否增加成功
     */
    boolean updateDownloadTimes(String realPath);

    /**
     * 按照类型进行目录下文件的排序；
     * @param fileId 目录ID
     * @param type 排序类型；
     * @return 文件列表
     */
    List<NetFile> listSortedFile(int fileId,String type);

}
