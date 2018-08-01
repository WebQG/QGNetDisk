package com.qg.www.controller.file;

import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.enums.Status;
import com.qg.www.service.impl.FileServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author linxu
 * @version 1.3
 * 模糊搜索控制；
 */
@WebServlet("/file/searchfile")
public class SearchFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取关键字；
        String key = req.getParameter("key");
        //创建打包器；
        DataPack dataPack = new DataPack();
        //创建数据对象；
        Data data = new Data();
        FileServiceImpl fileService=new FileServiceImpl();
        //获取搜索文件列表；
        data.setFiles(fileService.searchFile(key));
        //设置状态码；
        dataPack.setStatus(Status.NORMAL.getStatus());
        //包装数据；
        dataPack.setData(data);
        //打包返回JSON数据；
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));
    }
}
