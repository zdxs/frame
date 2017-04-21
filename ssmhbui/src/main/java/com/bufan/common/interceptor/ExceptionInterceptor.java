/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 使用trycatch后必须新加throw语句抛出异常：否则该拦截器无效。
 */
package com.bufan.common.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;

/**
 *
 * @author xiaosun
 */
public class ExceptionInterceptor implements Interceptor {

    @Override
    public void destroy() {
        System.err.println("错误销毁");
    }

    @Override
    public void init() {
        System.err.println("错误初始");
    }

    @Override
    public String intercept(ActionInvocation ai) throws Exception {
        System.err.println("错误intercept");
        String result = null; // Action的返回值  
        try {
            // 运行被拦截的Action,期间如果发生异常会被catch住   
            result = ai.invoke();
            return result;
        } catch (Exception e) {
            /**
             * 处理异常
             */
            String errorMsg = "出现错误信息，请查看日志！";
            //通过instanceof判断到底是什么异常类型   
            if (e instanceof RuntimeException) {
                //未知的运行时异常   
                RuntimeException re = (RuntimeException) e;
                //re.printStackTrace();
                errorMsg = re.getMessage().trim();
            }
            //把自定义错误信息   
            HttpServletRequest request = (HttpServletRequest) ai.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
            /**
             * 发送错误消息到页面
             */
            request.setAttribute("errorMsg", errorMsg);

            /**
             * log4j记录日志
             */
            Log log = LogFactory.getLog(ai.getAction().getClass());
            log.error(errorMsg, e);
            return "errorMsg";
        }// ...end of catch   
    }

}
