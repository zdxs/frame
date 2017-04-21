package com.core.common.sql;

/**
 * 表的字段和值
 *
 * @author xiaosun
 */
public class Pram {

    private String file;//字段名称

    private Object value;//字段值

    public Pram() {
    }

    public Pram(String file, Object value) {
        this.file = file;
        this.value = value;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
