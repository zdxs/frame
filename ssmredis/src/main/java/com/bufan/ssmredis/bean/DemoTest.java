/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.bean;

import com.bufan.ssmredis.common.BaseDao.AgentBean;
import com.bufan.ssmredis.common.CoreBean;

/**
 *
 * @author xiaosun
 */
public class DemoTest extends AgentBean implements CoreBean {

    private String id;              //id
    private String name;            //名称
    private String password;        //密码
    private String remark;          //备注

    public DemoTest() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DemoTest{" + "id=" + id + ", name=" + name + ", password=" + password + ", remark=" + remark + '}';
    }

    @Override
    public String fkField() {
        return null;
    }

    @Override
    public String likeField() {
        return "name,remark";
    }

    @Override
    public String uniqueField() {
        return "id,name";
    }

    @Override
    public String pkField() {
        return "id";
    }
}
