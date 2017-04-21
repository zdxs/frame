package com.core.common.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 构建条件查询
 *
 * @author xiaosun
 */
public class WherePrams {

    private String pram;            //参数

    private String limit;           //分页

    private String orderBy;         //排序

    private List<Serializable> parms = new ArrayList<>();

    private List<Serializable> limitParms = new ArrayList<>();

    public List<Serializable> getParms() {
        return parms;
    }

    public void setParms(List<Serializable> parms) {
        this.parms = parms;
    }

    public WherePrams() {
        super();
    }

    /**
     * 排序初始化
     *
     * @param file 字段
     * @param where 条件
     * @param value 值
     * @return
     */
    public WherePrams orderByFirst(String file, String where, Serializable value) {
        if ("desc".equals(where)) {
            this.orderBy = " order by " + file + where + " " + value + " ";
        } else {
            this.orderBy = " order by " + file + where + " " + value + " ";
        }
        return this;
    }

    public WherePrams orderByFirst(String file, SqlZf c, Serializable value) {
        String where = SqlZf.getSqlWhere(c);
        return orderByFirst(file, where, value);
    }

    /**
     * 条件初始化
     *
     * @param file
     * @param where
     * @param value
     * @return
     */
    public WherePrams wherefirst(String file, String where, Serializable value) {
        if (null == value) {

            if (where.equals("=")) {
                where = " is ";
            } else {
                where = " not ";
            }
            this.pram = " where " + file + where + "null";
        } else {
            if ("like".equals(where)) {
                this.pram = " where " + file + where + " '%" + value + "%'";
            } else {
                this.pram = " where " + file + where + " '" + value + "'";
            }
        }
        return this;
    }

    public WherePrams wherefirst(String file, SqlZf c, Serializable value) {
        String where = SqlZf.getSqlWhere(c);
        return and(file, where, value);
    }

    /**
     *
     * @param file
     * @param where
     * @param value
     */
    public WherePrams(String file, String where, Serializable value) {

        if (null == file && null == where && value == where) {
            return;
        }

        if (null == value) {

            if (where.equals("=")) {
                where = " is ";
            } else {
                where = " not ";
            }
            this.pram = " where " + file + where + "null";
        } else {
            if ("like".equals(where)) {
                this.pram = " where " + file + " " + where + " '%" + value + "%'";
            } else {
                this.pram = " where " + file + " " + where + " '" + value + "'";
            }
        }

    }

    /**
     * and条件
     *
     * @param file
     * @param where
     * @param value
     * @return
     */
    public WherePrams and(String file, String where, Serializable value) {
        if (null == value) {
            if (where.equals("=")) {
                where = " is ";
            } else {
                where = " not ";
            }
            this.pram = " and " + file + where + "null";
        } else {
            if ("like".equals(where)) {
                this.pram += " and " + file + " " + where + " '%" + value + "%'";
            } else {
                this.pram += " and " + file + " " + where + " '" + value + "'";
            }
        }
        return this;
    }

    public WherePrams and(String file, SqlZf c, Serializable value) {
        String where = SqlZf.getSqlWhere(c);
        return and(file, where, value);
    }

    /**
     * or条件
     *
     * @param file
     * @param where
     * @param value
     * @return
     */
    public WherePrams or(String file, String where, Serializable value) {

        if (null == value) {
            if (where.equals("=")) {
                where = " is ";
            } else {
                where = " not ";
            }
            this.pram = " or " + file + where + "null";
        } else {
            if ("like".equals(where)) {
                this.pram += " or " + file + where + " '%" + value + "%'";
            } else {
                this.pram += " or " + file + where + " '" + value + "'";
            }
        }

        return this;
    }

    public WherePrams or(String file, SqlZf c, Serializable value) {
        String where = SqlZf.getSqlWhere(c);
        return or(file, where, value);
    }

