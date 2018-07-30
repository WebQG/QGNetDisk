package com.qg.www.controller.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.DataPack;
import com.qg.www.enums.Status;
import com.qg.www.utils.RandomVerifyCode;
import com.qg.www.utils.SendMail;

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
 * @version 1.0
 * 传送邮箱验证码
 */
@WebServlet("/user/sendverifycode")
public class SendMailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取通过URL传递过来的邮箱；
        String email=req.getParameter("email");
        String verifyCode=RandomVerifyCode.getVerifyCode();
        try {
            SendMail.sendMail(email, verifyCode);
        } catch (Exception e) {
            //TODO 日志信息；捕捉的是邮箱投递的异常；
        }
        Map<String, String> map = new HashMap<>();
        map.put(email,verifyCode);
        req.getServletContext().setAttribute("map", map);
        //响应给客户端的信息；
        DataPack dataPack = new DataPack();
        dataPack.setStatus(Status.NORMAL.getStatus());
        dataPack.setData(null);
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));
    }
}
