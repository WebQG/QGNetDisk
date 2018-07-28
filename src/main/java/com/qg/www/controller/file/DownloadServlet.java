package com.qg.www.controller.file;

import com.qg.www.utils.DownloadUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author net
 * @version 1.0
 */
@WebServlet("/file/download")
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 得到文件的绝对路径
        String filePath = req.getParameter("filepath");

        int index = filePath.lastIndexOf("/");
        // 得到文件名
        String fileName = filePath.substring(index + 1,filePath.length());
        // 得到文件目录
        filePath = filePath.substring(0,index);

        // 得到文件的MIME类型
        String mimeType = getServletContext().getMimeType(fileName);
        // 响应告诉客户端文件类型
        resp.setContentType(mimeType);
        // 编码
        String newFilename = DownloadUtil.getFilename(req, fileName);
        // 设置提示保存或打开
        resp.setHeader("content-disposition", "attachment;filename=" + newFilename);

        // 得到被下载文件的输入流
        InputStream is = getServletContext().getResourceAsStream(filePath + File.separator +  fileName);
        ServletOutputStream os = resp.getOutputStream();
        //TODO 判断请求文件是否存在
        // 将文件内容输出
        int hasRead;
        byte[] buffer = new byte[1024];
        while ((hasRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, hasRead);
        }
        os.close();
        is.close();
    }
}
