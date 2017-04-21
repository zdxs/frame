/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
设置值得时候一些key名称
 */
package com.core.common;

/**
 *
 * @author xiaosun
 */
public class KeyUtil {

    /**
     * 存放key
     */
    public static enum URL {
        //默认配置文件路径
        DEFAULT_PROPERTIES_PATH("properties/config.properties");
        // 成员变量
        private String url;
        // 构造方法

        private URL(String url) {
            this.url = url;
        }
        // 普通方法

        @Override
        public String toString() {
            return this.url;
        }
    }

    /**
     * 存放key
     */
    public static enum KEYNAME {
        //RSA加密解密的私钥模
        RSA_PRIVATE_MODULUS("private_modulus"),
        //RSA加密解密的私钥指数
        RSA_PRIVATE_EXPONENT("private_exponent"),
        //RSA加密解密的公钥原始模
        RSA_PUBLIC_MODULUS_ORIGINAL("public_modulus_original"),
        //RSA加密解密的公钥原始指数
        RSA_PUBLIC_EXPONENT_ORIGINAL("public_exponent_original"),
        //浏览器相关
        BROWSER_NAME("BROWSER_NAME"),
        BROWSER_VERSION("BROWSER_VERSION"),
        BROWSER_SYSTEM("BROWSER_SYSTEM"),
        //存入服务器
        DEPT("dept");
        // 成员变量
        private String key;
        // 构造方法

        private KEYNAME(String key) {
            this.key = key;
        }
        // 普通方法

        @Override
        public String toString() {
            return this.key;
        }
    }
}
