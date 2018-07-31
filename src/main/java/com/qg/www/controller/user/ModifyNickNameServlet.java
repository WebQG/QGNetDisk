package com.qg.www.controller.user;

import com.google.gson.Gson;
import com.qg.www.enums.Status;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.User;
import com.qg.www.service.UserService;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author huachen
 * @version 1.0
 */
@WebServlet("/user/modifynickname")
public class ModifyNickNameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userid");
        String newNickName = req.getParameter("newnickname");

        UserService userService = new UserServiceImpl();
        User user = userService.modifyNickName(newNickName, Integer.parseInt(userId));
        Gson gson = new Gson();
        DataPack dataPack = new DataPack();
        if (user != null) {
            Data data = new Data(user);
            dataPack.setStatus("200");
            dataPack.setData(data);
        } else {
            dataPack.setStatus(Status.INFO_CHANGE_WROSE.getStatus());
        }
        String jsonStr = gson.toJson(dataPack);
        resp.getWriter().print(jsonStr);
    }
}
