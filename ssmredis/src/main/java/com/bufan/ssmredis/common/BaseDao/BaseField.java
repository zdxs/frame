/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.common.BaseDao;

import com.bufan.ssmredis.bean.DemoTest;
import com.bufan.ssmredis.util.Constants;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类目的获取代理类的字段 及其方法
 *
 * @author xiaosun
 * @param <T>
 */
public class BaseField<T extends AgentBean> {

    /**
     * 设置实体值
     *
     * @param listParm 该类的所有属性值
     * @param obj 该实体类
     * @param fieldName 需要赋值的实体类
     * @param fieldValue
     * @return
     */
    private T setFieldValue(List<Parm> listParm, Class<T> obj, String fieldName, String fieldValue) {
        T t = null;
        if (null == listParm || null == fieldName || listParm.size() <= 0 || null == obj) {
            return null;
        }
        try {
            for (Parm parm : listParm) {
                //将大写字符转换为小写
                if ((Constants.KEY_BEAN_SET + fieldName).toLowerCase().equals(parm.getFile().toLowerCase())) {
                    t = obj.newInstance();
                    BaseField.setFileValue(t, parm.getFile(), fieldValue);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 实体设置值
     *
     * @param agentbean
     * @param funSetGet 实体方法
     * @param fileValue 实体值
     * @return true|false
     */
    public static boolean setFileValue(AgentBean agentbean, String funSetGet, Serializable fileValue) {
        if (null == agentbean || null == funSetGet || null == fileValue) {
            return false;
        }
        boolean flog = false;
        try {
            Method method = null;
            Class<? extends AgentBean> thisClass = agentbean.getClass();
            if (fileValue instanceof String) {
                method = thisClass.getMethod(funSetGet, String.class);
            } else if (fileValue instanceof Long) {
                method = thisClass.getMethod(funSetGet, Long.TYPE);
            } else if (fileValue instanceof Double) {
                method = thisClass.getMethod(funSetGet, Double.TYPE);
            } else if (fileValue instanceof Short) {
                method = thisClass.getMethod(funSetGet, Short.TYPE);
            } else {
                method = thisClass.getMethod(funSetGet, Integer.TYPE);
            }
            method.invoke(agentbean, fileValue);
            flog = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flog;
    }

    /**
     * 获取该类中的属性值
     *
     * @param listParm
     * @param fieldName 字段名称
     * @return
     */
    public static Object getFieldValue(List<Parm> listParm, String fieldName) {
        Object result = null;
        if (null == listParm || null == fieldName || listParm.size() <= 0) {
            return result;
        }
        try {
            for (Parm parm : listParm) {
                //将大写字符转换为小写
                if (fieldName.toLowerCase().equals(parm.getFile().toLowerCase())) {
                    result = parm.getValue();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取某个类的实体名称以及值
     *
     * @param obj 实体
     * @return
     */
    public static List<Parm> getClassParm(Class obj) {
        List<Parm> listParm = new ArrayList<>();
        if (null == obj) {
            return null;
        }
        try {
//            newInstance: 弱类型。低效率。只能调用无参构造。
//            new: 强类型。相对高效。能调用任何public构造。
            Object o = obj.newInstance();
            //获取字段
            Field[] fields = obj.getDeclaredFields();
            for (Field field : fields) {
                String fName = field.getName();
                Method get = obj.getMethod(Constants.KEY_BEAN_GET + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                Object getValue = get.invoke(o);
                Parm pram = new Parm(fName, getValue);
                listParm.add(pram);
            }
            //获取所有方法
            Method[] methods = obj.getDeclaredMethods();
            for (Method methed : methods) {
                if (!methed.getName().contains(Constants.KEY_BEAN_SET)) {
                    Parm pram = new Parm(methed.getName(), methed.invoke(o));
                    listParm.add(pram);
                } else {
                    Parm pram = new Parm(methed.getName(), null);
                    listParm.add(pram);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listParm;
    }

    public static void main(String[] args) {
        List<Parm> listParm = BaseField.getClassParm(DemoTest.class);
        for (Parm en : listParm) {
            System.out.println(en.getFile());
            System.out.println(en.getValue());
        }
        System.out.println(BaseField.getFieldValue(listParm, "pkField"));

        DemoTest demotest = new DemoTest();
        demotest.setName("abcd");
        BaseField.setFileValue(demotest, "setId", "123");
        System.out.println(demotest.getId());
        System.out.println(demotest.getName());

        BaseField bf = new BaseField();
        DemoTest demotests = (DemoTest) bf.setFieldValue(listParm, demotest.getClass(), "name", "name");
        System.out.println(demotests.getName());
        System.out.println(demotests.getId());
    }
}