    /**
     * 分页处理
     *
     * @param startNum 1 页码
     * @param length 20每页条数
     * @return
     */
    public WherePrams limit(int startNum, int length) {
        Integer startnumnew = (startNum < 2) ? 0 : ((startNum - 1) * length);
        this.limit = " limit " + startnumnew + " , " + length + " ";
        return this;
    }

    /**
     * 分页处理
     *
     * @param startNum 0
     * @param length 20每页条数
     * @return
     */
    public WherePrams limitnew(int startNum, int length) {
        this.limit = " limit " + startNum + " , " + length + " ";
        return this;
    }

    /**
     * 多个参数，多个值查询所有数据 or条件
     *
     * @param c
     * @param paramvalue
     * @return
     */
    public WherePrams ManyPrmManyValueOrList(SqlZf c, Map<String, String> paramvalue) {
        //where链接字符
        String where = SqlZf.getSqlWhere(c);
        if (paramvalue != null) {
            //参数
            int i = 0;
            for (Map.Entry<String, String> entry : paramvalue.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                //第一次循环需要添加where
                if (i == 0) {
                    wherefirst(key.toString(), where, value);
                } else {
                    //否则执行and连接
                    or(key.toString(), where, value);
                }
                i++;
            }
        }
        return this;
    }

    /**
     * 多个参数，多个值查询所有数据
     *
     * @param c
     * @param paramvalue
     * @return
     */
    public WherePrams ManyPrmManyValueAndList(SqlZf c, Map<String, String> paramvalue) {
        //where链接字符
        String where = SqlZf.getSqlWhere(c);
        if (paramvalue != null) {
            //参数
            int i = 0;
            for (Map.Entry<String, String> entry : paramvalue.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                //第一次循环需要添加where
                if (i == 0) {
                    wherefirst(key.toString(), where, value);
                } else {
                    //否则执行and连接
                    and(key.toString(), where, value);
                }
                i++;
            }
        }
        return this;
    }

    /**
     * 多字段多值查询
     *
     * @param startNum 第几页
     * @param length 每页条数
     * @param c 字符
     * @param paramvalue 存放方式 String1:表字段，String2：值
     * @return
     */
    public WherePrams MysqlManyParmManyValueAnd(int startNum, int length, SqlZf c, Map<String, String> paramvalue) {
        //分页
        Integer startnumnew = (startNum < 2) ? 0 : ((startNum - 1) * length);
        this.limit = " limit " + startnumnew + " , " + length + " ";
        //where链接字符
        String where = SqlZf.getSqlWhere(c);
        if (paramvalue != null) {
            //参数
            int i = 0;
            for (Map.Entry<String, String> entry : paramvalue.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                //第一次循环需要添加where
                if (i == 0) {
                    wherefirst(key.toString(), where, value);
                } else {
                    //否则执行and连接
                    and(key.toString(), where, value);
                }
                i++;
            }
        }

        return this;
    }

    /**
     * 获得所有数量，没有分页参数
     *
     * @param c sql字符
     * @param paramvalue 存放方式 String1:表字段，String2：值
     * @return
     */
    public WherePrams CountManyParmManyValue(SqlZf c, Map<String, String> paramvalue) {
        //where链接字符
        String where = SqlZf.getSqlWhere(c);
        if (paramvalue != null) {
            //参数
            int i = 0;
            for (Map.Entry<String, String> entry : paramvalue.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                //第一次循环需要添加where
                if (i == 0) {
                    wherefirst(key.toString(), where, value);
                } else {
                    //否则执行and连接
                    or(key.toString(), where, value);
                }
                i++;
            }
        }
        return this;
    }

