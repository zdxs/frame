/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 验证表单数据，正则表达式
 */
package com.core.utils.check;

import com.core.utils.CheckUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表单验证工具类
 *
 * @author xiaosun
 */
public class FormCheck {

    private static Pattern pattern = null;
    private static Matcher matcher = null;

    //由字母数字下划线组成且开头必须是字母，不能超过16位
    private static final String REGEX_USERNAME = "[a-zA-Z]{1}[a-zA-Z0-9_]{1,15}";
    //字母和数字构成，不能超过16位
    private static final String REGEX_PASSWORD = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,16}";
    //验证昵称，支持中英文（包括全角字符）、数字、下划线和减号 （全角及汉字算两位）,,中文按二位计数
    //^[\\w\\-－＿[０-９]\u4e00-\u9fa5\uFF21-\uFF3A\uFF41-\uFF5A]+$
    private static final String REGEX_NIKENAME = "^[\\w\\-－＿[０-９]\u4e00-\u9fa5\uFF21-\uFF3A\uFF41-\uFF5A]+$";
    //匹配手机号全数字，长度为11位）
    private static final String REGEX_CELLPHONE = "[0-9]{11}";
    //匹配邮箱必须包含@符号；必须包含点；点和@之间必须有字符 \\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}
    //    ^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$
    private static final String REGEX_EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    //座机-区号+座机号码+分机号码 
    //    (?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)
    private static final String REGEX_FIXED_PHONE = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
    //邮编 匹配中国邮政编码  [1-9]\\d{5}
    private static final String REGEX_POST_CODE = "[1-9]\\\\d{5}";

    //匹配qq全数字 要求1~50位，不能以0开头，只能是数字  
    private static final String REGEX_QQ = "^[1-9][0-9]{0,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 验证IP地址
     *
     * @param obj
     * @return
     */
    public static boolean check_ipaddr(String obj) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(obj)) {
            try {
                pattern = Pattern.compile(REGEX_IP_ADDR);
                matcher = pattern.matcher(obj);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 验证验证URL
     *
     * @param obj
     * @return
     */
    public static boolean check_httpurl(String obj) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(obj)) {
            try {
                pattern = Pattern.compile(REGEX_URL);
                matcher = pattern.matcher(obj);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 验证身份证
     *
     * @param obj
     * @return
     */
    public static boolean check_idcard(String obj) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(obj)) {
            try {
                pattern = Pattern.compile(REGEX_ID_CARD);
                matcher = pattern.matcher(obj);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 验证中文
     *
     * @param obj
     * @return
     */
    public static boolean check_chinaese(String obj) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(obj)) {
            try {
                pattern = Pattern.compile(REGEX_CHINESE);
                matcher = pattern.matcher(obj);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 验证用户名格式 长度最小4位
     *
     * @param username 用户名
     * @return
     */
    public static boolean check_username(String username) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(username)) {
            try {
                //获取长度
                int lengths = getStrLength(username);
                if (lengths > 4) {
                    pattern = Pattern.compile(REGEX_USERNAME);
                    matcher = pattern.matcher(username);
                    flog = matcher.matches();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 验证密码格式 长度最小4位
     *
     * @param userpassword 密码
     * @return
     */
    public static boolean check_userpassword(String userpassword) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(userpassword)) {
            //获取长度
            int lengths = getStrLength(userpassword);
            if (lengths > 5) {
                try {
                    pattern = Pattern.compile(REGEX_PASSWORD);
                    matcher = pattern.matcher(userpassword);
                    flog = matcher.matches();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return flog;
    }

    /**
     * 验证昵称格式
     *
     * @param nickname 昵称
     * @return
     */
    public static boolean check_nickname(String nickname) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(nickname)) {
            try {
                pattern = Pattern.compile(REGEX_NIKENAME);
                matcher = pattern.matcher(nickname);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 验证手机格式
     *
     * @param cellphone 手机
     * @return
     */
    public static boolean check_cellphone(String cellphone) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(cellphone)) {
            //获取长度
            try {
                pattern = Pattern.compile(REGEX_CELLPHONE);
                matcher = pattern.matcher(cellphone);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 验证邮件格式
     *
     * @param email 邮件
     * @return
     */
    public static boolean check_email(String email) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(email)) {
            try {
                pattern = Pattern.compile(REGEX_EMAIL);
                matcher = pattern.matcher(email);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 验证座机格式
     *
     * @param fixedphone 座机
     * @return
     */
    public static boolean check_FIXED_PHONE(String fixedphone) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(fixedphone)) {
            try {
                pattern = Pattern.compile(REGEX_FIXED_PHONE);
                matcher = pattern.matcher(fixedphone);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 验证邮政编码格式
     *
     * @param postcode 邮政编码
     * @return
     */
    public static boolean check_postcode(String postcode) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(postcode)) {
            try {
                pattern = Pattern.compile(REGEX_POST_CODE);
                matcher = pattern.matcher(postcode);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    /**
     * 获取字符串的长度，对双字符（包括汉字）按两位计数
     *
     * @param value 计算的字符
     * @return 字符长度
     */
    public static int getStrLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        try {
            for (int i = 0; i < value.length(); i++) {
                String temp = value.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                } else {
                    valueLength += 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueLength;
    }

    /**
     * 校验qq号码
     *
     * @param qq
     * @return
     */
    public static boolean check_qq(String qq) {
        boolean flog = false;
        if (!CheckUtil.isEmpty(qq)) {
            try {
                pattern = Pattern.compile(REGEX_QQ);
                matcher = pattern.matcher(qq);
                flog = matcher.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flog;
    }

    public static void main(String[] args) {
        //用户名验证
//        System.out.println(FormCheck.check_username("a"));
//        System.out.println(FormCheck.check_email("a@qq.com"));
        System.out.println(FormCheck.check_userpassword("aa324@"));
//        System.out.println(FormCheck.check_cellphone("17008258884"));
//        System.out.println(FormCheck.check_FIXED_PHONE("023-76652546"));
//        System.out.println(FormCheck.check_qq("0"));
    }
}
