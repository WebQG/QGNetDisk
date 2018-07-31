package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.enums.Status;
import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author net
 * @version 1.3
 */
@MultipartConfig
@WebServlet("/file/upload.do")
public class UploadFileServlet extends HttpServlet {
    int length = 0;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 得到文件的part对象
        Part part = req.getPart("123");
        // 得到文件目录ID
        String fileId = req.getHeader("fileid");
        // 得到操作者ID
        String userId = req.getHeader("userid");
        // 得到文件名
        String fileName = getFileName(part);
        // 得到文件相对路径
        String filePath = req.getHeader("filepath");
        // 得到文件请求范围
        String range = req.getHeader("Range");
        int startPos = 0;
        int currentPartSize = 0;
        File filePathFile;
        if (range == null) {
            // 新的上传
            filePathFile = createOrRenameFile(new File(req.getServletContext().getRealPath("/")
                    + filePath + File.separator + fileName));
            writeTo(filePathFile, part,0,req.getContentLength());
        } else {
            startPos = Integer.parseInt(range.split("-")[0].split("=")[1]);
            currentPartSize = Integer.parseInt(range.split("-")[1]) - startPos;
            filePathFile = new File(req.getServletContext().getRealPath("/") + filePath + File.separator + fileName);
            writeTo(filePathFile,part,startPos,currentPartSize);
        }
        listFile(resp,fileId);
        if(startPos + length >= currentPartSize){
            FileServiceImpl fileService = new FileServiceImpl();
            UserServiceImpl userService = new UserServiceImpl();
            // 得到当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateNowStr = sdf.format(date);
            User user = userService.queryUser(Integer.parseInt(userId));
            // 向数据库添加file信息
            fileService.addFile(filePathFile.getName(), user.getNickName(), Integer.parseInt(userId), Integer.parseInt(fileId),
                    filePath + File.separator + fileName, dateNowStr, (long) req.getContentLength());
            System.out.println(filePath + File.separator + fileName);
        }
    }

    private String getFileName(Part part) {
        String header = part.getHeader("Content-Disposition");
        String fileName = header.substring(header.indexOf("filename=\"") + 10,
                header.lastIndexOf("\""));
        return fileName;
    }

    /**
     * @param from 指向文件的完整路径
     * @return 一个更改了文件名(如果传入的文件名已存在)的file对象
     */
    private File createOrRenameFile(File from) throws IOException {
        // 得到该文件的前缀与后缀
        String fileName = from.getName();
        // 取到后缀名前面点的位置
        int index = fileName.lastIndexOf(".");
        String toPrefix;
        String toSuffix;

        if (index == -1) {
            //File file = ZipUtil.zip(from.getAbsolutePath());
            // 这是一个文件夹
            toPrefix = fileName;
            toSuffix = "";
        } else {
            // 得到该文件的前缀与后缀
            toPrefix = fileName.substring(0, index);
            toSuffix = fileName.substring(index, fileName.length());
        }
        // 得到目录
        File directory = from.getParentFile();
        File newFile = new File(directory, toPrefix + toSuffix);

        // 循环判断重命名后的文件是否存在目录所在硬盘中，如果存在就使file(number).txt的number加一
        for (int i = 1; newFile.exists() && i < Integer.MAX_VALUE; i++) {
            newFile = new File(directory, toPrefix + '(' + i + ')' + toSuffix);
        }
        return newFile;
    }

    private void writeTo(File filePathFile, Part part, int startPos, int currentPartSize) throws IOException {
        InputStream in = part.getInputStream();
        RandomAccessFile randomAccessFile = new RandomAccessFile(filePathFile, "rw");
        byte[] buffer = new byte[1024];
        in.skip(startPos);
        randomAccessFile.seek(startPos);
        int hasRead;
            while ((length < currentPartSize) && (hasRead = in.read(buffer)) != -1){
                length += hasRead;
                randomAccessFile.write(buffer, 0, hasRead);
            }
        in.close();
        randomAccessFile.close();
    }

    public void listFile(HttpServletResponse resp, String fileId) throws IOException {
        //TODO 工具类包装
        FileServiceImpl fileService = new FileServiceImpl();
        // 包装Json
        DataPack dataPack = new DataPack();
        // 得到文件目录下的所有文件
        List<NetFile> files = fileService.listFile(Integer.parseInt(fileId));
        // 返回一切正常
        dataPack.setStatus(Status.NORMAL.getStatus());
        Data data = new Data();
        data.setFiles(files);
        dataPack.setData(data);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String jsonStr = gson.toJson(dataPack,DataPack.class);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(jsonStr);
    }
}
