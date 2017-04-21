/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.utils;

import org.slf4j.Logger;

/**
 * 日志帮助类
 *
 * @author LinQiang
 * @date 2017-1-3 16:40:27
 * @version V1.0
 */
public class LoggerUtil {

    /**
     * 记录异常日志
     *
     * @param logger 日志记录类
     * @param e 异常
     * @param c 类
     */
    public static void exceptionLog(Logger logger, Exception e, Class c) {
        try {
            e.printStackTrace();
            logger.error(c.getName() + ":" + e.getMessage());
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error(LoggerUtil.class.getName() + ":" + e1.getMessage());
        }
    }
}
