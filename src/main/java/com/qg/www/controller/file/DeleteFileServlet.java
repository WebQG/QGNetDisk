package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.service.impl.FileServiceImpl;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author linxu
 * @version 1.2
 * 接收删除文件请求，并对之进行处理
 */
@WebServlet("/file/deletefile")
public class DeleteFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        //解析JSON数据；
        Data data = gson.fromJson(req.getReader(), Data.class);
        //filePath包括文件目录路径；
        String filePath = req.getServletContext().getRealPath("")+data.getFilePath();
        DataPack dataPack;
        //执行删除操作，同时接收包装数据；
        dataPack =  new FileServiceImpl().deleteFile(filePath, data);
        resp.getWriter().print(gson.toJson(dataPack));
    }
}
