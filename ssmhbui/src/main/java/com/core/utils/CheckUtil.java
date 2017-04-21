package com.core.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;

/**
 * 检查帮助类
 *
 * @author Administrator
 *
 */
public class CheckUtil {

    /**
     * 获取对象的类型(如Integer,String,Float)
     *
     * @param obj
     * @return
     */
    public static String typeOf(Object obj) {
        if (null == obj) {
            return null;
        }
        String type = null;
        try {
            if (obj instanceof Integer) {
                type = Integer.class.getSimpleName();
            } else if (obj instanceof String) {
                type = String.class.getSimpleName();
            } else if (obj instanceof Double) {
                type = Double.class.getSimpleName();
            } else if (obj instanceof Float) {
                type = Float.class.getSimpleName();
            } else if (obj instanceof Long) {
                type = Long.class.getSimpleName();
            } else if (obj instanceof Boolean) {
                type = Boolean.class.getSimpleName();
            } else if (obj instanceof Date) {
                type = Date.class.getSimpleName();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return type;
    }

    /**
     * 为空校验
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(CheckUtil.isEmpty("s"));
    }
}
