package com.qg.www.dao;

import com.qg.www.beans.NetFile;

import java.util.List;

/**
 * 文件类的dao层接口
 * @author net
 * @version 1.2
 */
public interface FileDao {
    /**
     * 模糊搜索文件；
     * @param fileID 文件的ID；
     * @param keyWord 关键字；
     * @return 文件列表；
     */
    List<NetFile> searchFile(int fileID,String keyWord);
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
     *
     * 通过fileId找到创建该文件的用户ID
     *
     * @param fileId 文件或文件夹ID
     * @return 创建其的用户ID
     */
    int getUserId(int fileId);

    /**
     * 通过文件ID，找出父目录ID；
     * @param fileId
     * @return
     */
    int getDiretoryByFileId(int fileId);

    /**
     * 重命名文件
     *
     * @param fileId      文件ID
     * @param newFileName  新的文件名
     * @return 是否成功修改
     */
    boolean modifyFileName(int fileId, String newFileName);
    /**
     *
     * 用户下载时使文件下载量加1
     *
     * @param realPath 文件的绝对路径
     * @return 是否增加成功
     */
    boolean updateDownloadTimes(String realPath);

    /**
     * 排序浏览文件；
     * @param fileId 父目录ID
     * @param type 排序方式；
     * @return 文件列表；
     */
     List<NetFile >listSortedFile( int fileId, String type);





}