    /**
     * 多字段多值查询
     *
     * @param startNum 第几页
     * @param length 每页条数
     * @param c 字符
     * @param paramvalue 存放方式 String1:表字段，String2：值
     * @return
     */
    public WherePrams MysqlManyParmManyValueOr(int startNum, int length, SqlZf c, Map<String, String> paramvalue) {
        //分页
        Integer startnumnew = (startNum < 2) ? 0 : ((startNum - 1) * length);
        this.limit = " limit " + startnumnew + " , " + length + " ";
        //where链接字符
        String where = SqlZf.getSqlWhere(c);
        if (paramvalue != null) {
            //参数
            int i = 0;
            for (Map.Entry<String, String> entry : paramvalue.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                //第一次循环需要添加where
                if (i == 0) {
                    wherefirst(key.toString(), where, value);
                } else {
                    //否则执行and连接
                    or(key.toString(), where, value);
                }
                i++;
            }
        }
        return this;
    }

    /**
     * 多字段单表查询
     *
     * @param startNum 第几页
     * @param length 每页条数
     * @param c 字符
     * @param value 值
     * @param params 字段
     * @return
     */
    public WherePrams MysqlManyParmOneValueLimitAnd(int startNum, int length, SqlZf c, String value, Object... params) {
        //分页
        Integer startnumnew = (startNum < 2) ? 0 : ((startNum - 1) * length);
        this.limit = " limit " + startnumnew + " , " + length + " ";
        if (params != null) {
            //where链接字符
            String where = SqlZf.getSqlWhere(c);
            //参数
            for (int i = 0; i < params.length; ++i) {
                //第一次循环需要添加where
                if (i == 0) {
                    wherefirst(params[i].toString(), where, value);
                } else {
                    //否则执行and连接
                    and(params[i].toString(), where, value);
                }
            }
        }
        return this;
    }

    /**
     * 多字段单表查询
     *
     * @param startNum 第几页
     * @param length 每页条数
     * @param c 字符
     * @param value 值
     * @param params 字段
     * @return
     */
    public WherePrams MysqlManyParmOneValueLimitOr(int startNum, int length, SqlZf c, String value, Object... params) {
        //分页
        Integer startnumnew = (startNum < 2) ? 0 : ((startNum - 1) * length);
        this.limit = " limit " + startnumnew + " , " + length + " ";
        if (params != null) {
            //where链接字符
            String where = SqlZf.getSqlWhere(c);
            //参数
            for (int i = 0; i < params.length; ++i) {
                //第一次循环需要添加where
                if (i == 0) {
                    wherefirst(params[i].toString(), where, value);
                } else {
                    //否则执行and连接
                    or(params[i].toString(), where, value);
                }
            }
        }
        return this;
    }

    /**
     * mysql分页处理
     *
     * @param startNum 1 页码
     * @param length 20每页条数
     * @return
     */
    public WherePrams mysqllimit(int startNum, int length) {
        Integer startnumnew = (startNum < 2) ? 0 : ((startNum - 1) * length);
        this.limit = " limit " + startnumnew + " , " + length + " ";
        return this;
    }

    public WherePrams orderBy(String order) {
        if (this.orderBy != null) {
            this.orderBy += "," + order;
        } else {
            this.orderBy = order;
        }
        return this;
    }

    @Override
    public String toString() {
        return "WherePrams [pram=" + pram + "]";
    }

    /**
     * 获取prams 有分页
     *
     * @return
     */
    public String getWherePrams() {
        String p = "";
        p += null == this.pram ? "" : this.pram;
        p += null == this.orderBy ? "" : (" order by " + this.orderBy);
        p += null == this.limit ? "" : this.limit;
        return p;
    }

    /**
     * 只有where条件没有分页，可用于查询数量
     *
     * @return
     */
    public String getWherePramsNoPage() {
        String p = "";
        p += null == this.pram ? "" : this.pram;
        p += null == this.orderBy ? "" : (" order by " + this.orderBy);
        return p;
    }

    public Serializable[] listParmsByFmt() {
        parms.addAll(limitParms);
        return parms.toArray(new Serializable[parms.size()]);
    }

    public Serializable[] listParms() {
        int length = getWherePrams().indexOf("?");
        if (-1 == length) {
            return new Serializable[0];
        }
        parms.addAll(limitParms);
        return parms.toArray(new Serializable[parms.size()]);
    }
}
