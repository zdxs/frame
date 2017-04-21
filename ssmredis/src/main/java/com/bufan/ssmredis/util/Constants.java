/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bufan.ssmredis.util;

import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * java类文件中所用到的公共信息
 *
 * @author xiaosun
 */
public class Constants {

    //存放redis key的时候分割符号
    public static final String KEY_REDIS_SPLIT = ":";
    //集合list--命名规则 key=实体类+KEY_REDIS_SPLIT+KEY_REDIS_LIST  值=存储实体id
    public static final String KEY_REDIS_LIST = "lsit";
    public static final String KEY_REDIS_HASH = "hash";
    public static final String KEY_REDIS_SET = "set";
    public static final String KEY_REDIS_ZSET = "zset";

    //fastjson的一些属性 属性的详细参数请查看READEME.ME中的1
    public static final SerializerFeature[] features = {SerializerFeature.WriteNullNumberAsZero,
        SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect,
        SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCheckSpecialChar,
        SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
        SerializerFeature.WriteSlashAsSpecial, SerializerFeature.BrowserCompatible};

    /**
     * 类方法所用到的键
     */
    public static enum KEY {
        KEY_SEND_SUCCESS("success"),
        KEY_SEND_MSG("msg"),
        KEY_SEND_DATA("data");

        // 成员变量
        private final String value;

        // 构造方法
        private KEY(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
