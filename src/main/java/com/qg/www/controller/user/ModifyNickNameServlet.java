package com.qg.www.controller.user;

import com.google.gson.Gson;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.User;
import com.qg.www.enums.Status;
import com.qg.www.service.UserService;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author net
 * @version 1.0
 * 用户修改昵称；
 */
@WebServlet("/user/modifynickname")
public class ModifyNickNameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //用户ID
        String userId = req.getParameter("userid");
        //用户新昵称
        String newNickName = req.getParameter("newnickname");
        //用户业务实现；
        UserService userService = new UserServiceImpl();
        //获取用户
        User user = userService.modifyNickName(newNickName,Integer.parseInt(userId));
        //打包数据，响应。
        Gson gson = new Gson();
        DataPack dataPack = new DataPack();
        //修改成功
        if (user != null){
            Data data = new Data(user);
            dataPack.setStatus("200");
            dataPack.setData(data);
        }else {
            //修改失败；
            dataPack.setStatus(Status.INFO_CHANGE_WROSE.getStatus());
        }
        String jsonStr = gson.toJson(dataPack);
        resp.getWriter().print(jsonStr);
    }
}
