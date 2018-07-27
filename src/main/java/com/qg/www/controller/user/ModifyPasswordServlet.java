package com.qg.www.controller.user;

import com.google.gson.Gson;
import com.qg.www.Enum.Status;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.User;
import com.qg.www.service.impl.UserServiceImpl;
import com.qg.www.utils.DigestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author net
 * @version 1.0
 */
@WebServlet("/user/modifypassword")
public class ModifyPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        Data data = gson.fromJson(req.getReader(), Data.class);
        //保存未加密密码；
        String unSafePassword = data.getPassword();
        //加密密码
        String password = DigestUtil.md5(unSafePassword);
        //获取用户ID
        int userId = data.getUserid();
        //创建业务实现类
        UserServiceImpl userService = new UserServiceImpl();
        //构造打包器；
        DataPack dataPack = new DataPack();
        //修改密码
        User user = userService.modifyPassWord(password, userId);
        if (null != user) {
            //将传回加密前的密码；
            user.setPassword(unSafePassword);
            dataPack.setStatus(Status.NORMAL.getStatus());
            data = new Data(user);
            dataPack.setData(data);
        } else {
            dataPack.setStatus(Status.INFO_CHANGE_WROSE.getStatus());
            dataPack.setData(null);
        }
        resp.getWriter().print(gson.toJson(dataPack));
    }
}
