/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring获取实例bean
 *
 * @author xiaosun
 */
public class ApplicationContextUtil {

    /**
     * spring获取实例bean
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        Object object = null;
        try {
            object = new ClassPathXmlApplicationContext("spring/applicationContext-config.xml").getBean(beanName);
        } catch (BeansException beansException) {
            beansException.printStackTrace();
        }
        return object;
    }
}
