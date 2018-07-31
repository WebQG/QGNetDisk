package com.qg.www.controller.file;

import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.utils.DownloadUtil;
import com.qg.www.utils.ZipUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author net
 * @version 1.0
 */
@WebServlet("/file/download")
public class DownloadServlet extends HttpServlet {
    int startPos;
    int currentPartSize;
    int length;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("开始");
        // 得到文件的绝对路径 如QG/移动组/文件.exe
        String filePath = req.getParameter("filepath");
        String range = req.getHeader("Range");

        // 将下载次数+1
        FileServiceImpl fileService = new FileServiceImpl();
        fileService.updateDownloadTimes(filePath);
        System.out.println(filePath);

        int index;
        String fileName;
        File file = new File(req.getServletContext().getRealPath("") + File.separator + filePath);
        System.out.println(file.getAbsolutePath());
        if (!file.isDirectory()) {
            index = filePath.lastIndexOf("/");
            // 得到文件名
            fileName = filePath.substring(index + 1, filePath.length());
            // 得到文件目录
            filePath = filePath.substring(0, index);
        } else {
            ZipUtil.zip(file.getAbsolutePath());
            //TODO 不能识别的类型
            index = filePath.lastIndexOf("/");
            if (index == -1) {
                fileName = filePath + ".zip";
                filePath = "";
            } else {
                // 得到文件名
                fileName = filePath.substring(index + 1, filePath.length()) + ".zip";
                filePath = filePath.substring(0, index);
                System.out.println(fileName);
            }

        }


        // 得到文件的MIME类型
        String mimeType = getServletContext().getMimeType(fileName);
        // 响应告诉客户端文件类型
        //resp.setContentType(mimeType);
        // 编码
        String newFilename = DownloadUtil.getFilename(req, fileName);

        // 设置提示保存或打开
        resp.setHeader("content-disposition", "attachment;filename=" + newFilename);
        resp.setHeader("content-length", String.valueOf(file.length()));

        // 得到被下载文件的输入流
        InputStream is = getServletContext().getResourceAsStream(filePath + File.separator + fileName);
        ServletOutputStream os = resp.getOutputStream();
        //TODO 判断请求文件是否存在
        // 将文件内容输出
        int hasRead;
        byte[] buffer = new byte[1024];
        if (range != null) {
            // 得到断点的起点和需要读写的大小
            startPos = Integer.parseInt(range.split("-")[0].split("=")[1]);
            currentPartSize = Integer.parseInt(range.split("-")[1]) - startPos;
            // 定位到起点
            is.skip(startPos);

            while ((length < currentPartSize) && (hasRead = is.read(buffer)) != -1) {
                length += hasRead;
                os.write(buffer, 0, hasRead);
            }
        } else {
            while ((hasRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, hasRead);
            }
        }

        System.out.println("结束");
        os.close();
        is.close();
    }
}
