package com.qg.www.controller.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.service.impl.MessageServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author net
 * @version 1.5
 * 发送指定消息；
 */
@WebServlet("/message/privatemessage")
public class SendMessageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建gson
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        MessageServiceImpl messageService=new MessageServiceImpl();
        DataPack dataPack=messageService.reportMessage(gson.fromJson(req.getReader(),Data.class).getUserId());
        resp.getWriter().print(gson.toJson(dataPack));
    }
}
