package com.core.common.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 泛型工具类
 *
 * @author xiaosun
 *
 * @since v1.0
 */
public class GenericsUtils {

    //全局变量
    protected static Logger logger = LoggerFactory.getLogger(GenericsUtils.class);

    /**
     * 通过反射,获得指定类的父类的泛型参数的实际类型.
     *
     * 如<Buyer>DaoSupport</Buyer>
     *
     * @param clazz clazz 需要反射的类,该类必须继承范型父类
     * @param index 泛型参数所在索引,从0开始.
     * @return 范型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {
        
        try {
            // 得到泛型父类
            Type genType = clazz.getGenericSuperclass();

            // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
            if (!(genType instanceof ParameterizedType)) {

                return Object.class;
            }

            // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如BuyerServiceBean extends
            // DaoSupport<Buyer,Contact>就返回Buyer和Contact类型
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index >= params.length || index < 0) {
                throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
            }
            if (!(params[index] instanceof Class)) {

                return Object.class;
            }
            return (Class<?>) params[index];
        } catch(RuntimeException re){
            re.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射,获得指定类的父类的第一个泛型参数的实际类型.
     *
     * 如<Buyer>DaoSupport</Buyer>
     *
     * @param clazz clazz 需要反射的类,该类必须继承泛型父类
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz) {

        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得方法返回值泛型参数的实际类型.
     *
     * 如: public Map<String, Buyer> getNames(){}
     *
     * @param method 方法(Method)
     * @param index 泛型参数所在索引,从0开始(int).
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getMethodGenericReturnType(Method method, int index) {
        
        Type returnType = method.getGenericReturnType();

        if (returnType instanceof ParameterizedType) {

            ParameterizedType type = (ParameterizedType) returnType;
            Type[] typeArguments = type.getActualTypeArguments();

            if (index >= typeArguments.length || index < 0) {

                throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
            }
            return (Class<?>) typeArguments[index];
        }

        return Object.class;
    }

    /**
     * 通过反射,获得方法返回值第一个泛型参数的实际类型.
     *
     * 如: public Map<String, Buyer> getNames(){}
     *
     * @param method 方法(Method)
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getMethodGenericReturnType(Method method) {

        return getMethodGenericReturnType(method, 0);
    }

    /**
     * 通过反射,获得方法输入参数第index个输入参数的所有泛型参数的实际类型.
     *
     * 如: public void add(Map<String,Buyer> maps, List<String> names){}
     *
     * @param method 方法(Method)
     * @param index 第几个输入参数
     * @return 输入参数的泛型参数的实际类型集合, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回空集合
     */
    public static List<Class<?>> getMethodGenericParameterTypes(Method method, int index) {

        //定义集合
        List<Class<?>> results;

        //初始化变量
        results = new ArrayList<>();

        //异常捕获
        try {
            Type[] genericParameterTypes = method.getGenericParameterTypes();

            if (index >= genericParameterTypes.length || index < 0) {

                throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
            }
            Type genericParameterType = genericParameterTypes[index];

            //判断其左边对象是否为其右边类的实例，返回boolean类型的数据
            if (genericParameterType instanceof ParameterizedType) {

                ParameterizedType aType = (ParameterizedType) genericParameterType;
                Type[] parameterArgTypes = aType.getActualTypeArguments();
                for (Type parameterArgType : parameterArgTypes) {
                    Class<?> parameterArgClass = (Class<?>) parameterArgType;
                    results.add(parameterArgClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * 通过反射,获得方法输入参数第一个输入参数的所有泛型参数的实际类型. 如: public void
     * add(Map<String, Buyer>maps, List<String> names){}
     *
     * @param method 方法(Method)
     * @return 输入参数的泛型参数的实际类型集合, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回空集合
     */
    public static List<Class<?>> getMethodGenericParameterTypes(Method method) {

        return getMethodGenericParameterTypes(method, 0);
    }

    /**
     * 通过反射,获得Field泛型参数的实际类型.
     *
     * 如: public Map<String, Buyer> names;
     *
     * @param field 字段(Field)
     * @param index 泛型参数所在索引,从0开始.(int)
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getFieldGenericType(Field field, int index) {

        /*
         返回一个 Type 对象，它表示此 Field 对象所表示字段的声明类型。
         如果 Type 是一个参数化类型，则返回的 Type 对象必须准确地反映源代码中使用的实际类型参数。
         如果底层字段的类型是一个类型变量或者是一个参数化类型，则创建它。否则将解析它。
         */
        Type genericFieldType = field.getGenericType();

        if (genericFieldType instanceof ParameterizedType) {

            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            if (index >= fieldArgTypes.length || index < 0) {

                throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
            }
            return (Class<?>) fieldArgTypes[index];
        }

        return Object.class;
    }

    /**
     * 通过反射,获得Field泛型参数的实际类型.
     *
     * 如: public Map<String, Buyer> names;
     *
     * @param field 字段(Field)
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getFieldGenericType(Field field) {

        return getFieldGenericType(field, 0);
    }
}
