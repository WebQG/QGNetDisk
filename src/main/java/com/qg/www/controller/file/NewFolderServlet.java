package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.enums.Status;
import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.service.impl.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @author net
 * @version 1.0
 */
@WebServlet("/file/newfolder")
public class NewFolderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine())!=null){
            System.out.println(line);
        }
        System.out.println(req.getHeader("filePath"));
        // 得到文件的相对路径
        String filePath = req.getParameter("filepath");
        // 得到文件的ID
        String fileId = req.getParameter("fileid");
        // 得到用户的ID
        String userId = req.getParameter("userid");

        int index = filePath.lastIndexOf("/");
        // 得到文件名
        String fileName = filePath.substring(index + 1, filePath.length());
        // 得到文件路径
        filePath = filePath.substring(0, index);

        // 拼出文件的绝对路径
        String realPath = req.getServletContext().getRealPath("") + filePath + File.separator + fileName;
        File file = new File(realPath);
        Gson gson = new Gson();
        DataPack dataPack = new DataPack();
        if (!file.exists()) {
            FileServiceImpl fileService = new FileServiceImpl();
            UserServiceImpl userService = new UserServiceImpl();
            User fileUser = fileService.getUserStatusByFileId(Integer.parseInt(fileId));
            User user = userService.getUserByUserId(Integer.parseInt(userId));
            int userStatus = user.getStatus();
            int fileUserStatus = fileUser.getStatus();

            // 当用户拥有权限大于该创建目录用户的权限 或者该目录的创建者是上传者本人
            if (userStatus > fileUserStatus || user.getUserId() == Integer.parseInt(userId)) {
                // 文件不存在，新建文件夹，设置一切正常状态码
                file.mkdir();
                // 大小为0，修改时间为0，即不显示
                fileService.addFile(fileName, user.getNickName(),Integer.parseInt(userId), Integer.parseInt(fileId), filePath, "", 0);

                dataPack.setStatus(Status.NORMAL.getStatus());

                // 返回当前目录下的所有文件
                List<NetFile> files = fileService.listFile(Integer.parseInt(fileId));
                Data data = new Data();
                data.setFiles(files);
                dataPack.setData(data);
            } else {
                dataPack.setStatus(Status.STATUS_NO.getStatus());
            }
        } else {
            //TODO 该文件已存在
        }
        //TODO 返回目录下所有文件的信息
        String jsonStr = gson.toJson(dataPack);
        resp.getWriter().print(jsonStr);
    }
}
