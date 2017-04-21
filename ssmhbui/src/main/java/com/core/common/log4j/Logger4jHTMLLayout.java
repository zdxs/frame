/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.common.log4j;

import org.apache.log4j.HTMLLayout;

/**
 * 为了防止乱码从写htmllayout
 *
 * @author xiaosun
 */
public class Logger4jHTMLLayout extends HTMLLayout {

    @Override
    public String getContentType() {
        return "text/html;charset=utf-8";
    }
}
