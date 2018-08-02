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
        // 得到文件的绝对路径 如QG/移动组/文件.exe
        String filePath = req.getParameter("filepath");
        //String range = req.getParameter("Range");
        String range = req.getHeader("Range");
        System.out.println(range);
        // 得到服务器根目录
        String ServletContextPath = req.getServletContext().getRealPath("/");
        System.out.println(filePath);

        // 将下载次数+1
        FileServiceImpl fileService = new FileServiceImpl();
        fileService.updateDownloadTimes(filePath);

        File file = new File(ServletContextPath + filePath);

        // 得到文件路径和文件名
        String[] filePathAndName = DownloadUtil.getFileName(file, filePath);

        // 得到文件的MIME类型
        String mimeType = getServletContext().getMimeType(filePathAndName[1]);
        // 响应告诉客户端文件类型
        resp.setContentType(mimeType);
        // 编码
        String newFilename = DownloadUtil.encodingFilename(req, filePathAndName[1]);

        // 设置提示保存或打开
        resp.setHeader("content-disposition", "attachment;filename=" + newFilename);
        resp.setHeader("content-length", String.valueOf(file.length()));

        // 得到被下载文件的输入流
        System.out.println(filePathAndName[0] + "/" + filePathAndName[1]);
        InputStream is = getServletContext().getResourceAsStream(filePathAndName[0] + "/" + filePathAndName[1]);
        ServletOutputStream os = resp.getOutputStream();

        // 将文件内容输出
        int hasRead;
        byte[] buffer = new byte[1024];
        if (range != null) {
            // 得到断点的起点和需要读写的大小
            startPos = Integer.parseInt(range.split("-")[0].split("=")[1]);
            currentPartSize = Integer.parseInt(range.split("-")[1]) - startPos;
            // 定位到起点
            is.skip(startPos);

            System.out.println(startPos);
            System.out.println(currentPartSize);

            while ((length < currentPartSize) && (hasRead = is.read(buffer)) != -1) {
                length += hasRead;
                os.write(buffer, 0, hasRead);
            }
        } else {
            while ((hasRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, hasRead);
            }
        }
        resp.setHeader("content-length",String.valueOf(length));
        System.out.println(length);

        System.out.println("结束");
        os.close();
        is.close();


    }
}
