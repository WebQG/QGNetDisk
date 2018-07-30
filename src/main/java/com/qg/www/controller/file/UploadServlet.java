package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.enums.Status;
import com.qg.www.service.FileService;
import com.qg.www.service.UserService;
import com.qg.www.service.impl.FileServiceImpl;
import com.qg.www.service.impl.UserServiceImpl;
import com.qg.www.utils.UpdateUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author net
 * @version 1.1
 */
@WebServlet("/file/upload")
public class UploadServlet extends HttpServlet {
    /**
     * @param from 指向文件的完整路径
     * @return 一个包含文件名称前缀与后缀的字符串数组
     */
    private String[] getFileInfo(File from) {
        String fileName = from.getName();
        // 取到后缀名前面点的位置
        int index = fileName.lastIndexOf(".");
        String toPrefix;
        String toSuffix;
        if (index == -1) {
            // 这是一个文件夹
            toPrefix = fileName;
            toSuffix = "";
        } else {
            // 得到该文件的前缀与后缀
            toPrefix = fileName.substring(0, index);
            toSuffix = fileName.substring(index, fileName.length());
        }
        return new String[]{toPrefix, toSuffix};
    }

    /**
     * @param from 指向文件的完整路径
     * @return 一个更改了文件名(如果传入的文件名已存在)的file对象
     */
    private File createOrRenameFile(File from) {
        // 得到该文件的前缀与后缀
        String[] fileInfo = getFileInfo(from);
        // 截取前缀和后缀
        String toPrefix = fileInfo[0];
        String toSuffix = fileInfo[1];
        File file;
        // 将其修改名字(如果名字与已存在文件重名)后拼成目标文件名
        file = createOrRenameFile(from, toPrefix, toSuffix);
        return file;
    }

    /**
     * @param from     指向文件的完整路径
     * @param toPrefix 该文件名的前缀
     * @param toSuffix 该文件名的后缀
     * @return 返回重命名(如果目录中有文件名重复)后的文件对象
     */
    private File createOrRenameFile(File from, String toPrefix, String toSuffix) {
        // 得到该文件的目录
        File directory = from.getParentFile();
        // 将其拼成一个文件的绝对路径
        File newFile = new File(directory, toPrefix + toSuffix);
        // 循环判断重命名后的文件是否存在目录所在硬盘中，如果存在就使file(number).txt的number加一
        for (int i = 1; newFile.exists() && i < Integer.MAX_VALUE; i++) {
            newFile = new File(directory, toPrefix + '(' + i + ')' + toSuffix);
        }
        return newFile;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(req)) {
            // 如果不是则停止
            PrintWriter writer = resp.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }

        String filePath = req.getHeader("filepath");
        String fileId = req.getHeader("fileid");
        String userId = req.getHeader("userid");
        System.out.println(fileId);
        System.out.println(userId);
        // 得到文件的目录、文件ID、操作用户ID
        /*String filePath = req.getParameter("filepath");
        String fileId = req.getParameter("fileid");
        String userId = req.getParameter("userid");*/

        UserServiceImpl userService = new UserServiceImpl();
        FileServiceImpl fileService = new FileServiceImpl();
        User fileUser = fileService.getUserStatusByFileId(Integer.parseInt(fileId));
        User user = userService.getUserByUserId(Integer.parseInt(userId));
        // 包装Json
        DataPack dataPack = new DataPack();
        // 判断用户权限和 用户跟目录的创建人是否是同一个人
        if (user.getStatus() > fileUser.getStatus() || user.getUserId() == fileUser.getUserId()) {
            // 创建DiskFileItemFactory工厂对象
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            // 创建文件解析器
            ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
            // 防止文件乱码
            servletFileUpload.setHeaderEncoding("utf-8");
            // 得到目录的绝对路径
            String uploadPath = req.getServletContext().getRealPath("") + filePath;

            File file = new File(uploadPath);
            // 判断该目录是否存在
            if (file.exists()) {
                file.mkdir();
            }

            List<FileItem> fileItems = null;
            try {
                // 解析请求内容获取文件数据
                fileItems = servletFileUpload.parseRequest(new ServletRequestContext(req));
            } catch (FileUploadException e) {
                //TODO 捕获上传命令终止
                System.out.println("对方中断了上传进程");
            }

            // 使用Iterator遍历
            Iterator<FileItem> it = fileItems.iterator();
            while (it.hasNext()) {
                Object object = it.next();
                // 将Object壮行为DiskFileItem对象
                if (object instanceof DiskFileItem) {
                    DiskFileItem item = (DiskFileItem) object;

                    // 获取文件名
                    String fileName = item.getName();
                    // 保存文件的真实地址
                    String fileRealPath = uploadPath + File.separator + fileName;
                    File fileRealPathFile = new File(fileRealPath);
                    // 防止文件重命名覆盖
                    File fileRealRathChanged = createOrRenameFile(fileRealPathFile);

                    //TODO 任务队列
                    //TODO 任务删除
                    //TODO 上传下载列表显示
                    //TODO 动态显示

                    // 填入DiskFileItem对象以在后续获得输出流、传入文件的绝对路径，4条线程，得到文件的大小
                    UpdateUtil downUtil = new UpdateUtil(item, fileRealRathChanged.getAbsolutePath(), 4, req.getContentLength());

                    try {
                        // 开始线程下载
                        downUtil.download();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // 得到当前时间
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateNowStr = sdf.format(date);
                    // 向数据库添加file信息
                    System.out.println(fileName);
                    fileService.addFile(fileName, user.getNickName(),user.getUserId(), Integer.parseInt(fileId),
                            filePath + File.separator + fileName, dateNowStr, (long) req.getContentLength());

                    dataPack.setStatus(Status.NORMAL.getStatus());
                }
            }
        } else {
            // 没有权限
            dataPack.setStatus(Status.STATUS_NO.getStatus());
        }

        // 得到文件目录下的所有文件
        List<NetFile> files = fileService.listFile(Integer.parseInt(fileId));
        Data data = new Data();
        data.setFiles(files);
        dataPack.setData(data);
        // 返回一切正常
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(gson.toJson(dataPack, DataPack.class));
    }
}
