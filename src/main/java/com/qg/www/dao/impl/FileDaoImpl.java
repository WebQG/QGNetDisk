package com.qg.www.dao.impl;

import com.qg.www.beans.NetFile;
import com.qg.www.dao.FileDao;
import com.qg.www.utils.DbPoolConnection;
import com.qg.www.utils.SqlCloseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
     * 查询所有文件；
     *
     * @return 文件列表
     */
    @Override
    public List<NetFile> listAllFile() {
        try {
            //TODO 补充文件查询
            connection=DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("SELECT * FROM file;");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlCloseUtil.close(connection,preparedStatement,rs);
        }
        return null;
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
            connection = DbPoolConnection.getDataSourceInstance().getConnection();
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
            connection = DbPoolConnection.getDataSourceInstance().getConnection();
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
            connection = DbPoolConnection.getDataSourceInstance().getConnection();
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
            connection = DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT user_id FROM file WHERE file_id = ?");
            preparedStatement.setInt(1,fileId);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                return rs.getInt("file_id");
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
            connection = DbPoolConnection.getDataSourceInstance().getConnection();
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
            connection = DbPoolConnection.getDataSourceInstance().getConnection();
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
}
