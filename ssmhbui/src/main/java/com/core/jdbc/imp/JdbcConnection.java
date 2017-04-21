/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.jdbc.imp;

import com.core.jdbc.JdbcConnectionInterface;
import com.core.utils.PropertiesLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xiaosun
 */
public class JdbcConnection extends JdbcConnectionInterface {

    //链接资源文件
    private static final String DATASOURCES_FILE = "properties/datasources.properties";
    //mysql驱动
    private static final String KEY_DBC_DRIVER = "datasource.driverClassName";
    //jdbc地址
    private static final String KEY_DBC_JDBCURL = "datasource.url";
    //用户
    private static final String KEY_DBC_ADMIN = "datasource.username";
    //密码
    private static final String KEY_DBC_PWD = "datasource.password";
    //jdbcip
    private static final String KEY_DBC_IP = "datasources.ip";
    //jdbc端口
    private static final String KEY_DBC_PORT = "datasources.port";
    //jdbc数据库
    private static final String KEY_DBC_DATABASE = "datasources.name";

    //定义sqk语句的执行对象
    private PreparedStatement pstmt;
    //定义查询返回的结果集合
    private ResultSet resultSet;

    /**
     * 获取jdbc连接
     *
     * @return
     */
    @Override
    public Connection getMysqlConnection() {
        Connection conn = null;
        //读取配置文件
        try {
            PropertiesLoader pl = new PropertiesLoader(DATASOURCES_FILE);
            if (pl != null) {
                String dbc_driver = pl.getProperty(KEY_DBC_DRIVER);
                //url拼凑
                String jdbc_url = pl.getProperty(KEY_DBC_JDBCURL);
                String ip_port =  pl.getProperty(KEY_DBC_IP)+":"+pl.getProperty(KEY_DBC_PORT);
                String dbc_jdbc = jdbc_url.replace("${datasources.ip.port}",ip_port).replace("${datasources.name}", pl.getProperty(KEY_DBC_DATABASE));
                String dbc_admin = pl.getProperty(KEY_DBC_ADMIN);
                String dbc_pwd = pl.getProperty(KEY_DBC_PWD);
                conn = this.getConn(dbc_driver, dbc_jdbc, dbc_admin, dbc_pwd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return conn;
    }

    /**
     * 获取jdbc连接
     *
     * @param dbc_driver 驱动
     * @param dbc_jdbc 数据库连接路径
     * @param dbc_admin 用户名
     * @param dbc_pwd 密码
     * @return
     */
    public Connection getConn(String dbc_driver, String dbc_jdbc, String dbc_admin, String dbc_pwd) {
        // TODO Auto-generated method stub
        Connection conn = null;
        try {
            Class.forName(dbc_driver); // 加载驱动
            //         获取连接
            conn = DriverManager.getConnection(dbc_jdbc, dbc_admin, dbc_pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param rs
     * @param ps
     * @param conn
     * @return
     */
    public boolean closeDB(ResultSet rs, PreparedStatement ps, Connection conn) {
        // TODO Auto-generated method stub
        boolean flog = false;
        try {
            if (rs != null) {
                
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                if (!conn.isClosed()) {
                    // 关闭数据库连接
                    conn.close();
                } else {
                    conn.close(); // 关闭连接
                }
            }
            flog = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flog;
    }

    /**
     * 完成对数据库的表的增删改的操作
     *
     * @param sql
     * @param params
     * @retuen
     * @throws SQLException
     *
     */
    public boolean upderbypaerdstaemnet(String sql, List<Object> params) throws SQLException {
        boolean flag = false;
        int result = -1;//表示当用户执行增删改所影响数据库的行数
        Connection conn = this.getMysqlConnection();
        try {
            //执行sql
            pstmt = conn.prepareStatement(sql);
            int index = 1;
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            result = pstmt.executeUpdate();
            flag = result > 0 ? true : false;
            //关闭
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //执行完毕必须关闭连接
            this.closeDB(resultSet, pstmt, conn);
        }
        return flag;
        
    }

    /**
     * 返回多条记录
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> findMoreResult(String sql, List<Object> params) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Connection conn = this.getMysqlConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            int index = 1;
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
//            System.out.println(pstmt.toString());
            resultSet = pstmt.executeQuery();//返回查询结果
            //获取列的信息 -> metaData
            ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();
            int col_len = metaData.getColumnCount();//获得列的名称
            while (resultSet.next()) {
                //******取值****/
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < col_len; i++) {
                    //记录 名字和值
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = " ";
                    }
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //执行完毕必须关闭连接
            this.closeDB(resultSet, pstmt, conn);
        }
        return list;
    }

    /**
     * 实体使用反射机制来封装
     *
     * @param <T>
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public <T> T findSimpleRefResult(String sql, List<Object> params,
            Class<T> cls) throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
        T resultObject = null;
        int index = 1;
        Connection conn = this.getMysqlConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next()) {
                //通过反射机制创建实例
                resultObject = cls.newInstance();
                //初始化
                for (int i = 0; i < cols_len; i++) {
                    //记录 名字和值
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = " ";
                    }
                    //获取javabean（UserInfo）对应列的属性
                    java.lang.reflect.Field field = cls.getDeclaredField(cols_name);
                    //打开javabea的访问私有权限
                    field.setAccessible(true);
                    //相对应的javabean进行赋值
                    field.set(resultObject, cols_value);
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //执行完毕必须关闭连接
            this.closeDB(resultSet, pstmt, conn);
        }
        return resultObject;
    }

    /**
     * 通过反射机制访问数据库
     *
     * @param <T>
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws SQLException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws InstantiationException
     */
    public <T> List<T> finMoreRefResult(String sql, List<Object> params,
            Class<T> cls) throws SQLException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InstantiationException {
        List<T> list = new ArrayList<T>();
        int index = 1;
        Connection conn = this.getMysqlConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next()) {
                //通过反射机制创建实例
                T resultObject = cls.newInstance();
                //初始化
                for (int i = 0; i < cols_len; i++) {
                    //记录 名字和值
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = " ";
                    }
                    //获取javabean（UserInfo）对应列的属性
                    java.lang.reflect.Field field = cls.getDeclaredField(cols_name);
                    //打开javabea的访问私有权限
                    field.setAccessible(true);
                    //相对应的javabean进行赋值
                    field.set(resultObject, cols_value);
                }
                list.add(resultObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //执行完毕必须关闭连接
            this.closeDB(resultSet, pstmt, conn);
        }
        return list;
    }
    
    public static void main(String[] args) {
        JdbcConnection jdbc = new JdbcConnection();
        Connection conn = jdbc.getMysqlConnection();
//        System.out.println(conn);
    }
}
