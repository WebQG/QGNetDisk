package com.qg.www.controller.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 该类用于验证邮箱与验证码是否匹配；
 *
 * @author linxu
 * @version 1.4
 */
@WebServlet("/user/validateverifycode")
public class ValidateVerifyCodeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建Gson;
        Gson gson = new Gson();
        //解析数据；
        Data data = gson.fromJson(req.getReader(), Data.class);
        //获取全局邮件存储器；
        Map<String, String> map = (Map) req.getServletContext().getAttribute("map");
        //传入map、data进行判断；
        DataPack dataPack=new UserServiceImpl().validateMail(data,map);
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(dataPack));
    }
}
