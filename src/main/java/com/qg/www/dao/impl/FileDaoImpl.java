package com.qg.www.dao.impl;

import com.qg.www.beans.DataPack;
import com.qg.www.beans.NetFile;
import com.qg.www.beans.User;
import com.qg.www.dao.FileDao;
import com.qg.www.utils.DbPoolConnectionUtil;
import com.qg.www.utils.DbPoolConnectionUtil;
import com.qg.www.utils.SqlCloseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author net
 * @version 1.1
 */
public class FileDaoImpl implements FileDao {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    boolean flag=false;


    /**
     * 模糊搜索文件；
     *
     * @param keyWord 关键字；
     * @return 文件列表；
     */
    @Override
    public List<NetFile> searchFile(String keyWord) {
        //创建文件List
        List<NetFile> fileList=new ArrayList<>();
        try {
            connection=DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("SELECT * from file where file_name LIKE ?;");
            //模糊搜索；
            preparedStatement.setString(1,"%"+keyWord+"%");
            rs=preparedStatement.executeQuery();
            while (rs.next()){
                //创建文件；
                NetFile file= new NetFile();
                //初始化文件参数；
                file.setRealPath(rs.getString("real_path"));
                file.setFileId(rs.getInt("file_id"));
                file.setFileName(rs.getString("file_name"));
                file.setFatherId(rs.getInt("father_id"));
                file.setUserId(rs.getInt("user_id"));
                file.setUserName(rs.getString("user_name"));
                file.setModifyTime(rs.getString("modify_time"));
                file.setDownloadTimes(rs.getInt("download_times"));
                //将文件添加进列表；
                fileList.add( file);
            }

        } catch (SQLException e) {
            //TODO 采用日志；
        }
        return fileList;
    }

    /**
     * 排序浏览文件；
     *
     * @param fileId 父目录ID
     * @param type   排序方式；
     * @return 文件列表；
     */
    @Override
    public List<NetFile> listSortedFile(int fileId, String type) {
        List<NetFile> fileList=new ArrayList<>();
        String sql;
        //判断是按照日期排序或者是拼音排序
        if("num".equals(type)){
            //日期降序；
            sql="SELECT *from  file    where father_id=?  order by modify_time desc ;";
        }else {
            //拼音升序；
            sql="select * from file where father_id=? order by convert ( file_name using gbk)collate gbk_chinese_ci  ;";
        }
        try {
            connection=DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,fileId);
            rs=preparedStatement.executeQuery();
            while(rs.next()){
                //创建新的文件对象；
                NetFile file=new NetFile();
                //初始化文件参数；
                file.setRealPath(rs.getString("real_path"));
                file.setFileId(rs.getInt("file_id"));
                file.setFileName(rs.getString("file_name"));
                file.setFatherId(rs.getInt("father_id"));
                file.setUserId(rs.getInt("user_id"));
                file.setUserName(rs.getString("user_name"));
                file.setDownloadTimes(rs.getInt("download_times"));
                file.setModifyTime(rs.getString("modify_time"));
                //将文件添加进列表；
                fileList.add( file);
            }
        } catch (SQLException e) {
           //TODO 日志记录异常；
        }
        return fileList;
    }

    /**
     * 添加文件或者文件夹
     *
     * @param fileName   文件名
     * @param userName   用户名
     * @param userId     用户ID
     * @param fatherId   父目录ID
     * @param realPath   路径
     * @param modifyTime 最后修改时间
     * @param fileSize   文件大小
     * @return 是否成功添加；
     */
    @Override
    public boolean addFile(String fileName, String userName, int userId, int fatherId, String realPath, String modifyTime, long fileSize) {
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("INSERT INTO file (file_name,father_id,user_id," +
                    "user_name,modify_time,filesize,real_path)VALUES (?,?,?,?,?,?,?)");
            preparedStatement.setString(1,fileName);
            preparedStatement.setInt(2,fatherId);
            preparedStatement.setInt(3,userId);
            preparedStatement.setString(4,userName);
            preparedStatement.setString(5,modifyTime);
            preparedStatement.setLong(6,fileSize);
            preparedStatement.setString(7,realPath);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                // 关闭流
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     *
     * @param fileId 当前文件目录的ID
     * @return 当前文件目录下的所有文件列表
     */
    @Override
    public List<NetFile> listFile(int fileId) {
        List<NetFile> fileList = new ArrayList<>();
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM file WHERE father_id = ?");
            preparedStatement.setInt(1,fileId);
            rs = preparedStatement.executeQuery();
            while(rs.next()){
                NetFile netFile = new NetFile();
                netFile.setFileId(rs.getInt("file_id"));
                netFile.setFileName(rs.getString("file_name"));
                netFile.setFatherId(rs.getInt("father_id"));
                netFile.setUserId(rs.getInt("user_id"));
                netFile.setUserName(rs.getString("user_name"));
                netFile.setModifyTime(rs.getString("modify_time"));
                netFile.setDownloadTimes(rs.getInt("download_times"));
                netFile.setRealPath(rs.getString("real_path"));
                fileList.add(netFile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return fileList;
    }

    @Override
    public boolean deleteFile(int fileId) {
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM file WHERE file_id=?");
            preparedStatement.setInt(1,fileId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return false;
    }

    @Override
    public int getUserId(int fileId) {
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT user_id FROM file WHERE file_id = ?");
            preparedStatement.setInt(1,fileId);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return 0;
    }

    /**
     * 通过文件ID，找出父目录ID；
     *
     * @param fileId
     * @return
     */
    @Override
    public int getDiretoryByFileId(int fileId) {
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT father_id FROM file WHERE file_id=?;");
            preparedStatement.setInt(1, fileId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return rs.getInt("father_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 用户下载时使文件下载量加1
     *
     * @param realPath 文件的相对路径
     * @return 是否增加成功
     */
    @Override
    public boolean updateDownloadTimes(String realPath) {
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("UPDATE file SET download_times = download_times + 1 WHERE real_path=?");
            preparedStatement.setString(1,realPath);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return false;
    }

    /**
     * 重命名文件
     *
     * @param fileId      文件ID
     * @param newFileName 新的文件名
     * @return 是否成功修改
     */
    @Override
    public boolean modifyFileName(int fileId, String newFileName) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(date);
        try {
            connection = DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("UPDATE file SET file_name=? ,modify_time = ? WHERE file_id=?;");
            preparedStatement.setString(1, newFileName);
            preparedStatement.setString(2,dateNowStr);
            preparedStatement.setInt(3, fileId);
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlCloseUtil.close(connection, preparedStatement, rs);
        }
        return false;
    }

    /**
     * 通过文件ID获取文件
     *
     * @param fileId 文件ID
     * @return 文件；
     */
    @Override
    public NetFile getFileById(int fileId) {
        NetFile netFile=new NetFile();
        try {
            connection=DbPoolConnectionUtil.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("SELECT * from file where file_id=?;");
            preparedStatement.setInt(1,fileId);
            rs=preparedStatement.executeQuery();
            while(rs.next()){
                netFile.setFileId(rs.getInt("file_id"));
                netFile.setFileName(rs.getString("file_name"));
                netFile.setFatherId(rs.getInt("father_id"));
                netFile.setUserId(rs.getInt("user_id"));
                netFile.setUserName(rs.getString("user_name"));
                netFile.setModifyTime(rs.getString("modify_time"));
                netFile.setDownloadTimes(rs.getInt("download_times"));
                netFile.setRealPath(rs.getString("real_path"));
            }
            return netFile;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return null;
    }
}
