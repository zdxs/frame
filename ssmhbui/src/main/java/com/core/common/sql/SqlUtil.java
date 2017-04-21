/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.common.sql;

import com.core.bean.CoreModel;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sql生成工具类
 *
 * @author xiaosun
 * @param <T>
 */
public class SqlUtil<T extends CoreModel> {

    /**
     * 获取实体类的某个字段
     *
     * @param t 实体
     * @param fieldName 字段
     * @return
     */
    public Field getField(Class<?> t, String fieldName) {
        Field[] fields = t.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 核心拼凑
     *
     * @param po 实体bean
     * @param listparam 返回集合
     * @return listparam
     */
    public List<Pram> publicSetParamList(Class<T> po, List<Pram> listparam) {
        //获取字段
        Field[] fields = po.getDeclaredFields();
        if (null != fields && fields.length > 0) {
            try {
                Object o = po.newInstance();
                for (Field f : fields) {
                    //如果是集合就不加入selectSqlParms
                    String fieldtypes = f.getType().getName().toString();

//                    if (!fieldtypes.equals(SqlStringZf.JAVA_UTIL_LIST)) {
                    if (!SqlStringZf.CheckFieldFilter(f)) {
                        String fName = f.getName();
                        //判断是否是boolean类型
                        String getf = SqlStringZf.GET_STR;
                        String fieldType = f.getGenericType().toString();
                        if (fieldType.indexOf(SqlStringZf.BOOLEAN_STR) != -1 || fieldType.indexOf(SqlStringZf.BOOLEAN_STR_D) != -1) {
                            getf = SqlStringZf.IS_STR;
                        }

                        //判断是否使用了某个注解
                        if (f.isAnnotationPresent(FieldName.class)) {
                            String fieldName = f.getAnnotation(FieldName.class).name();
                            Method get = po.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                            Object getValue = get.invoke(o);
                            Pram pram = new Pram(fieldName + SqlStringZf.AS_STR + fName, getValue);
                            listparam.add(pram);
                        } else {
                            String fieldName = toTableString(fName);
                            Method get = po.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                            Object getValue = get.invoke(o);
                            Pram pram = new Pram(fieldName + SqlStringZf.AS_STR + fName, getValue);
                            listparam.add(pram);
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return listparam;
    }

    /**
     * 获取查询sql的字段列表
     *
     * @param po 实体
     * @return 获取实体类的字段集合
     */
    public List<Pram> getPramListOfSelect(CoreModel po) {
        List<Pram> list = new ArrayList<>();
        try {
            //获取list集合
            list = corefields(po, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取实体类对应的表名
     *
     * @param po 实体
     * @return 表名
     */
    public String getTableName(CoreModel po) {
        String className = po.getClass().getSimpleName();
        String tname = SqlStringZf.NULL_STR;
        try {
            //骆驼峰
            tname = toTableString(className);
            //拼凑
            tname = SqlStringZf.getTablePrefix() + SqlStringZf.DEFAULT_ID_LINK + tname;
            //表名书检查
            Map<String, String> filtermap = SqlStringZf.getFilterMap();
            if (null != filtermap && filtermap.size() > 0) {
                for (Map.Entry<String, String> entry : filtermap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    //判断
                    if (null != key && key.equals(tname)) {
                        tname = tname + value;
                    }
                }
            } else {
                tname = SqlStringZf.getTablePrefix() + SqlStringZf.DEFAULT_ID_LINK + tname;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tname;

    }

    /**
     * 核心拼凑
     *
     * @param <T>
     * @param po
     * @param listparam
     * @return
     * @throws java.lang.InstantiationException
     */
    public static <T extends CoreModel> List<Pram> corefields(CoreModel po, List<Pram> listparam) throws InstantiationException {

        try {
            Class<? extends CoreModel> thisClass = po.getClass();

            //获取字段数组
            Field[] fields = thisClass.getDeclaredFields();
            if (null != fields && fields.length > 0) {
                for (Field f : fields) {
                    //如果是集合就不加入selectSqlParms
                    String fieldtypes = f.getType().getName();
//                    if (!fieldtypes.equals(SqlStringZf.JAVA_UTIL_LIST)) {
                    if (!SqlStringZf.CheckFieldFilter(f)) {
                        String fName = f.getName();
                        //判断是否是boolean类型
                        String getf = SqlStringZf.GET_STR;
                        String fieldType = f.getGenericType().toString();
                        if (fieldType.contains(SqlStringZf.BOOLEAN_STR) || fieldType.indexOf(SqlStringZf.BOOLEAN_STR_D) != -1) {
                            getf = SqlStringZf.IS_STR;
                        }
                        if (f.isAnnotationPresent(FieldName.class)) {
                            String fieldName = f.getAnnotation(FieldName.class).name();
                            Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                            Object getValue = get.invoke(po);
//                            Pram pram = new Pram(fieldName + SqlStringZf.AS_STR + fName, getValue);
                            Pram pram = new Pram(fieldName, getValue);
                            listparam.add(pram);
                        } else {
                            String fieldName = new SqlUtil<T>().toTableString(fName);
                            Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                            Object getValue = get.invoke(po);
//                            Pram pram = new Pram(fieldName + SqlStringZf.AS_STR + fName, getValue);
                            Pram pram = new Pram(fieldName, getValue);
                            listparam.add(pram);
                        }
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return listparam;
    }

    /**
     * 获取条件列
     *
     * @param <T>
     * @param po
     * @return
     */
    public static <T extends CoreModel> List<Pram> getPramListofStatic(CoreModel po) {
        List<Pram> list = new ArrayList<>();
        try {
            list = corefields(po, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过Class获取生成对应Sql字段
     *
     * @param po
     * @return
     */
    public List<Pram> getPramList(Class<T> po) {
        List<Pram> list = new ArrayList<>();
        Class<? extends CoreModel> thisClass = po;
        Field[] fields = thisClass.getDeclaredFields();
        try {
            Object o = thisClass.newInstance();
            for (Field f : fields) {
                String fName = f.getName();

                //判断是否是boolean类型
                String getf = SqlStringZf.GET_STR;
                String fieldType = f.getGenericType().toString();
                if (fieldType.indexOf(SqlStringZf.BOOLEAN_STR) != -1 || fieldType.indexOf(SqlStringZf.BOOLEAN_STR_D) != -1) {
                    getf = SqlStringZf.IS_STR;
                }
                if (f.isAnnotationPresent(FieldName.class)) {
                    String fieldName = f.getAnnotation(FieldName.class).name();
                    Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                    Object getValue = get.invoke(o);
                    Pram pram = new Pram(fieldName, getValue);
                    list.add(pram);
                } else {
                    String fieldName = toTableString(fName);
                    Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                    Object getValue = get.invoke(o);
                    Pram pram = new Pram(fieldName, getValue);
                    list.add(pram);
                }
            }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取某个实体的字段集合
     *
     * @param po 实体
     * @return 该实体是所有字段
     */
    public List<Pram> getPramList(T po) {
        List<Pram> list = new ArrayList<>();
        Class<? extends CoreModel> thisClass = po.getClass();
        Field[] fields = thisClass.getDeclaredFields();
        try {
            for (Field f : fields) {
                String fName = f.getName();
                //判断是否是boolean类型
                String getf = SqlStringZf.GET_STR;
                if (!SqlStringZf.CheckFieldFilter(f)) {
                    //字段类型
                    String fieldType = f.getGenericType().toString();
                    if (fieldType.contains(SqlStringZf.BOOLEAN_STR) || fieldType.indexOf(SqlStringZf.BOOLEAN_STR_D) != -1) {
                        getf = SqlStringZf.IS_STR;
                    }
                    if (f.isAnnotationPresent(FieldName.class)) {
                        String fieldName = f.getAnnotation(FieldName.class).name();
                        Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                        Object getValue = get.invoke(po);
                        if (getValue == null || getValue.equals(SqlStringZf.NULL_STR)) {
                            String calssName = f.getType().getName();
                            if (calssName instanceof String) {
                                Pram pram = new Pram(fieldName, SqlStringZf.NULL_STR);
                                list.add(pram);
                            }
                        } else {
                            Pram pram = new Pram(fieldName, getValue);
                            list.add(pram);
                        }
                    } else {
                        String fieldName = toTableString(fName);
                        Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                        Object getValue = get.invoke(po);
                        if (getValue == null || getValue.equals(SqlStringZf.NULL_STR)) {
                            String calssName = f.getType().getName();
                            if (calssName instanceof String) {
                                Pram pram = new Pram(fieldName, SqlStringZf.NULL_STR);
                                list.add(pram);
                            }
                        } else {
                            Pram pram = new Pram(fieldName, getValue);
                            list.add(pram);
                        }
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过Class获取生成对应Sql查询的字段
     *
     * @param po 要获取字段的类
     * @return List<Pram> selectSqlParms;字段集合
     */
    public List<Pram> getPramListOfSelect(Class<T> po) {
        List<Pram> list = new ArrayList<>();
        try {
            //获取list集合
            list = publicSetParamList(po, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过Class获取生成对应Sql查询的字段
     *
     * @param po
     * @return
     */
    public List<Pram> getPramListByBean(Class<T> po) {
        List<Pram> list = new ArrayList<>();
        try {
            //获取list集合
            list = publicSetParamList(po, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过Class获取生成对应Sql查询的字段
     *
     * @param po
     * @return
     */
    public List<Pram> getPramListByBeanOfSelect(Class<T> po) {
        List<Pram> list = new ArrayList<>();
        try {
            //获取list集合
            list = publicSetParamList(po, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取表的主键
     *
     * 警告：实体bean中第一个字段必须是主键
     *
     * @param po 实体
     * @return 第一个字段
     */
    public String getPkName(Class<T> po) {
        String first_field = getIdName(po);
        String database_id = SqlStringZf.DEFAULT_ID;
        if (null != first_field && !first_field.equals(SqlStringZf.NULL_STR)) {
            //骆驼峰该字段
            database_id = toTableString(first_field);
        }
        return database_id;
    }

    /**
     * 获取主键实体类中的字段
     *
     * 警告：实体bean中第一个字段必须是主键
     *
     * @param po
     * @return
     */
    public String getIdName(Class<T> po) {
        //获取实体集合中的第一个
        Class<?> thisClass = po;
        //定义局部变量
        String result = null;

        try {
            Field[] fields = thisClass.getDeclaredFields();
            //空值判断
            if (null == fields || fields.equals(SqlStringZf.NULL_STR) || fields.length < 0) {
                return null;
            } else {
                int i = 0;
                for (Field f : fields) {
                    //只循环一次
                    if (i < 1) {
                        //取得第一个字段
                        if (f.isAnnotationPresent(FieldName.class)) {
                            result = f.getAnnotation(FieldName.class).name();
                        } else {
                            result = f.getName();
                        }
                    }
                    i = i + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取Sql表名
     *
     * @param po
     * @return 返回表名
     */
    public String getTableName(Class<T> po) {
        String tname = SqlStringZf.NULL_STR;
        try {
            //取得前缀
            tname = toTableString(po.getSimpleName());
            String poName = tname.substring(tname.length() - 2, tname.length());
            if ("po".equals(poName)) {
                tname = tname.substring(0, tname.length() - 3);
            } else {
                tname = SqlStringZf.getTablePrefix() + SqlStringZf.DEFAULT_ID_LINK + tname;
                //表名书检查
                Map<String, String> filtermap = SqlStringZf.getFilterMap();
                if (null != filtermap && filtermap.size() > 0) {
                    for (Map.Entry<String, String> entry : filtermap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        //判断
                        if (null != key && key.equals(tname)) {
                            tname = tname + value;
                        }
                    }
                } else {
                    tname = SqlStringZf.getTablePrefix() + SqlStringZf.DEFAULT_ID_LINK + tname;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tname;
    }

    /**
     * 获取Sql字段名
     *
     * @param po
     * @return
     */
    public String getTableNameByBean(Class<T> po) {
        String tname = SqlStringZf.NULL_STR;
        try {
            tname = toTableString(po.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tname;
    }

    /**
     * 获取字段的值
     *
     * @param po
     * @param fileName
     * @return
     */
    public static <T> Serializable getFileValue(Class<T> po, String fileName) {
        try {
            Method method = po.getMethod(SqlStringZf.GET_STR + fileName.substring(0, 1).toUpperCase() + fileName.substring(1));
            Object o = po.newInstance();
            Object invoke = method.invoke(o);
            return null == invoke ? null : (Serializable) invoke;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字段获取值
     *
     * @param po
     * @param fileName
     * @return
     */
    public Serializable getFileValue(CoreModel po, String fileName) {
        try {
            Class<? extends CoreModel> cla = po.getClass();

            Method method = cla.getMethod(SqlStringZf.GET_STR + fileName.substring(0, 1).toUpperCase() + fileName.substring(1));
            Object o = po;
            Object invoke = method.invoke(o);
            return null == invoke ? null : (Serializable) invoke;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 实体设置值
     *
     * @param po 实体
     * @param fileName 实体成员
     * @param fileValue 实体值
     * @return
     */
    public static boolean setFileValue(CoreModel po, String fileName, Serializable fileValue) {
        Class<? extends CoreModel> thisClass = po.getClass();
        try {
            String setget = SqlStringZf.SET_STR + fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
            if (fileName.contains(SqlStringZf.DEFAULT_ID_LINK)) {
                setget = SqlStringZf.SET_STR;
                String[] fieldPart = fileName.split(SqlStringZf.DEFAULT_ID_LINK);
                for (String s : fieldPart) {
                    setget += s.substring(0, 1).toUpperCase() + s.substring(1);
                }
            }

            if (null != fileValue) {
                try {
                    Method method = null;
                    if (fileValue instanceof String) {
                        try {
                            method = thisClass.getMethod(setget, String.class);
                        } catch (Exception e) {
                            method = CoreModel.class.getMethod(setget, String.class);
                        }
                    } else if (fileValue instanceof Integer) {
                        if (fileName.contains(SqlStringZf.DEFAULT_ID_LINK)) {
                            String[] fieldPart = fileName.split(SqlStringZf.DEFAULT_ID_LINK);
                            fileName = SqlStringZf.NULL_STR;
                            for (int i = 0; i < fieldPart.length; i++) {
                                String s = fieldPart[i];
                                if (i > 0) {
                                    fileName += s.substring(0, 1).toUpperCase() + s.substring(1);
                                } else {
                                    fileName += s;
                                }
                            }
                        }
                        //验证field 是否属于该实体类
                        Field field = null;
                        try {
                            field = thisClass.getDeclaredField(fileName);
                        } catch (Exception e) {
                            Logger.getLogger(SqlUtil.class.getName()).log(Level.SEVERE, null, e);
                            field = CoreModel.class.getDeclaredField(fileName);
                        }
                        String calssName = field.getType().getName();
                        if (calssName.equals("int") || calssName.equals("java.lang.Integer")) {
                            if (Integer.MAX_VALUE > new Integer(SqlStringZf.NULL_STR + fileValue)) {
                                method = thisClass.getMethod(setget, field.getType());
                            }
                        } else if (calssName instanceof String) {
                            method = thisClass.getMethod(setget, field.getType());
                        } else {
                            method = thisClass.getMethod(setget, field.getType());
                        }
                    } else if (fileValue instanceof Long) {
                        method = thisClass.getMethod(setget, Long.TYPE);
                    } else if (fileValue instanceof Double) {
                        method = thisClass.getMethod(setget, Double.TYPE);
                    } else if (fileValue instanceof Short) {
                        method = thisClass.getMethod(setget, Short.TYPE);
                    } else {
                        method = thisClass.getMethod(setget, Integer.TYPE);
                    }

                    method.invoke(po, fileValue);
                } catch (NoSuchMethodException e) {
                    // TODO: handle exception
                    try {
                        Method method = thisClass.getMethod(setget, Boolean.TYPE);
                        if (fileValue instanceof Integer) {
                            method.invoke(po, (int) fileValue > 0 ? true : false);
                        }
                    } catch (NoSuchMethodException e2) {
                        // TODO: handle exception
                        e2.printStackTrace();
                    }
                } catch (NoSuchFieldException ex) {
                    Logger.getLogger(SqlUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return true;
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // TODO Auto-generated catch block
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 前段列名称与数据库对应
     *
     * @param coloum
     * @return
     */
    public static String toColoumString(String coloum) {
        if (null == coloum) {
            return null;
        }
        SqlUtil sqlutil = new SqlUtil();
        return sqlutil.coreToString(coloum);
    }

    /**
     * 核心骆驼峰风格转换
     *
     * @param str 字符串
     * @return 最终结果以_连接
     */
    public String coreToString(String str) {
        if (null == str) {
            return null;
        }
        String result = "";
        try {
            result = str.substring(0, 1).toLowerCase();
            for (int i = 1; i < str.length(); i++) {
                //骆驼峰命名
                if (!Character.isLowerCase(str.charAt(i))) {
                    result += SqlStringZf.DEFAULT_ID_LINK;
                }
                result += str.substring(i, i + 1);
            }
            result = result.toLowerCase();
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 驼峰标识转下划线标识
     *
     * @param text
     * @return 如:user_detail
     */
    public String toTableString(String text) {
        return coreToString(text);
    }

    public static void main(String[] args) {
        //骆驼峰方法测试
        SqlUtil sqlutil = new SqlUtil();
        String toTableString = sqlutil.coreToString("asdfasdfasdfASDFASDFASDFASDFS斯蒂芬森");
        System.out.println(toTableString);
    }
}
