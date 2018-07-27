package com.qg.www.dao;

import com.qg.www.beans.NetFile;

import java.util.List;

/**
 * 文件类的dao层接口
 */
public interface FileDao {
    /**
     * 查询所有文件；
     *
     * @return 文件列表
     */
    List<NetFile> listFile();

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


}
