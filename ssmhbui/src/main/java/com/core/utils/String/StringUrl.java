/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 路径转码
 */
package com.core.utils.String;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xiaosun
 */
public class StringUrl {

    public static Logger logger = LoggerFactory.getLogger(StringUrl.class);
    private static final String BM_UTF = "UTF-8";

    /**
     * 对中文解码
     *
     * @param string
     * @return
     */
    public static String urlDecode(String string) {
        try {
            return URLDecoder.decode(string, BM_UTF);
        } catch (UnsupportedEncodingException e) {
            logger.error("urlDecode is error", e);
        }
        return string;
    }

    /**
     * 对中文编码
     *
     * @param string
     * @return
     */
    public static String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, BM_UTF);
        } catch (UnsupportedEncodingException e) {
            logger.error("urlEncode is error", e);
        }
        return string;
    }

    public static void main(String[] args) {
        System.out.println(StringUrl.urlEncode("http://localhost/zhg/user/login/"));
        System.out.println(StringUrl.urlDecode("%E4%B8%AD%E6%96%87"));
    }
}
