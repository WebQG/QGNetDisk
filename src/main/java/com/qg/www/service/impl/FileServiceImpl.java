package com.qg.www.service.impl;

import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.dao.impl.FileDaoImpl;
import com.qg.www.dao.impl.MessageDaoImpl;
import com.qg.www.dao.impl.UserDaoImpl;
import com.qg.www.enums.MessageActions;
import com.qg.www.enums.Status;
import com.qg.www.service.FileService;
import com.qg.www.utils.UploadUtil;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

                MessageDaoImpl messageDao = new MessageDaoImpl();
                // 得到当前日期
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateNowStr = sdf.format(date);

                String root;

                if(path.split("\\\\")[5].split("/").length > 1){
                    root = path.split("\\\\")[5].split("/")[1];
                }else {
                    root = path.split("\\\\")[5].split("/")[0];
                }
                messageDao.addMessage(dateNowStr,operator.getUserId(),user.getUserId(),fileName + "_" + newFileName,
                        root,MessageActions.RENAME_FILE.getAction());

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

        MessageDaoImpl messageDao = new MessageDaoImpl();
        // 得到当前日期
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(date);

        String root;

        // 得到文件的根目录
        if(filePath.split("\\\\")[5].split("/").length > 2){
            root = filePath.split("\\\\")[5].split("/")[1];
        }else {
            root = filePath.split("\\\\")[5].split("/")[0];
        }

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

            System.out.println(filePath);

            messageDao.addMessage(dateNowStr,operator.getUserId(),fileUser.getUserId(),fileName,
                    root,MessageActions.DELETE_FILE.getAction());

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

            messageDao.addMessage(dateNowStr,operator.getUserId(),operator.getUserId(),fileName,
                    root,MessageActions.DELETE_FILE.getAction());

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

    /**
     * 将文件上传至服务器
     *
     * @param servletContextPath 服务器端的根目录
     * @param filePath 文件的相对路径
     * @param fileName  文件名
     * @param range  请求的文件范围
     * @param part  上传文件的Part实例
     * @param userId  用户ID
     * @param fileId  文件ID
     * @param fileSize  文件大小
     * @throws IOException
     */
    @Override
    public void uploadFile(String servletContextPath,String filePath ,String fileName, String range,Part part,
                           int userId,int fileId,long fileSize)
            throws IOException {
        // 服务器端文件的写入起点
        long startPos = 0;
        // 一共写入的字节数
        long length;
        // 当前需要写入的字节数
        long currentPartSize;
        File filePathFile;

        // 判断文件的父目录是否存在
        if(new File(servletContextPath + filePath).exists()){
            // 判断是否是断点续传
            if (range == null) {
                // 新的上传
                filePathFile = UploadUtil.createOrRenameFile(new File(servletContextPath
                        + filePath + File.separator + fileName));
                // 得到本次写入的长度
                length = UploadUtil.writeTo(filePathFile, part,0,fileSize);
            } else {
                // 得到文件写入的起点位置
                startPos = Integer.parseInt(range.split("-")[0].split("=")[1]);
                // 本次文件需要写入的字节数
                currentPartSize = Integer.parseInt(range.split("-")[1]) - startPos;

                filePathFile = new File(servletContextPath + filePath + File.separator + fileName);
                length = UploadUtil.writeTo(filePathFile,part,startPos,currentPartSize);
            }
            // 文件已上传完毕
            if(startPos + length + 500>= fileSize){
                FileServiceImpl fileService = new FileServiceImpl();
                UserServiceImpl userService = new UserServiceImpl();
                // 得到当前时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateNowStr = sdf.format(date);
                User user = userService.getUserByUserId(userId);
                // 向数据库添加file信息
                fileService.addFile(filePathFile.getName(), user.getNickName(), userId, userId,
                        filePath + "/", dateNowStr, fileSize);
                // 向数据库添加message动态
                MessageDaoImpl messageDao = new MessageDaoImpl();

                System.out.println(filePath);
                System.out.println((filePath + "/").split("/").length);
                String root;
                if((filePath + "/").split("/").length > 1){
                    root = (filePath + "/").split("/")[1];
                }else {
                    root = (filePath + "/").split("/")[0];
                }

                messageDao.addMessage(dateNowStr,userId,userId,filePathFile.getName(),root,MessageActions.UPLOAD_FILE.getAction());
            }
        }else {
            //TODO 文件夹不存在
        }

    }

    @Override
    public long downloadFile(String range,InputStream is, ServletOutputStream os) throws IOException {
        long startPos;
        long currentPartSize;
        long length =  0;
        // 将文件内容输出
        int hasRead;
        byte[] buffer = new byte[1024];
        if (range != null) {
            // 得到断点的起点和需要读写的大小
            startPos = Integer.parseInt(range.split("-")[0].split("=")[1]);
            currentPartSize = Integer.parseInt(range.split("-")[1]) - startPos;
            // 定位到起点
            System.out.println(is);
            is.skip(startPos);

            System.out.println(startPos);
            System.out.println(currentPartSize);

            while ((length < currentPartSize) && (hasRead = is.read(buffer)) != -1) {
                length += hasRead;
                os.write(buffer, 0, hasRead);
                FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/123.txt"));
                fileOutputStream.write(buffer,0,hasRead);
            }

        } else {
            while ((hasRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, hasRead);
            }
        }

        return length;
    }


}
