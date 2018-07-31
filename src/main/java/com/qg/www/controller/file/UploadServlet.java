package com.qg.www.controller.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qg.www.enums.Status;
import com.qg.www.beans.Data;
import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
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
     * @return 一个更改了文件名(如果传入的文件名已存在)的file对象
     */
    private File createOrRenameFile(File from) {
        // 得到该文件的前缀与后缀
        String fileName=from.getName();
        // 取到后缀名前面点的位置
        int index = fileName.lastIndexOf(".");
        String toPrefix;
        String toSuffix;

        if(index==-1){
            //File file = ZipUtil.zip(from.getAbsolutePath());
            // 这是一个文件夹
            toPrefix=fileName;
            toSuffix="";
        }else{
            // 得到该文件的前缀与后缀
            toPrefix=fileName.substring(0,index);
            toSuffix=fileName.substring(index,fileName.length());
        }
        // 得到目录
        File directory = from.getParentFile();
        File newFile = new File(directory, toPrefix + toSuffix);

        // 循环判断重命名后的文件是否存在目录所在硬盘中，如果存在就使file(number).txt的number加一
        for (int i = 1; newFile.exists() && i < Integer.MAX_VALUE; i++) {
            newFile = new File(directory, toPrefix + '(' + i + ')' + toSuffix);
        }
        return newFile;
    }

    /**
     * 输出当前目录下的文件列表
     * @param resp  响应
     * @param fileId 当前目录ID
     * @throws IOException
     */
    public void listFile(HttpServletResponse resp, String fileId) throws IOException {
        //TODO 工具类包装
        FileServiceImpl fileService = new FileServiceImpl();
        // 包装Json
        DataPack dataPack = new DataPack();
        // 得到文件目录下的所有文件
        List<NetFile> files = fileService.listFile(Integer.parseInt(fileId));
        // 返回一切正常
        dataPack.setStatus(Status.NORMAL.getStatus());
        Data data = new Data();
        data.setFiles(files);
        dataPack.setData(data);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String jsonStr = gson.toJson(dataPack,DataPack.class);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(jsonStr);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("开始");
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
        String startPos = req.getHeader("startpos");
        // 得到文件的目录、文件ID、操作用户ID
        /*String filePath = req.getParameter("filepath");
        String fileId = req.getParameter("fileid");
        String userId = req.getParameter("userid");*/

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
        if(file.exists()){
            file.mkdir();
        }

        List<FileItem> fileItems = null;
        try {
            // 解析请求内容获取文件数据
            fileItems = servletFileUpload.parseRequest(new ServletRequestContext(req));
            System.out.println("解析成功");
        } catch (FileUploadException e) {
            //TODO 捕获上传命令终止
            System.out.println("对方中断了上传进程");
        }

        System.out.println("继续");
        // 使用Iterator遍历
        Iterator<FileItem> it = fileItems.iterator();
        while (it.hasNext()){
            Object object = it.next();
            // 将Object壮行为DiskFileItem对象
            if(object instanceof DiskFileItem){
                DiskFileItem item = (DiskFileItem) object;

                // 获取文件名
                String fileName = item.getName();
                // 保存文件的真实地址
                String fileRealPath = uploadPath + File.separator + fileName;
                // 暂存文件地址
                String tempSavePath = uploadPath + "Temp" + File.separator + fileName;
                File tempSaveFile = new File(tempSavePath);
                File fileRealPathFile = new File(fileRealPath);
                // 防止文件重命名覆盖
                if(!tempSaveFile.exists()){

                }
                File fileRealRathChanged = createOrRenameFile(fileRealPathFile);
                item.setFieldName(fileRealRathChanged.getName());

                //TODO 任务队列
                //TODO 任务删除
                // 填入DiskFileItem对象以在后续获得输出流、传入文件的绝对路径，4条线程，得到文件的大小
                UpdateUtil downUtil = new UpdateUtil(item,fileRealRathChanged.getAbsolutePath(),1,req.getContentLength());

                try {
                    // 开始线程下载
                    downUtil.download();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FileServiceImpl fileService = new FileServiceImpl();
                UserServiceImpl userService = new UserServiceImpl();
                // 得到当前时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateNowStr = sdf.format(date);
                User user = userService.queryUser(Integer.parseInt(userId));
                // 向数据库添加file信息
                while(true){
                    fileService.addFile(item.getFieldName(),user.getNickName(),Integer.parseInt(userId),Integer.parseInt(fileId),
                            filePath + File.separator + fileName,dateNowStr,(long)req.getContentLength());
                }
            }
        }
        listFile(resp,fileId);
    }
}
