package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.qg.www.enums.Status;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.User;
import com.qg.www.service.impl.FileServiceImpl;
import org.apache.commons.io.FileUtils;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author linxu
 * @version 1.2
 * 接收删除文件请求，并对之进行处理
 */
@WebServlet("/file/deletefile")
public class DeleteFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        //解析JSON数据；
        Data data = gson.fromJson(req.getReader(), Data.class);
        //filePath包括文件和文件名；
        String filePath = data.getFilePath();
        int fileId = data.getFileId();
        //获取操作人信息
        User operator = data.getUser();
        //调用实现层，通过文件ID查找归属人，获取归属人的权限进行判断是否能够删除。以及对返回内容进行打包。
        FileServiceImpl fileService = new FileServiceImpl();
        //先保存父节点的ID
        int fatherId = fileService.getFatherId(fileId);
        //查询文件归属人；
        User fileUser = fileService.getUserStatusByFileId(fileId);
        //打包响应内容；
        DataPack dataPack = new DataPack();
        //如果是自己的文件；
        if (operator.getUserId() == fileUser.getUserId()) {
            //删除文件
            fileService.deleteFile(fileId);
            //删除文件存储的操作
            FileUtils.deleteDirectory(new File(req.getServletContext().getRealPath(filePath)));
            data.setFiles(fileService.listFile(fatherId));
            dataPack.setStatus(Status.NORMAL.getStatus());
            //如果权限比他高。
        } else if (operator.getStatus() > fileUser.getStatus()) {
            //删除文件
            fileService.deleteFile(fileId);
            //删除文件存储的操作
            FileUtils.deleteDirectory(new File(req.getServletContext().getRealPath(filePath)));
            data.setFiles(fileService.listFile(fatherId));
            dataPack.setStatus(Status.NORMAL.getStatus());
            //不能够删除
        } else {
            data.setFiles(fileService.listFile(fileService.getFatherId(fileId)));
            dataPack.setStatus(Status.FILE_DELECT_WROSE.getStatus());
        }
        dataPack.setData(data);
        resp.getWriter().print(gson.toJson(dataPack));
    }
}
