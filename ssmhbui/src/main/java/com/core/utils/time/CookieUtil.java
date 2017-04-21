/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.utils.time;

import java.math.BigDecimal;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具类
 *
 * @author LinQiang
 * @date 2016-12-23 15:34:14
 * @version V1.0
 */
public class CookieUtil {

    private static int maxAge = 60 * 60 * 24;//最大生命周期

    /**
     * 根据cookie名称获取cookie
     *
     * @param cookieName
     * @param request
     * @return
     */
    public Cookie getCookie(String cookieName, HttpServletRequest request) {
        Cookie cookie1 = null;
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(cookieName)) {
                    return cookie;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookie1;
    }

    /**
     * 添加新的Cookie
     *
     * @param name
     * @param value
     * @param response
     */
    public static void setNewCookie(String name, String value, HttpServletResponse response) {
        try {

            Cookie cookie = new Cookie(name, value);
            cookie.setMaxAge(maxAge);//设置1小时生存期，当设置为负值时，则为浏览器进程Cookie(内存中保存)，关闭浏览器就失效。
            response.addCookie(cookie);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Cookie值
     *
     * @param name
     * @param request
     * @return
     */
    public static String getCookieValue(String name, HttpServletRequest request) {
        String value = "";
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 删除cookie中对应数值
     *
     * @param name
     * @param request
     * @param response
     */
    public static void deleteCookies(String name, HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0); //设置为0为立即删除该Cookie
                    response.addCookie(cookie);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加规定cookie中的数值
     *
     * @param name
     * @param value
     * @param request
     * @param response
     */
    public static void addnewCookieValue(String name, BigDecimal value, HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    cookie.setValue(new BigDecimal(cookie.getValue().trim()).add(value).toString());
                    response.addCookie(cookie);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新设置指定cookie的值
     *
     * @param name cookie名称
     * @param value 新值
     * @param request
     * @param response
     */
    public static void setNewCookieValue(String name, String value, HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    cookie.setValue(value);
                    response.addCookie(cookie);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 减去规定cookie中的数值
     *
     * @param name
     * @param value
     * @param request
     * @param response
     */
    public static void subCookieValue(String name, BigDecimal value, HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    cookie.setValue(new BigDecimal(cookie.getValue()).subtract(value).toString());
                    response.addCookie(cookie);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
