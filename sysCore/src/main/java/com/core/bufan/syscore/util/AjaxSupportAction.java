/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.core.bufan.syscore.common.constant.SysConstant;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xiaosun
 */
public class AjaxSupportAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    protected HttpServletResponse response;
    protected HttpServletRequest request;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 发送ajax消息
     *
     * @param msg
     * @throws IOException
     */
    protected void sendAjaxMsg(String msg) throws IOException {
        response.setCharacterEncoding(SysConstant.ENCODING);
        response.setContentType(SysConstant.CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        writer.print(msg);
        writer.flush();
        writer.close();
    }

    /**
     * 发送json数据到客户端
     *
     * @param result
     * @param msg
     * @param success
     */
    protected void generateAndSend(JSONObject result, String msg, boolean success) {
        result.put(SysConstant.KEY_MSG, msg);
        result.put(SysConstant.KEY_SUCCESS, success);
        Object obj = result.get(SysConstant.KEY_DATA);
        if (null == obj || obj.equals("")) {
            result.put(SysConstant.KEY_DATA, new JSONArray());
        }
        try {
            String s = JSON.toJSONString(result, SysConstant.features);
            sendAjaxMsg(s);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

}
