/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
<!-- https://mvnrepository.com/artifact/com.github.stuxuhai/jpinyin -->
maven 地址
<dependency>
    <groupId>com.github.stuxuhai</groupId>
    <artifactId>jpinyin</artifactId>
    <version>1.1.8</version>
</dependency>
 */
package com.core.common.pinyin;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 中文字符串返回拼音
 *
 * @author xiaosun
 */
public class PinYinUtil {

    /**
     * 输入中文返回拼音
     *
     * @param str1 需要输出拼音的字符
     * @param splitstr 以什么分开 默认以","隔开
     * @return nǐ,hǎo,shì,jiè
     */
    public static String zh_pinyin(String str1, String splitstr) {
        if (null == str1 || str1.equals("")) {
            return null;
        }
        String returnstr = null;
        if (null == splitstr || splitstr.equals("")) {
            splitstr = ",";
        }
        try {
            returnstr = PinyinHelper.convertToPinyinString(str1, splitstr, PinyinFormat.WITH_TONE_MARK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnstr;
    }

    /**
     * 输入中文返回拼音
     *
     * @param str1 需要输出拼音的字符
     * @param splitstr 以什么分开
     * @return ni3,hao3,shi4,jie4
     */
    public static String zh_pinyinnum(String str1, String splitstr) {
        if (null == str1 || str1.equals("")) {
            return null;
        }
        String returnstr = null;
        if (null == splitstr || splitstr.equals("")) {
            splitstr = ",";
        }
        try {
            returnstr = PinyinHelper.convertToPinyinString(str1, splitstr, PinyinFormat.WITH_TONE_NUMBER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnstr;
    }

    /**
     * 输入中文返回拼音
     *
     * @param str1 需要输出拼音的字符
     * @param splitstr 以什么分开
     * @return ni,hao,shi,jie
     */
    public static String zh_pinyinnone(String str1, String splitstr) {
        if (null == str1 || str1.equals("")) {
            return null;
        }
        String returnstr = null;
        if (null == splitstr || splitstr.equals("")) {
            splitstr = ",";
        }
        try {
            returnstr = PinyinHelper.convertToPinyinString(str1, splitstr, PinyinFormat.WITHOUT_TONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnstr;
    }
}
