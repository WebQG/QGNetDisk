package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.enums.Status;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.enums.Status;
import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.service.impl.UserServiceImpl;
import com.qg.www.utils.UploadUtil;

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

        // 拼出文件的绝对路径
        String realPath = req.getServletContext().getRealPath("") + filePath + "/" + fileName;

        // 调用Service层得到状态码
        UserServiceImpl userService = new UserServiceImpl();
        String status = userService.newFolder(realPath, Integer.parseInt(userId), fileName, Integer.parseInt(fileId), filePath);

        UploadUtil.listFile(resp, fileId, status);
    }
}
