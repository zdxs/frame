/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.bean;

import java.io.Serializable;

/**
 *
 * @author xiaosun
 */
public class DemoTest implements Serializable {

    protected Integer id;
    private String name;

    public DemoTest() {
        super();
    }

    @Override
    public String toString() {
        return "DemoTest{" + "id=" + id + ", name=" + name + '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DemoTest(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
