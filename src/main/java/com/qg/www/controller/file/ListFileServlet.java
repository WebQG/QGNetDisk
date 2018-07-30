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
 * 列出文件列表；
 *
 * @author linxu
 * @version 1.2
 */
@WebServlet("/file/listfile")
public class ListFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //定义目录号；
        int fileId;
        //定义排序类型；
        String type;
        //获取目录号
        fileId = Integer.parseInt(req.getParameter("fileid"));
        //获取排序类型；
        type=req.getParameter("type");
        FileServiceImpl fileService = new FileServiceImpl();
        //创建打包器；
        DataPack dataPack = new DataPack();
        //实例化数据；
        Data data = new Data();
        //如果类型为空，则是直接显示目录下面的文件；
        if (null==type){
            //初始化数据；
            data.setFiles(fileService.listFile(fileId));
        }else {
            //否则则按照排序进行查找；
            data.setFiles(fileService.listSortedFile(fileId,type));
        }
        //设置状态码；
        dataPack.setStatus(Status.NORMAL.getStatus());
        dataPack.setData(data);
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));

    }

}
