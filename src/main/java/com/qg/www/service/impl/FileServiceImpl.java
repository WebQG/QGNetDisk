package com.qg.www.service.impl;

import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.dao.impl.FileDaoImpl;
import com.qg.www.dao.impl.UserDaoImpl;
import com.qg.www.enums.Status;
import com.qg.www.service.FileService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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
     * @param keyWord 关键字
     * @return 文件列表；
     */
    @Override
    public List<NetFile> searchFile(String keyWord) {
        //判断是否为空；
        if (null != keyWord) {
            //不为空执行查询；
            FileDaoImpl fileDao = new FileDaoImpl();
            //返回查询列表；
            return fileDao.searchFile(keyWord);
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
     * @param data
     * @param path     文件根目录路径；
     * @param dataPack 数据处理
     * @return 新的文件列表
     */
    @Override
    public DataPack renameFile(Data data, String path, DataPack dataPack) {
        //获取新文件名
        String newFileName = data.getNewFileName();
        //获取原文件名；
        String fileName = data.getFileName();
        //获取文件ID；
        int fileId = data.getFileId();
        //获取操作人；
        User operator = new UserServiceImpl().getUserByUserId(data.getOperatorID());
        //获取文件归属者的个人信息
        User user = new UserServiceImpl().getUserByUserId(data.getUserId());
        //创建数据对象；
        Data data1 = new Data();
        //判空；
        if (null != operator && null != user) {
            //倘若操作者是修改自己的文件或者权限比他高，就修改。否则不修改
            if ((operator.getStatus() > user.getStatus()) || (operator.getUserId() == user.getUserId())) {
                File file = new File(path + fileName);
                file.renameTo(new File(path + newFileName));
                //修改数据库存储；
                FileDaoImpl fileDao = new FileDaoImpl();
                fileDao.modifyFileName(fileId, newFileName);
                //返回新的文件列表；
                data1.setFiles(fileDao.listFile(fileDao.getDiretoryByFileId(fileId)));
                //设置正常状态码；
                dataPack.setStatus(Status.NORMAL.getStatus());
                dataPack.setData(data1);
            } else {
                //返回原文件列表；
                data1.setFiles(listFile(getFatherId(fileId)));
                //设置无权限状态码；
                dataPack.setStatus(Status.INFO_CHANGE_WROSE.getStatus());
                dataPack.setData(data1);
            }
        } else {
            //返回原文件列表；
            data1.setFiles(listFile(getFatherId(fileId)));
            //设置无权限状态码；
            dataPack.setStatus(Status.INFO_CHANGE_WROSE.getStatus());
            dataPack.setData(data1);
        }
        return dataPack;
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
        FileDaoImpl fileDao = new FileDaoImpl();
        return fileDao.listSortedFile(fileId, type);
    }


    /**
     * 删除文件或者文件夹操作；
     *
     * @param data     数据；
     * @param filePath 文件路径
     * @return 包装数据；
     */
    @Override
    public DataPack deleteFile(String filePath, Data data) {
        //删除文件的ID;
        int fileId = data.getFileId();
        //获取操作人信息
        User operator = data.getUser();
        //调用实现层，通过文件ID查找归属人，获取归属人的权限进行判断是否能够删除。以及对返回内容进行打包。
        FileServiceImpl fileService = new FileServiceImpl();
        //先保存父节点的ID
        int fatherId = fileService.getFatherId(fileId);
        //查询文件归属人；
        User fileUser = fileService.getUserStatusByFileId(fileId);
        //获取即将被删除的文件的名字；
        String fileName = fileService.getFileNameById(fileId);
        //打包响应内容；
        DataPack dataPack = new DataPack();
        Data data1 = new Data();
        FileDaoImpl fileDao = new FileDaoImpl();
        //打开文件；
        File file = new File(filePath + fileName);
        //判断操作者与被操作者的权限；
        if (operator.getStatus() > fileUser.getStatus()) {
            //能够删除；
            fileDao.deleteFile(fileId);
            //执行服务器存储删除操作；
            try {
                //判断是文件还是文件夹；
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                    System.out.println("管理员删除文件；");
                } else {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //打包文件列表；
            data1.setFiles(listFile(fatherId));
            dataPack.setData(data1);
            dataPack.setStatus(Status.NORMAL.getStatus());
            //倘若文件是自己的，则删除自己的文件；
        } else if (fileUser.getUserId() == operator.getUserId()) {
            //能够删除；
            fileDao.deleteFile(fileId);
            //执行服务器存储删除操作；
            try {
                //判断是文件还是文件夹；
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //打包文件列表；
            data1.setFiles(listFile(fatherId));
            dataPack.setStatus(Status.NORMAL.getStatus());
            dataPack.setData(data1);
            //没有权限则是删除失败；返回父目录；
        } else {
            data1.setFiles(listFile(fatherId));
            dataPack.setStatus(Status.FILE_DELETE_WROSE.getStatus());
            dataPack.setData(data1);
        }
        return dataPack;
    }

    /**
     * 通过文件ID获取文件名称；
     *
     * @param fileID 文件ID
     * @return 文件名称；
     */
    @Override
    public String getFileNameById(int fileID) {
        FileDaoImpl fileDao = new FileDaoImpl();
        NetFile file = fileDao.getFileById(fileID);
        return file.getFileName();
    }
}
