package com.qg.www.service.impl;

import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.dao.impl.FileDaoImpl;
import com.qg.www.dao.impl.UserDaoImpl;
import com.qg.www.service.FileService;

import java.util.List;

public class FileServiceImpl implements FileService {
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

    @Override
    public List<NetFile> listAllFile() {
        FileDaoImpl fileDao = new FileDaoImpl();
        // 得到所有文件列表
        return fileDao.listAllFile();
    }

    @Override
    public boolean addFile(String fileName, String userName, int userId, int fatherId, String realPath, long modifyTime, long fileSize) {
        if(fileName!=null && userName != null && realPath != null){
            FileDaoImpl fileDao = new FileDaoImpl();
            // 返回创建是否成功
            return fileDao.addFile(fileName,userName,userId,fatherId,realPath,modifyTime,fileSize);
        }
        return false;
    }

    @Override
    public List<NetFile> listFile(int fileId) {
        FileDaoImpl fileDao = new FileDaoImpl();
        return fileDao.listAllFile();
    }

    @Override
    public boolean deleteFile(int fileId) {
        FileDaoImpl fileDao = new FileDaoImpl();
        return fileDao.deleteFile(fileId);
    }
}
