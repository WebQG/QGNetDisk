package com.qg.www.controller.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.Message;
import com.qg.www.beans.User;
import com.qg.www.service.MessageService;
import com.qg.www.service.UserService;
import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.service.impl.MessageServiceImpl;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/message/listmessage")
public class ListMessageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        //TODO 未来拓展针对特定人员广播
        //User user = gson.fromJson(req.getReader(),User.class);

        // 得到包装后的文件列表
        MessageServiceImpl messageService = new MessageServiceImpl();
        DataPack dataPack = messageService.listMessages();

        // 发送动态列表
        resp.getWriter().print(gson.toJson(dataPack,DataPack.class));
    }
}
