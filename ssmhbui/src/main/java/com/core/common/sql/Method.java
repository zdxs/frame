package com.core.common.sql;

import java.io.Serializable;
import java.util.Map;

/**
 * 构建where条件
 *
 * @author xiaosun
 */
public class Method {

    /**
     * where重写
     *
     * @param file
     * @param where 相当于sql语句中的："=";"<>";"like"; ">";"<";"in";"="; @par am value
     * @return
     */
    public static WherePrams where(String file, String where, Serializable value) {
        return new WherePrams(file, where, value);
    }

    /**
     * 构建where条件
     *
     * @param file 字段
     * @param c sql连接字符
     * @param value 值
     * @return WherePrams
     */
    public static WherePrams where(String file, SqlZf c, Serializable value) {
        String where = SqlZf.getSqlWhere(c);
        return where(file, where, value);
    }

    /**
     * 所有分页 本方法只具有分页，没有条件查询
     *
     * @param preIndex 第几页
     * @param pageSize 每页数量
     * @return 返回where条件
     */
    public static WherePrams Alllimit(Integer preIndex, Integer pageSize) {
        WherePrams whereparams = new WherePrams();
        return whereparams.mysqllimit(preIndex, pageSize);
    }

    /**
     * 多字段单表查询and连接
     *
     * @param preIndex 第几页
     * @param pageSize 每页条数
     * @param c 字符
     * @param value 值
     * @param params 字段
     * @return
     */
    public static WherePrams ManyParmOneValue(Integer preIndex, Integer pageSize, SqlZf c, String value, Object... params) {
        WherePrams whereparams = new WherePrams();
        return whereparams.MysqlManyParmOneValueLimitAnd(preIndex, pageSize, c, value, params);
    }

    /**
     * 根据多个字段，多个值查询所有数据，连接条件是or
     *
     * @param c sql连接字符
     * @param paramvalue key:数据库字段，value:值
     * @return
     */
    public static WherePrams ManyPrmManyValueOrList(SqlZf c, Map<String, String> paramvalue) {
        WherePrams whereparams = new WherePrams();
        return whereparams.ManyPrmManyValueOrList(c, paramvalue);
    }

    /**
     * 根据多个字段，多个值查询所有数据
     *
     * @param c sql连接字符
     * @param paramvalue key:数据库字段，value:值
     * @return
     */
    public static WherePrams ManyPrmManyValueAndList(SqlZf c, Map<String, String> paramvalue) {
        WherePrams whereparams = new WherePrams();
        return whereparams.ManyPrmManyValueAndList(c, paramvalue);
    }

    /**
     * 多参数，多个值 and
     *
     * @param preIndex 第几页
     * @param pageSize 每页条数
     * @param c sql连接字符
     * @param paramvalue 存放方式 String1:表字段，String2：值
     * @return
     */
    public static WherePrams ManyParmManyValueAnd(Integer preIndex, Integer pageSize, SqlZf c, Map<String, String> paramvalue) {
        WherePrams whereparams = new WherePrams();
        return whereparams.MysqlManyParmManyValueAnd(preIndex, pageSize, c, paramvalue);
    }

    /**
     * 多参数，多个值 or
     *
     * @param preIndex 第几页
     * @param pageSize 每页条数
     * @param c sql连接字符
     * @param paramvalue 存放方式 String1:表字段，String2：值
     * @return
     */
    public static WherePrams ManyParmManyValueOr(Integer preIndex, Integer pageSize, SqlZf c, Map<String, String> paramvalue) {
        WherePrams whereparams = new WherePrams();
        return whereparams.MysqlManyParmManyValueOr(preIndex, pageSize, c, paramvalue);
    }

    /**
     * 多字段单表查询or连接
     *
     * @param preIndex 第几页
     * @param pageSize 每页条数
     * @param c sql连接字符
     * @param value 值
     * @param params 字段
     * @return
     */
    public static WherePrams ManyParmOneValueOr(Integer preIndex, Integer pageSize, SqlZf c, String value, Object... params) {
        WherePrams whereparams = new WherePrams();
        return whereparams.MysqlManyParmOneValueLimitOr(preIndex, pageSize, c, value, params);
    }

    /**
     * 获得所有数量，没有分页参数
     *
     * @param c sql连接字符
     * @param paramvalue 存放方式 String1:表字段，String2：值
     * @return
     */
    public static WherePrams CountManyParmManyValue(SqlZf c, Map<String, String> paramvalue) {
        WherePrams whereparams = new WherePrams();
        return whereparams.CountManyParmManyValue(c, paramvalue);
    }

    /**
     * 构建默认查询sql
     *
     * @return
     */
    public static WherePrams createDefault() {
        return new WherePrams(null, null, null);
    }

}
