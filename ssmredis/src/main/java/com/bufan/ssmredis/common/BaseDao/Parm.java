/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.common.BaseDao;

/**
 * 记录每个实体类的字段和值
 *
 * @author xiaosun
 */
public class Parm {

    private String file;//字段名称

    private Object value;//字段值

    public Parm(String file, Object value) {
        this.file = file;
        this.value = value;
    }

    public Parm() {
        super();
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
