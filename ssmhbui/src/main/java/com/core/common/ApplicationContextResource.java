package com.core.common;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * applicationContext-resource.xml 的配置数据源 具体如何使用暂时没有想好,如果不使用该类
 * <bean name="dataSource" class改为org.apache.commons.dbcp.BasicDataSource >
 * @author xiaosun
 */
public class ApplicationContextResource extends BasicDataSource {

    public <T> T unwrap(Class<T> iface) throws SQLException {
        System.out.println("com.core.common.ApplicationContextResource.unwrap()");
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        System.out.println("com.core.common.ApplicationContextResource.isWrapperFor()");
        return false;
    }

    @Override
    public synchronized void close() throws SQLException {
        DriverManager.deregisterDriver(DriverManager.getDriver(url));
        System.out.println("com.core.common.ApplicationContextResource.close()");
        super.close();
    }

    public synchronized void init() throws SQLException {
        System.out.println("com.core.common.ApplicationContextResource.init()");
        System.out.println("数据源初始化");
    }
}
