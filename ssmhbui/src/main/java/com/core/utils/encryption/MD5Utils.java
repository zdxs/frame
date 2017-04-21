package com.core.utils.encryption;

import com.core.utils.CheckUtil;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * MD5加密帮助类
 *
 * @author LinQiang
 */
public class MD5Utils {

    static Logger logger = Logger.getLogger(MD5Utils.class);

    public static void main(String[] args) {
        System.out.println("USER_NAME_COOKIE:" + MD5("ZHG_NAME"));
        System.out.println("USER_PWD_COOKIE:" + MD5("admin"));
    }

    /**
     * 将字符串MD5加密
     *
     * @param s 待加密的字符串
     * @return
     */
    public final static String MD5(String s) {
        String md5Str = "";
        if (!CheckUtil.isEmpty(s)) {
            return md5Str;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            if (null != str && str.length > 0) {
                md5Str = new String(str).toLowerCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return md5Str;
        }
        return md5Str;
    }

    /**
     * 将字符串MD5加密
     *
     * @param str 待加密的字符串
     * @return
     */
    public static String getMd5String(String str) {
        MessageDigest messageDigest = null;
        StringBuilder md5StrBuff = new StringBuilder();
        try {
            if (CheckUtil.isEmpty(str)) {
                return null;
            }
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }
}
