package com.qg.www.controller.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.DataPack;
import com.qg.www.enums.Status;
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
 * 动态消息展示；
 */
@WebServlet("/message/listmessage")
public class ListMessageServlet extends HttpServlet {
    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        //TODO 未来拓展针对特定人员广播
        // 得到包装后的文件列表
        MessageServiceImpl messageService = new MessageServiceImpl();
        DataPack dataPack = messageService.listMessages();
        dataPack.setStatus(Status.NORMAL.getStatus());
        // 发送动态列表
        resp.getWriter().print(gson.toJson(dataPack,DataPack.class));

    }
}
