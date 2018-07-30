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
import java.util.Map;

/**
 * 用户注册servlet；
 *
 * @author linxu
 * @version 1.0
 */
@WebServlet("/user/register")
public class UserRegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建gson解析器；
        Gson gson = new Gson();
        //解析成一个User对象
        Data data = gson.fromJson(req.getReader(), Data.class);
        //获取邮箱
        String email = data.getEmail();
        //获取昵称
        String nickName = data.getNickName();
        //获取验证码
        String unSafePassword=data.getPassword();
        String verifyCode = data.getVerifyCode();
        //加密密码；
        String password = DigestUtil.md5(data.getPassword());
        User user;
        //创建数据打包器；
        DataPack dataPack = new DataPack();
        //TODO 后期修改为邮箱验证码常量；
        //获取验证码的map集合
        Map<String, String> map = ((Map) req.getServletContext().getAttribute("map"));
        if (map == null || !verifyCode.equals(map.get(email))) {
            dataPack.setStatus(Status.VERIFYCODE_WROSE.getStatus());
            dataPack.setData(null);
        } else {
            //注册用户
            UserServiceImpl userService = new UserServiceImpl();
            user = userService.register(email, password, nickName,unSafePassword);
            //邮箱是否存在；
            if (null != user) {
                user.setPassword(unSafePassword);
                Data datas = new Data(user);
                dataPack.setStatus(Status.NORMAL.getStatus());
                dataPack.setData(datas);
            } else {
                //邮箱已经被注册
                data = null;
                dataPack.setStatus(Status.EMIAL_ISREGISTER.getStatus());
                dataPack.setData(data);
            }
        }
        //验证完成之后，判断是否为空,包不包含用户邮箱，包含则删除；
        if(map!=null&&map.containsKey(email)){
            map.remove(email);
        }
        //将信息打包响应给客户端；
        resp.getWriter().print(gson.toJson(dataPack));
    }
}
