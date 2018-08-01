package com.qg.www.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * druid数据库连接工具类；
 * @version 1.0
 * @author linxu
 */
public class DbPoolConnectionUtil {
    /**
     * 声明数据源类变量
     */
    private static DataSource dataSource = null;
    /**
     * 声明配置变量；
     */
    private static Properties properties = null;
    /**
     * 默认原始连接池实例为空；
     */
    private static DbPoolConnectionUtil poolConnectionInstance = null;

    /**
     * 静态代码块；
     */
    static {
        try {
            properties = getProperties("D:\\QG\\QGNetDisk\\src\\main\\java\\druid.properties");

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //工厂创建数据源；
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param properties
     * @throws IOException
     * @return返回配置实例；
     */
    private static Properties getProperties(String properties) throws IOException {
        Properties p;
        InputStream ins = null;
        if (properties == null) {
            throw new IllegalArgumentException("配置文件名不能为空-properties'fileName  can not be null!");
        }
        //实例化配置对象；
        p = new Properties();
        //打开文件输入流；
        try {
            ins = new FileInputStream(properties);
            p.load(ins);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            //关闭输入流；
            if (ins != null) {
                ins.close();
            }
        }
        return p;
    }

    /**
     * 从数据源中获取连接
     *
     * @return connection
     * @throws SQLException
     */
    public DruidPooledConnection getConnection() throws SQLException {
        return (DruidPooledConnection) dataSource.getConnection();
    }

    public static synchronized DbPoolConnectionUtil getDataSourceInstance() {
        if (poolConnectionInstance == null) {
            //创建单例；
            poolConnectionInstance = new DbPoolConnectionUtil();
        }
        return poolConnectionInstance;
    }

    private DbPoolConnectionUtil() {
    }
}

