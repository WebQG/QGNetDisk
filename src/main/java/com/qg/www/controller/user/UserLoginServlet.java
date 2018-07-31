package com.qg.www.controller.user;

import com.google.gson.Gson;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.User;
import com.qg.www.enums.Status;
import com.qg.www.service.impl.UserServiceImpl;
import com.qg.www.utils.DigestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author linxu
 * @version 1.0
 * 用户登录servlet,成功则返回用户的全部信息；
 */
@WebServlet("/user/login")
public class UserLoginServlet extends HttpServlet {

    @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建gson解析器；
        Gson gson=new Gson();
        //解析成一个User对象
        User user=gson.fromJson(req.getReader(),User.class);
        //获取登录数据；
        String email=user.getEmail();
        //加密前的密码；
        String unSafePassword=user.getPassword();
        //对密码进行加密；
        String password=DigestUtil.md5( user.getPassword());

        //调用登录业务,获取登录用户对象；
        UserServiceImpl userService=new UserServiceImpl();
        User loginUser=userService.login(email,password);
        //创建数据打包器；
        DataPack dataPack=new DataPack();
        //根据登录是否成功
        if(null==loginUser){
            dataPack.setStatus(Status.PASSWORD_WROSE.getStatus());
        }else{
            //打包数据，封装成JSON格式并且响应给客户端；
            dataPack.setStatus(Status.NORMAL.getStatus());
            //用登录后的对象初始化
            Data data=new Data(loginUser);
            data.setPassword(unSafePassword);
            dataPack.setData(data);
        }
        resp.getWriter().print(gson.toJson(dataPack));
    }
}
