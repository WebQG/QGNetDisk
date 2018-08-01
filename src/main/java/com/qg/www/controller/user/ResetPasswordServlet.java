package com.qg.www.controller.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.enums.Status;
import com.qg.www.service.impl.UserServiceImpl;
import com.qg.www.utils.DigestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author linxu
 * @version 1.1
 * 重置密码控制；
 */
@WebServlet("/user/resetpassword")
public class ResetPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        //将JSon数据解析成Data数据
        Data data = gson.fromJson(req.getReader(), Data.class);
        //获取邮箱
        String email = data.getEmail();
        //获取重置密码
        String password = data.getPassword();
        //邮箱验证码
        String verifyCode = data.getVerifyCode();
        //创建数据打包器；
        DataPack dataPack = new DataPack();
        //重置密码
        UserServiceImpl userService = new UserServiceImpl();
        userService.resetPassword(email, DigestUtil.md5(password));
        dataPack.setStatus(Status.NORMAL.getStatus());
        dataPack.setData(null);
        resp.getWriter().print(gson.toJson(dataPack));

    }
}
