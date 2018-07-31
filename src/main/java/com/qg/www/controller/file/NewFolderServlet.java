package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.enums.Status;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author net
 * @version 1.0
 */
@WebServlet("/file/newfolder")
public class NewFolderServlet extends HttpServlet {

    public void listFile(HttpServletResponse resp, String fileId, String status) throws IOException {
        //TODO 工具类包装
        FileServiceImpl fileService = new FileServiceImpl();
        // 包装Json
        DataPack dataPack = new DataPack();
        // 得到文件目录下的所有文件
        List<NetFile> files = fileService.listFile(Integer.parseInt(fileId));
        // 返回一切正常
        dataPack.setStatus(status);
        Data data = new Data();
        data.setFiles(files);
        dataPack.setData(data);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String jsonStr = gson.toJson(dataPack, DataPack.class);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(jsonStr);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 得到文件的相对路径
        String filePath = req.getParameter("filepath");
        // 得到目录的ID
        String fileId = req.getParameter("fileid");
        // 得到用户的ID
        String userId = req.getParameter("userid");
        // 得到新建文件名
        String fileName = req.getParameter("filename");

        String status = "";
        // 拼出文件的绝对路径
        String realPath = req.getServletContext().getRealPath("") + filePath + File.separator + fileName;
        File file = new File(realPath);
        Gson gson = new Gson();
        DataPack dataPack = new DataPack();
        if (!file.exists()) {
            FileServiceImpl fileService = new FileServiceImpl();
            UserServiceImpl userService = new UserServiceImpl();
            User user = userService.queryUser(Integer.parseInt(userId));

            // 文件不存在，新建文件夹，设置一切正常状态码
            file.mkdir();

            // 得到文件夹创建时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateNowStr = sdf.format(date);

            fileService.addFile(fileName, user.getNickName(), Integer.parseInt(userId), Integer.parseInt(fileId), filePath, dateNowStr, 0);
            status = Status.NORMAL.getStatus();
        } else {
            status = Status.FILE_NAME_ISEMPTY.getStatus();
        }

        listFile(resp, fileId, status);
    }
}
