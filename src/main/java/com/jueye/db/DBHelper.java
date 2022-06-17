package com.jueye.db;

import com.jueye.db.mapper.RowMapper;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Created by soledede.weng on 2016-11-23.
 */
public class DBHelper<T> {
    private static BasicDataSource dbcp;

    static {
        //获取源代码包根目录中的文件
        InputStream inStream = DBHelper.class.getClassLoader().getResourceAsStream("db.properties");
        Properties prop = new Properties();

        try {
            prop.load(inStream);

//一、初始化连接池
            dbcp = new BasicDataSource();
            //设置驱动 (Class.forName())
            dbcp.setDriverClassName(prop.getProperty("jdbc.driver"));
            //设置url
            dbcp.setUrl(prop.getProperty("jdbc.url"));
            //设置数据库用户名
            dbcp.setUsername(prop.getProperty("jdbc.username"));
            //设置数据库密码
            dbcp.setPassword(prop.getProperty("jdbc.password"));
            //初始连接数量
            dbcp.setInitialSize(Integer.parseInt(prop.getProperty("jdbc.initsize")));
            //连接池允许的最大连接数
            dbcp.setMaxTotal(Integer.parseInt(prop.getProperty("jdbc.maxactive")));
            //设置最大等待时间
            dbcp.setMaxWaitMillis(Integer.parseInt(prop.getProperty("jdbc.maxwait")));
            //设置最小空闲数
            dbcp.setMinIdle(Integer.parseInt(prop.getProperty("jdbc.minidle")));
            //设置最大空闲数
            dbcp.setMaxIdle(Integer.parseInt(prop.getProperty("jdbc.maxidle")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接对象
     *
     * @return Connection类的对象
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = dbcp.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    public List<T> executeQueryForList(String sql, RowMapper<T> rm, Object... args) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<T> list = new ArrayList<T>();

        try {
            conn = getConnection();

            stat = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }


            rs = stat.executeQuery();
            while (rs.next()) {
                T obj = rm.mapRow(rs);
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stat, conn);
        }
        return list;
    }


    public T executeQueryForObject(String sql, RowMapper<T> rm, Object... args) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        T obj = null;

        try {
            conn = getConnection();

            stat = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }


            rs = stat.executeQuery();
            if (rs.next()) {
                obj = rm.mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stat, conn);
        }
        return obj;
    }


    /**
     * 执行insert update delete语句
     *
     * @param sql insert or update or delte语句
     * @return true代表成功 false代表失败
     */
    public boolean executeSQL(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement stat = null;

        try {
            conn = getConnection();
            stat = conn.prepareStatement(sql);
            //?
            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }

            int rows = stat.executeUpdate();
            if (rows > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(stat, conn);
        }

        return false;
    }

    /**
     * 释放数据库资源
     *
     * @param rs
     * @param stat
     * @param conn
     */
    public void close(ResultSet rs, Statement stat, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 释放数据库资源
     *
     * @param stat
     * @param conn
     */
    public void close(Statement stat, Connection conn) {
        close(null, stat, conn);
    }
}
