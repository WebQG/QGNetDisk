package com.qg.www.dao.impl;

import com.qg.www.beans.NetFile;
import com.qg.www.dao.FileDao;
import com.qg.www.utils.DbPoolConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public List<NetFile> listFile() {
        try {
            //TODO 补充文件查询
            connection=DbPoolConnection.getDataSourceInstance().getConnection();
            preparedStatement=connection.prepareStatement("SELECT * FROM file;");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //利用finally块关闭文件资源
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
    public boolean addFile(String fileName, String userName, int userId, int fatherId, String realPath, long modifyTime, long fileSize) {
        return false;
    }
}
