package com.kischang.simple_utils.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * DES 加密解密工具类
 *
 * Create By KisChang On 2016-05-24
 *
 * @version 1.0
 */
public class DESUtils {

    //DES算法密钥
    private byte[] des_key = { 21, 1, -110, 82, -32, -85, -128, -65 };
    //加密算法 DES、DESede(即3DES)、Blowfish
    private String Algorithm = "DES";

    public enum AlgorithmType {
        DES, DESede, Blowfish
    }

    public DESUtils() {
    }

    public DESUtils(byte[] des_key) {
        this.des_key = des_key;
    }

    public DESUtils(byte[] des_key, AlgorithmType algorithm) {
        this.des_key = des_key;
        Algorithm = algorithm.name();
    }

    /**
     * 数据加密，算法（DES）
     * @param data 要进行加密的数据
     * @return 加密后的数据
     */
    public byte[] encryptDes(byte[] data) {
        byte[]  encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(des_key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);
            SecretKey key = keyFactory.generateSecret(deskey);
            // 加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);
            // 加密，并把字节数组编码成字符串
            encryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**
     * 数据解密，算法（DES）
     * @param cryptData 加密数据
     * @return 解密后的数据
     */
    public byte[] decryptBasedDes(byte[] cryptData) {
        byte[] decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(des_key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);
            SecretKey key = keyFactory.generateSecret(deskey);
            // 解密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            // 把字符串解码为字节数组，并解密
            decryptedData = cipher.doFinal(cryptData);
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }

}
