/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.common;

/**
 *
 * @author xiaosun
 */
public interface CoreBean {

    /**
     * 唯一约束
     *
     * @return
     */
    public String uniqueField();

    /**
     * 外键
     * <p>
     * 格式--》表+|+字段+","+表+|+字段+","
     *
     * @return
     */
    public String fkField();

    /**
     * 模糊查询
     *
     * @return
     */
    public String likeField();

    /**
     * 主键
     *
     * @return
     */
    public String pkField();
}
