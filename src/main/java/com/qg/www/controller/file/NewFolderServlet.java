package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.qg.www.Enum.Status;
import com.qg.www.beans.DataPack;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author net
 * @version 1.0
 */
@WebServlet("/file/newfolder")
public class NewFolderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 得到文件的相对路径
        String filePath = req.getParameter("filepath");
        // 得到文件的ID
        String fileId = req.getParameter("fileid");
        // 得到文件名
        String fileName = req.getParameter("filename");

        // 拼出文件的绝对路径
        String realPath = req.getServletContext().getRealPath("") + filePath + File.separator + fileName;
        File file = new File(realPath);
        Gson gson = new Gson();
        DataPack dataPack = new DataPack();
        if(!file.exists()){
            // 文件不存在，新建文件夹，设置一切正常状态码
            file.mkdir();
            dataPack.setStatus(Status.NORMAL.getStatus());
        }else{
            //TODO 该文件已存在
        }
        //TODO 返回目录下所有文件的信息
        String jsonStr = gson.toJson(dataPack);
        resp.getWriter().print(jsonStr);
    }
}
