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
        //字符串类型文件ID；
        String strFileId = req.getParameter("fileid");
        //获取关键字；
        String key = req.getParameter("key");
        //创建打包器；
        DataPack dataPack = new DataPack();
        //创建数据对象；
        Data data = new Data();
        //判断是否满足转化为整型的条件；
        if (null != strFileId && strFileId.matches("\\d*")) {
            //转化为整型的目录ID；
            int fileId = Integer.parseInt(strFileId);
            FileServiceImpl fileService = new FileServiceImpl();
            //初始化数据对象；
            data.setFiles(fileService.searchFile(fileId, key));
            dataPack.setStatus(Status.NORMAL.getStatus());
        }else{
            //否则不返回文件列表；
            dataPack.setStatus(Status.FILE_NAME_ISEMPTY.getStatus());
        }
        //包装数据；
        dataPack.setData(data);
        //打包返回JSON数据；
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));
    }
}
