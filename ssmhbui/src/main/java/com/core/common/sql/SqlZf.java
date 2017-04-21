package com.core.common.sql;

/**
 * sql 拼接时候的字符链接
 *
 * @author xiaosun
 */
public enum SqlZf {

    //常量
    EQ, NE, LIKE, DA, IXAO, IN, DESC, ASC;

    /**
     * 常量对应数据库字符
     *
     * EQ==>"="
     *
     * NE==>"<>"
     *
     * LIKE==>"like"
     *
     * DA==>">"
     *
     * IXAO==>"<">
     *
     * IN==>"in"
     *
     * DESC==>"desc"
     *
     * ASC==>"asc"
     *
     * @param c
     * @return
     */
    public static String getSqlWhere(SqlZf c) {

        //asc 按升序排列
        //desc 按降序排列
        switch (c) {
            case EQ:
                return "=";
            case NE:
                return "<>";
            case LIKE:
                return "like";
            case DA:
                return ">";
            case IXAO:
                return "<";
            case IN:
                return "in";
            case DESC:
                return "desc";
            case ASC:
                return "asc";
            default:
                return "=";
        }
    }
}
