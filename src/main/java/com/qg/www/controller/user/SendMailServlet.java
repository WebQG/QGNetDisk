package com.qg.www.controller.user;

import com.google.gson.GsonBuilder;
import com.qg.www.beans.DataPack;
import com.qg.www.enums.Status;
import com.qg.www.service.impl.UserServiceImpl;
import com.qg.www.utils.RandomVerifyCodeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linxu
 * @version 1.4
 * 传送邮箱验证码
 */
@WebServlet("/user/sendverifycode")
public class SendMailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取通过URL传递过来的邮箱；
        String email = req.getParameter("email");
        //随机生成验证码；
        String verifyCode = RandomVerifyCodeUtil.getVerifyCode();
        //该类复用，因此采用是否是注册来判断它具体的使用；
        String isRegister = req.getParameter("isregister");
        UserServiceImpl userService = new UserServiceImpl();
        //获取封装数据；
        DataPack dataPack = userService.sendMail(email, verifyCode, isRegister);
        //如果正常发送邮件,则创建全局存储；
        if (Status.NORMAL.getStatus().equals(dataPack.getStatus())){
            //如果发送成功,创建ServletContext的map;
            Map<String, String> map = new HashMap<>(16);
            map.put(email, verifyCode);
            req.getServletContext().setAttribute("map", map);
        }
        //照常响应；
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));
    }
}
