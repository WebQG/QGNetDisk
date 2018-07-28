package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.qg.www.beans.Data;
import com.qg.www.beans.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/file/deletefile")
public class DelectFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        req.setCharacterEncoding("UTF-8");
        User user = gson.fromJson(req.getReader(),User.class);
        String filePath = req.getParameter("filepath");
        String fileId = req.getParameter("fileid");
        filePath = req.getServletContext().getRealPath("") + filePath;
        File file = new File(filePath);
        System.out.println(filePath);



        System.out.println("结束");
    }

}
