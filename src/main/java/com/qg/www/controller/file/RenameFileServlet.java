package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.User;
import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author linxu
 * @version 1.3
 * 文件重命名。
 */
@WebServlet("/file/rename")
public class RenameFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建gson解析数据；
        Gson gson = new Gson();
        //创建数据对象；
        Data data = gson.fromJson(req.getReader(), Data.class);
        String path = req.getServletContext().getRealPath("")+data.getFilePath();
        //构造打包器；
        DataPack dataPack = new DataPack();
        FileServiceImpl fileService = new FileServiceImpl();
        //调用实现方法；
        dataPack = fileService.renameFile(data, path, dataPack);
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));
    }
}
