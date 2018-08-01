package com.qg.www.controller.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.enums.Status;
import com.qg.www.enums.UserStatus;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author linxu
 * @version 1.0
 * 超级管理员管理用户权限
 */
@WebServlet("/user/modifystatus")
public class ModifyUserStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建gson对象；
        Gson gson = new Gson();
        //解析数据
        Data data = gson.fromJson(req.getReader(), Data.class);
        UserServiceImpl userService = new UserServiceImpl();
        //创建打包器；
        DataPack dataPack = userService.modifyStatus(data);
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));
    }
}
