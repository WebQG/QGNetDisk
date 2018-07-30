package com.qg.www.service.impl;

import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.dao.impl.FileDaoImpl;
import com.qg.www.dao.impl.UserDaoImpl;
import com.qg.www.service.FileService;

import java.util.List;

public class FileServiceImpl implements FileService {
    /**
     * 得到该文件或文件夹创建人的权限
     *
     * @param fileId 文件或文件夹的ID
     * @return 文件或文件夹创建人的ID
     */
    @Override
    public User getUserStatusByFileId(int fileId) {
        int userId;
        FileDaoImpl fileDao = new FileDaoImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        // 得到文件创建者的ID
        userId = fileDao.getUserId(fileId);
        // 得到该用户的所有信息
        return userDao.queryUser(userId);
    }

    /**
     * 模糊搜索文件列表；
     *
     * @param fileId  父目录ID
     * @param keyWord 关键字
     * @return 文件列表；
     */
    @Override
    public List<NetFile> searchFile(int fileId, String keyWord) {
        //判断是否为空；
        if (fileId != 0 && null != keyWord) {
            //不为空执行查询；
            FileDaoImpl fileDao = new FileDaoImpl();
            //返回查询列表；
            return fileDao.searchFile(fileId, keyWord);
        } else {
            return null;
        }
    }

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

    @Override
    public boolean addFile(String fileName, String userName, int userId, int fatherId, String realPath, String modifyTime, long fileSize) {
        if (fileName != null && userName != null && realPath != null) {
            FileDaoImpl fileDao = new FileDaoImpl();
            // 返回创建是否成功
            return fileDao.addFile(fileName, userName, userId, fatherId, realPath, modifyTime, fileSize);
        }
        return false;
    }

    /**
     * 得到当前文件目录下的文件列表
     *
     * @param fileId 当前文件目录的ID
     * @return 当前文件目录下的所有文件列表
     */
    @Override
    public List<NetFile> listFile(int fileId) {
        FileDaoImpl fileDao = new FileDaoImpl();
        return fileDao.listFile(fileId);
    }

    /**
     * 根据文件或者文件夹ID删除文件
     *
     * @param fileId 文件或文件夹的ID
     * @return 是否删除成功；
     */
    @Override
    public boolean deleteFile(int fileId) {
        FileDaoImpl fileDao = new FileDaoImpl();
        return fileDao.deleteFile(fileId);
    }

    /**
     * 通过文件ID查找父目录的ID；
     *
     * @param fileId 文件ID
     * @return 父目录ID
     */
    @Override
    public int getFatherId(int fileId) {
        FileDaoImpl fileDao = new FileDaoImpl();
        return fileDao.getDiretoryByFileId(fileId);
    }

    /**
     * 用户下载时使文件下载量加1
     *
     * @param realPath 文件的相对路径
     * @return 是否增加成功
     */
    @Override
    public boolean updateDownloadTimes(String realPath) {
        FileDaoImpl fileDao = new FileDaoImpl();
        return fileDao.updateDownloadTimes(realPath);
    }

    /**
     * 修改文件的名称，同时返回新的文件列表
     *
     * @param fileId      文件的ID
     * @param newFileName 新的文件名字
     * @return 新的文件列表
     */
    @Override
    public List<NetFile> renameFile(int fileId, String newFileName) {
        //判空；
        if (null != newFileName && fileId > 0) {
            FileDaoImpl fileDao = new FileDaoImpl();
            fileDao.modifyFileName(fileId, newFileName);
            //返回新的文件列表
            return fileDao.listFile(fileDao.getDiretoryByFileId(fileId));
        } else {
            return null;
        }
    }

    /**
     * 按照类型进行目录下文件的排序；
     *
     * @param fileId 目录ID
     * @param type   排序类型；
     * @return 文件列表
     */
    @Override
    public List<NetFile> listSortedFile(int fileId, String type) {
        FileDaoImpl fileDao=new FileDaoImpl();
        return fileDao.listSortedFile(fileId,type);
    }
}
