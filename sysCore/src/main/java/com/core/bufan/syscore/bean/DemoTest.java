/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.bean;

/**
 *
 * @author xiaosun
 */
public class DemoTest {

    protected Integer id;
    private String name;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DemoTest(Integer id, String name, String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public DemoTest() {
        super();
    }

    @Override
    public String toString() {
        return "DemoTest{" + "id=" + id + ", name=" + name + ", version=" + version + '}';
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
