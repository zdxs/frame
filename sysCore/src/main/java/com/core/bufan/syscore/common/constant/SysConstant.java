/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.bufan.syscore.common.constant;

import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 全局常量配置文件
 *
 * @author xiaosun
 */
public class SysConstant {

    // TODO 2017-03-29目的转换字符 fastjson的一些属性 属性的详细参数请查看READEME.ME中的1 
    public static final SerializerFeature[] features = {SerializerFeature.WriteNullNumberAsZero,
        SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect,
        SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCheckSpecialChar,
        SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
        SerializerFeature.WriteSlashAsSpecial, SerializerFeature.BrowserCompatible};

    public static final String ENCODING = "UTF-8";
    public static final String CONTENT_TYPE = "application/json;charset=utf-8";
    public static final String KEY_DATA = "data";
    public static final String KEY_MSG = "msg";
    public static final String KEY_SUCCESS = "success";
}
