package com.bufan.ssmredis.util;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * 检查帮助类
 *
 * @author Administrator
 *
 */
public class CheckUtil {

    private static Logger logger = Logger.getRootLogger();

    /**
     * 检测分页数据
     *
     * @param pageNumStr
     * @param limit
     * @return
     */
    public static String checkPageUtil(String pageNumStr, String limit) {
        if (!CheckUtil.checkStr(limit)) {
            if (!CheckUtil.checkStr(pageNumStr)) {
                return "1";
            } else {
                if (Integer.parseInt(pageNumStr) < 0) {
                    return "1";
                } else {
                    return pageNumStr;
                }
            }
        } else {
            if (!CheckUtil.checkStr(limit)) {
                return "15";
            } else {
                if (Integer.parseInt(limit) < 0) {
                    return "15";
                } else {
                    return limit;
                }
            }
        }
    }

    /**
     * 检查字符串(多个)
     *
     * @param strArr 字符串数组
     * @return
     */
    public static boolean checkStr(String... strArr) {
        if (null == strArr) {
            return false;
        }
        if (strArr.length == 0) {
            return false;
        }
        for (String s : strArr) {
            if (!checkStr(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查集合
     *
     * @param list 集合
     * @return
     */
    public static boolean checkList(List<?> list) {
        if (null == list) {
            return false;
        }
        return list.size() > 0;
    }

    /**
     * 检查map集合(Map<String, Object>)
     *
     * @param m
     * @return
     */
    public static boolean checkMap(Map<String, Object> m) {
        if (null == m) {
            return false;
        }
        return m.size() > 0;
    }

    /**
     * 检查map集合(Map<String, String>)
     *
     * @param m
     * @return
     */
    public static boolean checkMapStr(Map<String, String> m) {
        if (null == m) {
            return false;
        }
        return m.size() > 0;
    }

    /**
     * 检查大于0的数字
     *
     * @param num 整数型数字
     * @return
     */
    public static boolean checkNum(Integer num) {
        if (num == null) {
            return false;
        }
        return num > 0;
    }

    /**
     * 检查大于0的数字
     *
     * @param num 浮点型数字
     * @return
     */
    public static boolean checkNum(Float num) {
        if (isNull(num)) {
            return false;
        }
        return num > 0;
    }

    /**
     * 检查大于0的数字
     *
     * @param num 浮点数数字
     * @return
     */
    public static boolean checkNum(Double num) {
        if (num == null) {
            return false;
        }
        return num > 0;
    }

    /**
     * 检测为空
     *
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 检测为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     * 为空校验
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            if (((Map) obj).size() <= 0) {
                return true;
            } else {
                return ((Map) obj).isEmpty();
            }
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 检测object
     *
     * @param obj
     * @return
     */
    public static boolean checkObj(Object obj) {
        try {
            if (null == obj) {
                return false;
            }
            if ("".equals(obj)) {
                return false;
            }
            return "" != obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查object数组
     *
     * @param arr
     * @return
     */
    public static boolean checkArr(Object[] arr) {
        try {
            if (null == arr) {
                return false;
            }
            if (0 == arr.length) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     *
     * 字符串数组以指定符号间隔拼成字符串
     *
     * @param arr    字符串数组
     * @param symbol 符号
     * @return
     */
    public static String arr2String(String[] arr, String symbol) {
        if (!checkArr(arr)) {
            return "";
        }
        String result = "";
        try {
            for (String s : arr) {
                result += s + symbol;
            }
            if (CheckUtil.checkStr(result)) {
                result = result.substring(0, result.lastIndexOf(symbol));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }

    /**
     * 判断是否为布尔值
     *
     * @param str
     * @return
     */
    public static boolean isBoolean(String str) {
        if (!checkStr(str)) {
            return false;
        }
        try {
            Boolean.getBoolean(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断是否为integer
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (!checkStr(str)) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

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
     * 判断是否为double值
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        if (!checkStr(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断是否为float值
     *
     * @param str
     * @return
     */
    public static boolean isFloat(String str) {
        if (!checkStr(str)) {
            return false;
        }
        try {
            Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断是否包含空格，换行符，制表符
     *
     * @param str
     * @return
     */
    public static Boolean isIllegalChar(String str) {
        if (!checkStr(str)) {
            return null;
        }
        Matcher m = null;
        boolean result = false;
        try {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            m = p.matcher(str);
            result = m.matches();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
