package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.User;
import com.qg.www.enums.Status;
import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
/**
 * @author linxu
 * @version 1.3
 * 文件或者文件夹重命名。
 */
@WebServlet("/file/rename")
public class RenameFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建gson解析数据；
        Gson gson = new Gson();
        Data data ;
        data = gson.fromJson(req.getReader(), Data.class);
        String newFileName = data.getNewFileName();
        String fileName = data.getFileName();
        int fileId = data.getFileId();
        String path=req.getServletContext().getRealPath(data.getFilePath());
        //构造打包器；
        DataPack dataPack = new DataPack();
        //判断是否有权限重命名文件夹
        UserServiceImpl userService = new UserServiceImpl();
        //获取操作人的信息；
        User operator = userService.getUserByUserId(data.getOperatorID());
        //获取文件归属者的个人信息
        User user = userService.getUserByUserId(data.getUserId());
        FileServiceImpl fileService = new FileServiceImpl();
        if (null != operator && null != user) {
            //倘若操作者是修改自己的文件或者权限比他高，就修改。否则不修改
            if ((operator.getStatus() > user.getStatus()) || (operator.getUserId() == user.getUserId())) {
                File file = new File(path+ fileName);
                file.renameTo(new File(path + newFileName));
                //修改数据库存储数据；
                data.setFiles(fileService.renameFile(fileId, newFileName));
                //打包数据返回；
                dataPack.setStatus(Status.NORMAL.getStatus());
                dataPack.setData(data);
            }else {
                dataPack.setStatus(Status.INFO_CHANGE_WROSE.getStatus());
                data.setFiles(fileService.listFile(fileService.getFatherId(fileId)));
                dataPack.setData(data);
            }
        } else {
                dataPack.setStatus(Status.INFO_CHANGE_WROSE.getStatus());
                data.setFiles(fileService.listFile(fileService.getFatherId(fileId)));
                dataPack.setData(data);
        }
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));

    }
}
