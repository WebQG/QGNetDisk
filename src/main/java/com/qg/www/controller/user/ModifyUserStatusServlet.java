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
        //创建打包器；
        DataPack dataPack = new DataPack();
        //获取修改人ID
        int userId = data.getUserId();
        //获取被修改人ID
        int operatoredId = data.getOperatorID();
        UserServiceImpl userService = new UserServiceImpl();
        //获取操作人权限；
        int userStatus = userService.getUserByUserId(userId).getStatus();
        //被操作人权限；
        int operatoredStatus = userService.getUserByUserId(operatoredId).getStatus();
        //判断是否有权限提升或者撤销管理员；
        if (userStatus > operatoredStatus) {
            //判断是撤销还是提升；
            if (operatoredStatus == UserStatus.ORDINARY_USER.getUserStatus()) {
                //提升；
                userService.modifyStatus(UserStatus.ORDINARY_ADMIN.getUserStatus(), operatoredId);
            } else {
                //撤销；
                userService.modifyStatus(UserStatus.ORDINARY_USER.getUserStatus(), operatoredId);
            }
            dataPack.setStatus(Status.NORMAL.getStatus());
            dataPack.setData(null);
        } else {
            dataPack.setStatus(Status.STATUS_NO.getStatus());
            dataPack.setData(null);
        }
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));
    }
}
