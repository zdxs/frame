/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 获取浏览器信息
 */
package com.core.common.browser;

import com.core.common.KeyUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author xiaosun
 */
public class BrowserUtil {

    /**
     * 获取浏览器信息
     *
     * @param ua request.getHeader("User-Agent")
     * @return
     */
    public static Map<String, String> getBrowser(String ua) {
        if (null == ua) {
            return null;
        }
        Map<String, String> Sys = new HashMap<>();
        try {
            UserAgent userAgent = UserAgent.parseUserAgentString(ua);
            Browser browser = userAgent.getBrowser();
            Version version = userAgent.getBrowserVersion();
            OperatingSystem system = userAgent.getOperatingSystem();
            Sys.put(KeyUtil.KEYNAME.BROWSER_NAME.toString(), browser.getName());
            Sys.put(KeyUtil.KEYNAME.BROWSER_VERSION.toString(), version.getVersion());
            Sys.put(KeyUtil.KEYNAME.BROWSER_SYSTEM.toString(), system.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Sys;
    }

    /**
     * 自己写的获取浏览器信息
     *
     * @param ua
     * @return
     */
    public static Map<String, String> getUserAgent(String ua) {
        Map<String, String> Sys = new HashMap<String, String>();
        if (null == ua) {
            return null;
        }
        ua = ua.toLowerCase();
        String s;
        String msieP = "msie ([\\d.]+)";
        String firefoxP = "firefox\\/([\\d.]+)";
        String chromeP = "chrome\\/([\\d.]+)";
        String operaP = "opera.([\\d.]+)/)";
        String safariP = "version\\/([\\d.]+).*safari";

        Pattern pattern = Pattern.compile(msieP);
        Matcher mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            Sys.put("type", "ie");
            Sys.put("version", s.split(" ")[1]);
            return Sys;
        } else {
        }
        pattern = Pattern.compile(firefoxP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            System.out.println(s);
            Sys.put("type", "firefox");
            Sys.put("version", s.split("/")[1]);
            return Sys;
        }
        pattern = Pattern.compile(chromeP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            Sys.put("type", "chrome");
            Sys.put("version", s.split("/")[1]);
            return Sys;
        }
        pattern = Pattern.compile(operaP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            Sys.put("type", "opera");
            Sys.put("version", s.split("\\.")[1]);
            return Sys;
        }
        pattern = Pattern.compile(safariP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            Sys.put("type", "safari");
            Sys.put("version", s.split("/")[1].split(".")[0]);
            return Sys;
        }
        return Sys;
    }
}
