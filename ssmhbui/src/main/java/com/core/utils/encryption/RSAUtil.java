/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.utils.encryption;

import com.core.common.KeyUtil;
import com.core.utils.CheckUtil;
import com.core.utils.PropertiesLoader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import javax.crypto.Cipher;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。
 * 问题一：在login.jsp中，公钥的Exponent,和modulus输出格式问题
 *
 * 开始总是什么：长度过大，必须以0开始之类的异常。我想到很可能是js加密和纯java加密那些地方不同导致的。后来发现，原来是我公钥的Exponent,和modulus输出直接用的toString()方法，实际上应该用toString(16)，用16进制输出，因为在security.js中，那个
 *
 * RSAUtils.getKeyPair(publicKeyExponent,"",${publicKeyModulus);方法内部，明显是从16进制进行转换的
 *
 * @author LinQiang
 */
public class RSAUtil {

    /**
     * 生成公钥和私钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     *
     */
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        keyPairGen.initialize(1024);//这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低 
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
    }

    /**
     * 使用模和指数生成RSA公钥
     *
     *
     * @param modulus 模
     * @param exponent 指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     *
     * @param modulus 模
     * @param exponent 指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取默认私钥
     *
     * @return
     */
    public static RSAPrivateKey getDefaultPrivateKey() {
        RSAPrivateKey privateKey = null;
        try {
            //从配置文件获取私钥
            PropertiesLoader loader = new PropertiesLoader();
            //原始模
            String privateModulus = loader.getProperty(KeyUtil.KEYNAME.RSA_PRIVATE_MODULUS.toString());
            //原始私钥指数
            String privateExponent = loader.getProperty(KeyUtil.KEYNAME.RSA_PRIVATE_EXPONENT.toString());
            privateKey = RSAUtil.getPrivateKey(privateModulus, privateExponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 获取默认公钥
     *
     * @return
     */
    public static RSAPublicKey getDefaultPublicKey() {
        RSAPublicKey publicKey = null;
        try {
            //从配置文件获取公钥
            PropertiesLoader loader = new PropertiesLoader();
            //原始模
            String pubModulus = loader.getProperty(KeyUtil.KEYNAME.RSA_PUBLIC_MODULUS_ORIGINAL.toString());
            //原始公钥指数
            String pubExponent = loader.getProperty(KeyUtil.KEYNAME.RSA_PUBLIC_EXPONENT_ORIGINAL.toString());
            publicKey = RSAUtil.getPublicKey(pubModulus, pubExponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) {
        if (CheckUtil.isEmpty(data)) {
            return null;
        }
        if (null == publicKey) {
            return null;
        }
        String mi = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 模长    
            int key_len = publicKey.getModulus().bitLength() / 8;
            // 加密数据长度 <= 模长-11    
            String[] datas = splitString(data, key_len - 11);
            mi = "";
            //如果明文长度大于模长-11则要分组加密    
            for (String s : datas) {
                mi += bcd2Str(cipher.doFinal(s.getBytes()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mi;
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) {
        if (CheckUtil.isEmpty(data)) {
            return null;
        }
        if (null == privateKey) {
            return null;
        }
        String ming = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //模长    
            int key_len = privateKey.getModulus().bitLength() / 8;
            byte[] bytes = data.getBytes();
            byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
            //System.err.println(bcd.length);    
            //如果密文长度大于模长则要分组解密    
            ming = "";
            byte[][] arrays = splitArray(bcd, key_len);
            for (byte[] arr : arrays) {
                ming += new String(cipher.doFinal(arr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ming;
    }

    /**
     * 默认解密
     *
     * @param data
     * @return
     */
    public static String defaultDescrypt(String data) {
        String descrypedPwd = "";
        try {
            RSAPrivateKey privateKey = getDefaultPrivateKey();
            //解密后的密码,password是提交过来的密码
            descrypedPwd = decryptByPrivateKey(data, privateKey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return descrypedPwd;
    }

    /**
     * 默认加密
     *
     * @param data
     * @return
     */
    public static String defaultEncrypt(String data) {
        String encrypedValue = "";
        try {
            RSAPublicKey pubKey = getDefaultPublicKey();
            //解密后的密码,password是提交过来的密码
            encrypedValue = encryptByPublicKey(data, pubKey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encrypedValue;
    }

    /**
     * ASCII码转BCD码
     *
     * @param ascii
     * @param asc_len
     * @return
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    /**
     * ASCII码转BCD码
     *
     * @param asc
     * @return
     */
    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9')) {
            bcd = (byte) (asc - '0');
        } else if ((asc >= 'A') && (asc <= 'F')) {
            bcd = (byte) (asc - 'A' + 10);
        } else if ((asc >= 'a') && (asc <= 'f')) {
            bcd = (byte) (asc - 'a' + 10);
        } else {
            bcd = (byte) (asc - 48);
        }
        return bcd;
    }

    /**
     * BCD转字符串
     *
     * @param bytes
     * @return
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     *
     * @param string
     * @param len
     * @return
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     *
     * @param data
     * @param len
     * @return
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public static void main(String[] args) throws Exception {
        HashMap<String, Object> map = getKeys();
        //生成公钥和私钥    
        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");

        //模    
        String modulus = publicKey.getModulus().toString();
        System.out.println("modulus=" + modulus);
        String modulus16 = publicKey.getModulus().toString(16);
        System.out.println("modulus16=" + modulus16);
        //公钥指数    
        String public_exponent = publicKey.getPublicExponent().toString(16);
        System.out.println("pubkey exponent16=" + public_exponent);
        //私钥指数    
        String private_exponent = privateKey.getPrivateExponent().toString();
        System.out.println("private exponent=" + private_exponent);

    }
}
