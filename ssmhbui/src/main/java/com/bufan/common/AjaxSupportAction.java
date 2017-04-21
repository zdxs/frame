package com.bufan.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.core.utils.CheckUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 *
 * @author xiaoyang 2013年11月26日
 */
@SuppressWarnings("serial")
public class AjaxSupportAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    protected final Logger logger = Logger.getLogger(getClass());

    protected final String LOG_TAG = this.getClass().getName();

    protected HttpServletResponse response;

    protected HttpServletRequest request;

    private static SerializerFeature[] features = {SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat};

    //返回页面
    public String resultPage;
    //分页参数
    public Integer pageIndex = 1;//页码
    public Integer pageSize = 20;//每页条数

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * 发送ajax消息
     *
     * @param msg
     * @throws IOException author : xiaoyang
     */
    protected synchronized void sendAjaxMsg(String msg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
//        
//        // 获取访问的客户端信息,如果是ide则不用json方式反正使用json方式(在ie下发json会使得浏览器提示下载文件)
//        String browse = BrowserCheck.BrowseName(request.getHeader("User-Agent").toLowerCase());
//        System.out.println(browse);
//        if (CheckUtil.checkStr(browse)) {
//            if (browse.equals("ie") || browse == "ie") {
//                response.setContentType("text/html;charset=UTF-8");
//            } else {
//                response.setContentType("application/json;charset=utf-8");
//            }
//        }
        PrintWriter writer = response.getWriter();
        writer.print(msg);
        writer.flush();
    }

    /**
     * 跨域json流重新封装
     *
     * @param msg
     * @param callback
     * @throws IOException
     */
    protected synchronized void sendAjaxMsg(String msg, String callback) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(callback + "(" + msg + ")");
        writer.flush();
    }

    /**
     * 跨域json 流封装
     *
     * @param result
     * @param msg
     * @param success
     * @param callback
     * @author xiaotian
     */
    protected void generateAndSendCrs(JSONObject result, String msg, boolean success, String callback) {

        result.put("msg", msg);
        result.put("success", success);
        try {
            sendAjaxMsg(result.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.WriteDateUseDateFormat), callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跨域json 流封装
     *
     * @param result
     * @param filter
     * @param msg
     * @param success
     * @param callback
     * @author xiaotian
     */
    protected void generateAndSendCrs(JSONObject result, PropertyPreFilter filter, String msg,
            boolean success, String callback) {
        result.put("msg", msg);
        result.put("success", success);
        try {
            if (null != filter) {
                sendAjaxMsg(JSON.toJSONString(result, filter,
                        SerializerFeature.DisableCircularReferenceDetect,
                        SerializerFeature.WriteDateUseDateFormat), callback);
            } else {
                sendAjaxMsg(result.toJSONString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-access")
    protected void generateAndSend(JSONObject result, String msg, boolean success) {
        if (result == null) {
            result = new JSONObject();
            result.put("data", "");
        }
        result.put("msg", msg);
        result.put("success", success);
        try {
            String s = JSON.toJSONString(result, features);
//            System.out.println(s);
            sendAjaxMsg(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    protected void generateAndSend(JSONObject result, PropertyPreFilter filter, String msg,
            boolean success) {
        result.put("msg", msg);
        result.put("success", success);
        try {
            if (null != filter) {
                sendAjaxMsg(JSON.toJSONString(result, filter,
                        SerializerFeature.DisableCircularReferenceDetect,
                        SerializerFeature.WriteDateUseDateFormat));
            } else {
                sendAjaxMsg(result.toJSONString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证参数，如果不符合预期则抛出IllegalArgumentException异常
     *
     * @param result 参数是否符合预期，true 符合， false 不符合
     * @param errorMsg 错误信息
     * @throws IllegalArgumentException author : xiaoyang
     */
    public static void checkParam(boolean result, String errorMsg) throws IllegalArgumentException {
        if (!result) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    /**
     * 如果通过{@link Exception#getMessage()}返回的值为空，则返回指定的信息
     *
     * @param e 异常信息对象
     * @param exceptionMsg 默认指定的异常消息
     */
    protected static String checkExceptionMessage(Exception e, String exceptionMsg) {
        if (null == e.getMessage() || "".equals(e.getMessage())) {
            return exceptionMsg;
        } else {
            return e.getMessage();
        }
    }

    /**
     * 验证请求方式
     *
     * @param methodType 允许的请求方式
     * @throws Exception
     */
    protected void filterRequestMethod(String methodType) throws Exception {
        String validateType = request.getMethod();
        if (null == methodType || "".equals(methodType)) {
            return;
        } else if (!validateType.toUpperCase().equals(methodType)) {
            throw new Exception();
        }
    }

    /**
     * Http请求方法类型类
     *
     * @author peng.gou
     * @version 1.0
     * @since 1.0
     */
    protected static interface HttpRequestMethodType {

        /**
         * GET请求方式
         */
        public final static String GET = "GET";

        /**
         * POST请求方式
         */
        public final static String POST = "POST";

        /**
         * PUT请求方式
         */
        public final static String PUT = "PUT";

        /**
         * DELETE请求
         */
        public final static String DELETE = "DELETE";

    }

    public String getResultPage() {
        return resultPage;
    }

    public void setResultPage(String resultPage) {
        this.resultPage = resultPage;
    }

    public Integer getPageIndex() {
        String pi = request.getParameter("pageIndex");
        if (CheckUtil.isEmpty(pi)) {
            pageIndex = Integer.parseInt(pi);
            return pageIndex;
        } else {
            return pageIndex;
        }

    }

    public void setPageIndex(Integer pageIndex) {
        String pi = request.getParameter("pageIndex");
        if (CheckUtil.isEmpty(pi)) {
            pageIndex = Integer.parseInt(pi);
            this.pageIndex = pageIndex;
        } else {
            this.pageIndex = pageIndex;
        }
    }

    public Integer getPageSize() {
        String ps = request.getParameter("pageSize");
        if (CheckUtil.isEmpty(ps)) {
            pageSize = Integer.parseInt(ps);
            return pageSize;
        } else {
            return pageSize;
        }
    }

    public void setPageSize(Integer pageSize) {
        String ps = request.getParameter("pageSize");
        if (CheckUtil.isEmpty(ps)) {
            pageSize = Integer.parseInt(ps);
            this.pageSize = pageSize;
        } else {
            this.pageSize = pageSize;
        }

    }

    /**
     * 重置分页数据
     */
    public void SetpageSizepageIndex() {
        this.setPageIndex(pageIndex);
        this.setPageSize(pageSize);
    }

}
