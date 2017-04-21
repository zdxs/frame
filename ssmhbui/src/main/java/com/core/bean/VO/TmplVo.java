/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bean.VO;

/**
 * 找回密码，模板
 *
 * @author xiaosun
 */
public class TmplVo {

    private String type;//类别，邮件：2，手机：3
    private String scop;//作用于
    private String title;//标题
    private String values;//内容
    private String info;//警告信息
    
    

    public TmplVo(String type, String scop, String title, String values, String info) {
        this.type = type;
        this.scop = scop;
        this.title = title;
        this.values = values;
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScop() {
        return scop;
    }

    public void setScop(String scop) {
        this.scop = scop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
