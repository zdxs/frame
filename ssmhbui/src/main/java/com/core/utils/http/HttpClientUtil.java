package com.core.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * http帮助类
 *
 * @author LinQiang
 */
public class HttpClientUtil {

    /**
     * 发送request请求
     *
     * @param httpclient 客户端
     * @param httpPost post对象
     * @return
     */
    public static HttpResponse sendRequest(CloseableHttpClient httpclient, HttpPost httpPost) {
        CloseableHttpResponse response = null;
        try {
            httpclient.execute(httpPost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 格式化response实体对象
     *
     * @param response
     * @return
     */
    public static String parseReponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 生成post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static HttpPost postForm(String url, Map<String, String> param) {
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            List<NameValuePair> nvpr = new ArrayList<NameValuePair>();
            Set<String> keySet = param.keySet();
            for (String key : keySet) {
                BasicNameValuePair bnvp = new BasicNameValuePair(key, param.get(key));
                nvpr.add(bnvp);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvpr, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpPost;
    }

    /**
     * 跨域请求
     *
     * @param url 请求地址
     * @param param 参数
     * @return 返回字符串结果
     */
    public static String post(String url, Map<String, String> param) {
        String body = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = postForm(url, param);
            HttpResponse response = httpclient.execute(httpPost);
            body = parseReponse(response);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    public static void main(String[] args) throws Exception {

    }
}
