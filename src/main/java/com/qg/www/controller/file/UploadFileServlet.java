package com.qg.www.controller.file;

import com.qg.www.enums.Status;
import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.utils.UploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

/**
 * @author net
 * @version 1.3
 */
@MultipartConfig
@WebServlet("/file/upload.do")
public class UploadFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 得到文件的part对象
        Part part = req.getPart("123");
        // 得到文件目录ID
        String fileId = req.getHeader("fileid");
        // 得到操作者ID
        String userId = req.getHeader("userid");
        // 得到文件名
        String fileName = UploadUtil.getFileName(part);
        // 得到文件相对路径
        String filePath = req.getHeader("filepath");
        // 得到文件请求范围
        String range = req.getHeader("Range");
        // 服务器的根目录
        String servletContextPath = req.getServletContext().getRealPath("/");
        // 调用Service层
        FileServiceImpl fileService = new FileServiceImpl();
        fileService.uploadFile(servletContextPath,filePath ,fileName, range,part,
        Integer.parseInt(userId),Integer.parseInt(fileId),(long)req.getContentLength());
        // 输出当前目录下的文件列表
        UploadUtil.listFile(resp,fileId,Status.NORMAL.getStatus());
    }
}
