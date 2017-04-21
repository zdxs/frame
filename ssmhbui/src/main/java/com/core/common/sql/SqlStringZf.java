/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.common.sql;

import com.core.utils.CheckUtil;
import com.core.utils.PropertiesLoader;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 持久层将要用到的字符
 *
 * @author xiaosun
 */
public class SqlStringZf {

    public static final String JAVA_UTIL_LIST = "java.util.List";

    //get字符
    public static final String GET_STR = "get";
    //set字符
    public static final String SET_STR = "set";

    //boolean 
    public static final String BOOLEAN_STR = "boolean";
    public static final String BOOLEAN_STR_D = "Boolean";

    //IS
    public static final String IS_STR = "is";

    //as 必须要有空格
    public static final String AS_STR = " as ";

    //默认逐渐名字
    public static final String DEFAULT_ID = "id";
    public static final String DEFAULT_ID_LINK = "_";

    //空字符
    public static final String NULL_STR = "";

    //配置文件路径
    public static final String DATABASE_CONFIG = "properties/datasources.properties";
    public static final String DATABASE_PREFIX = "DATABASE_PREFIX";

    //filterj键值对
    public static final String DATABASE_TABLE_FILTER_KEY = "DATABASE_TABLE_FILTER_KEY";
    public static final String DATABASE_TABLE_FILTER_VAL = "DATABASE_TABLE_FILTER_VAL";

    //split
    public static final String SPLIT_STR = ",";
    //数据库字段链接字符
    public static final String DEFAULT_SQL_LINK = ",";
    public static final String DEFAULT_SQL_COLUMN_LINK = "'";
    //默认值，不使用null
    public static final String DEFAULT_VALUES = "''";
    //默认值，用null
    public static final String DEFAULT_VALUES_NULL = null;

    //枚举数据库执行sql出错
    public static enum MSG {
        SQL_ERROR("数据库CRID失败");
        private String value;

        // 构造方法
        private MSG(String value) {
            this.value = value;
        }

        // 普通方法
        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 获取特殊的表
     *
     * 实体与数据库表不统一
     *
     * @return
     */
    public static Map<String, String> getFilterMap() {
        //读取配置文件
        PropertiesLoader pl = new PropertiesLoader(SqlStringZf.DATABASE_CONFIG);
        //循环去除并且存放
        Properties pall = pl.getProperties();
        Map<String, String> filtermap = new LinkedHashMap<>();
        try {
            //获取排除key
            String[] filter_key = pall.getProperty(SqlStringZf.DATABASE_TABLE_FILTER_KEY.toString()).split(SqlStringZf.SPLIT_STR);
            //获取排除val
            String[] filter_val = pall.getProperty(SqlStringZf.DATABASE_TABLE_FILTER_VAL.toString()).split(SqlStringZf.SPLIT_STR);
            //空值
            if (null != filter_key && null != filter_val) {
                for (int i = 0; i < filter_key.length; i++) {
                    filtermap.put(filter_key[i], filter_val[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filtermap;
    }

    /**
     * 检测字段类型，该方法只针对sql拼凑过程中的新增，修改
     *
     * @param field 字段 类型Field
     * @return true|false
     */
    public static boolean CheckFieldFilter(Field field) {
        boolean flog = false;
        if (CheckUtil.isEmpty(field)) {
            return false;
        }
        try {
            //如果是集合就不加入selectSqlParms
            String fieldtypes = field.getType().getName().toString();
            if (fieldtypes.equals(SqlStringZf.JAVA_UTIL_LIST)) {
                flog = true;
            }
        } catch (Exception e) {
        }

        return flog;
    }

    /**
     * 通过配置文件取得表的前缀
     *
     * @return 返回数据库表的前缀
     */
    public static String getTablePrefix() {
        String prefix = SqlStringZf.NULL_STR;
        try {
            //读取配置文件
            PropertiesLoader pl = new PropertiesLoader(SqlStringZf.DATABASE_CONFIG);
            //循环去除并且存放
            Properties pall = pl.getProperties();
            prefix = pall.get(SqlStringZf.DATABASE_PREFIX).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix;
    }
}
