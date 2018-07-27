package com.qg.www.controller.user;

import com.google.gson.Gson;
import com.qg.www.Enum.Status;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.service.impl.UserServiceImpl;
import com.qg.www.utils.DigestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/user/resetpassword")
public class ResetPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        //将JSon数据解析成Data数据
        Data data = gson.fromJson(req.getReader(), Data.class);
        //获取邮箱
        String email = data.getEmail();
        //获取重置密码
        String password = data.getPassword();
        //邮箱验证码
        String verifyCode = data.getVerifycode();
        //创建数据打包器；
        DataPack dataPack = new DataPack();
        //获取验证码的map集合
        Map<String, String> map = ((Map) req.getServletContext().getAttribute("map"));
        if (map == null || !map.get(email).equals(verifyCode)) {
            dataPack.setStatus(Status.VERIFYCODE_WROSE.getStatus());
            dataPack.setData(null);
        } else {
            UserServiceImpl userService = new UserServiceImpl();
            userService.resetPassword(email, DigestUtil.md5(password));
            dataPack.setStatus(Status.NORMAL.getStatus());
            dataPack.setData(null);
        }
        //验证完成之后，判断是否为空,包不包含用户邮箱，包含则删除；
        if (map != null && map.containsKey(email)) {
            map.remove(email);
        }
        resp.getWriter().print(gson.toJson(dataPack));

    }
}
