package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.Enum.Status;
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
 * 列出文件列表；
 * @author linxu
 * @version 1.2
 */
@WebServlet("/file/listfile")
public class ListFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int fileId=Integer.parseInt(req.getParameter("fileid"));
        FileServiceImpl fileService=new FileServiceImpl();
        DataPack dataPack=new DataPack();
        Data data=new Data();
        data.setFiles(fileService.listFile(fileId));
        dataPack.setStatus(Status.NORMAL.getStatus());
        dataPack.setData(data);
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));

    }
}
