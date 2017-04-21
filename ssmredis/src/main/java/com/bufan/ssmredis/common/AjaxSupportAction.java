/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bufan.ssmredis.util.Constants;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author xiaosun
 */
public class AjaxSupportAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    //服务器响应客户端
    protected HttpServletResponse response;

    //HttpServletRequest接口最常用的方法就是获得请求中的参数
    protected HttpServletRequest request;

    //返回页面-->使用：1：设置setResultPage("页面地址)，2：struts2中使用<result name="success">${resultPage}</result>
    public String resultPage;

    //fastjson的一些属性 属性的详细参数请查看READEME.ME中的1
    private static final SerializerFeature[] features = {SerializerFeature.WriteNullNumberAsZero,
        SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect,
        SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCheckSpecialChar};

    /**
     * ********************************方法START*****************************
     */
    /**
     * 发送ajax消息 // TODO 该处抛出异常需要捕获
     *
     * @param msg
     * @throws java.io.IOException
     */
    protected synchronized void sendAjaxMsg(String msg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(msg);
        writer.flush();
    }

    /**
     * action 层调用返回数据
     *
     * @param result JSONObject
     * @param msg 返回信息
     * @param success 返回true|false
     */
    protected void generateAndSend(JSONObject result, String msg, boolean success) {
        if (null == result) {
            result = new JSONObject();
            result.put(Constants.KEY.KEY_SEND_DATA.toString(), "");
        }
        result.put(Constants.KEY.KEY_SEND_MSG.toString(), msg);
        result.put(Constants.KEY.KEY_SEND_SUCCESS.toString(), success);
        try {
            String s = JSON.toJSONString(result, features);
            sendAjaxMsg(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**********************************方法E N D******************************/
    
    /**
     * 
     * @return 
     */
    public String getResultPage() {
        return resultPage;
    }

    /**
     * 
     * @param resultPage 
     */
    public void setResultPage(String resultPage) {
        this.resultPage = resultPage;
    }

    /**
     * 
     * @param request 
     */
    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 
     * @param response 
     */
    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

}
