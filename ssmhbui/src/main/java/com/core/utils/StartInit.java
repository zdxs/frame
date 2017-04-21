package com.core.utils;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *初始化参数设置
 */
import com.core.common.KeyUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 初始化工具类
 *
 * @author xiaosun
 */
public class StartInit implements ServletContextListener {

    public static final String log4jdirkey = "corelogdir";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            System.out.println("web.xml初始化");
            //log4j
            String log4jdir = sce.getServletContext().getRealPath("/");
            //System.out.println("log4jdir:"+log4jdir);  
            System.setProperty(log4jdirkey, log4jdir);
            
            ServletContext sct = sce.getServletContext();
            //初始化参数配置
            Map<String, String> depts = new HashMap<String, String>();
            //读取配置文件
            PropertiesLoader pl = new PropertiesLoader(KeyUtil.URL.DEFAULT_PROPERTIES_PATH.toString());
            //循环去除并且存放
            Properties pall = pl.getProperties();
            //循环
            for (Map.Entry<Object, Object> entry : pall.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key != null && value != null) {
                    depts.put(key.toString(), value.toString());
                }
            }
            //初始化错误信息汇总 error_msg
//        Map<String, String> depts_error_msg = new HashMap<String, String>();
//        //读取配置文件
//        PropertiesLoader error_pl = new PropertiesLoader("properties/error_msg.properties");
//        //循环去除并且存放
//        Properties error_pall = error_pl.getProperties();
//        //循环
//        for (Map.Entry<Object, Object> entry : error_pall.entrySet()) {
//            Object key = entry.getKey();
//            Object value = entry.getValue();
//            if (key != null && value != null) {
//                depts_error_msg.put(key.toString(), value.toString());
//            }
//        }
            //取值
//        ServletContext sct = getServletConfig().getServletContext();
            // 从上下文环境中通过属性名获取属性值
//        Map<String, String> dept = (Map<String, String>) sct.getAttribute("dept");
            //存入初始参数
            sct.setAttribute(KeyUtil.KEYNAME.DEPT.toString(), depts);
//        sct.setAttribute(Constants.DEPT_ERROR, depts_error_msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("web.xml销毁");
    }

}
