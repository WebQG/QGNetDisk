package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.qg.www.Enum.Status;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linxu
 * @version 1.2
 * 计算剩余容量并且返回。
 */
@WebServlet("/file/capacity")
public class QueryCapacityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long size;
        //项目中的upload文件夹
        File directory=new File(req.getServletContext().getRealPath("QG/"));
        //采用文件工具类计算文件夹的大小。
        size=FileUtils.sizeOfDirectory(directory);
        //创建Gson
        Gson gson=new Gson();
        //构造Map存储数据；
        Map map=new HashMap(16);
        map.put("data",size);
        map.put("status",Status.NORMAL.getStatus());
        resp.getWriter().print(gson.toJson(map));
    }
}
