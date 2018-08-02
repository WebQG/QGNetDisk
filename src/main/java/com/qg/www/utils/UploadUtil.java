package com.qg.www.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.enums.Status;
import com.qg.www.service.impl.FileServiceImpl;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

public class UploadUtil {

    /**
     * 输出当前文件目录下的所有文件信息
     * @param resp 用来向客户端输出用户列表
     * @param fileId 当前文件目录ID
     * @throws IOException
     */
    public static void listFile(HttpServletResponse resp, String fileId, String status) throws IOException {
        //TODO 工具类包装
        FileServiceImpl fileService = new FileServiceImpl();
        // 包装Json
        DataPack dataPack = new DataPack();
        // 得到文件目录下的所有文件
        List<NetFile> files = fileService.listFile(Integer.parseInt(fileId));
        // 返回一切正常
        dataPack.setStatus(status);
        // 包装文件列表信息
        Data data = new Data();
        data.setFiles(files);
        dataPack.setData(data);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String jsonStr = gson.toJson(dataPack,DataPack.class);
        resp.getWriter().print(jsonStr);
    }

    /**
     * 得到上传文件的名字，包括前缀与后缀
     * @param part 上传文件的Part实例
     * @return 返回上传文件的名字，包括前缀与后缀
     */
    public static String getFileName(Part part) {
        // 得到头部信息
        String header = part.getHeader("Content-Disposition");
        // 截取头部信息中包含的文件名
        String fileName = header.substring(header.indexOf("filename=\"") + 10,
                header.lastIndexOf("\""));
        return fileName;
    }

    /**
     * 把文件写入服务器
     *
     * @param filePathFile 写入文件的位置
     * @param part 上传文件的Part实例
     * @param startPos 写入文件的起点
     * @param currentPartSize 所需要写入文件的长度大小
     * @return 已经写入文件的长度
     * @throws IOException
     */
    public static long writeTo(File filePathFile, Part part, long startPos, long currentPartSize) throws IOException {
        long length = 0;
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
        return length;
    }

    public static File createOrRenameFile(File from) throws IOException {
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

}
