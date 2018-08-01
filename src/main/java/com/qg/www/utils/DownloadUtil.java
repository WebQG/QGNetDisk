package com.qg.www.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DownloadUtil {

    public static String encodingFilename(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("user-agent");
        // 针对以IE或者Edge为内核的浏览器
        if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
            filename = URLEncoder.encode(filename, "UTF-8");
        } else {
            // 非IE浏览器的处理
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        return filename;
    }

    public static String[] getFileName(File file, String filePath) {
        int index;
        String fileName;
        // 判断是否文件夹
        if (!file.isDirectory()) {
            index = filePath.lastIndexOf("/");
            // 得到文件名
            fileName = filePath.substring(index + 1, filePath.length());
            // 得到文件目录
            filePath = filePath.substring(0, index);
        } else {
            // 将文件夹压缩成zip文件
            try {
                ZipUtil.zip(file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("压缩失败");
                e.printStackTrace();
            }
            index = filePath.lastIndexOf("/");
            // 如果文件夹是根目录
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
        String[] strings = {filePath, fileName};
        return strings;
    }
}
