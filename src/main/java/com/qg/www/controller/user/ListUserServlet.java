package com.qg.www.controller.user;

import com.google.gson.GsonBuilder;
import com.qg.www.enums.Status;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author linxu
 * @version 1.1
 * 用户列表
 */
@WebServlet("/user/listuser")
public class ListUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserServiceImpl userService = new UserServiceImpl();
        //创建数据对象；
        Data data = new Data();
        //创建打包器；
        DataPack dataPack=new DataPack();
        //设置响应码
        dataPack.setStatus(Status.NORMAL.getStatus());
        //列取所有用户
        data.setUsers(userService.queryAllUser());
        dataPack.setData(data);
        //返回整个用户列表
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));
    }
}
