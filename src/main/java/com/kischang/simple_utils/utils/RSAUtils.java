package com.kischang.simple_utils.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author KisChang
 * @date 2020-07-12
 */
public class RSAUtils {

    private static KeyFactory keyFactory = null;

    public static void init() throws NoSuchAlgorithmException {
        Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        keyFactory = KeyFactory.getInstance("RSA");;
    }

    public static PrivateKey loadPrivateKey(String key) throws InvalidKeySpecException {
        return loadPrivateKey(Base64.decodeBase64(key));
    }
    public static PrivateKey loadPrivateKey(byte[] key) throws InvalidKeySpecException {
        return keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(key)
        );
    }

    public static PublicKey loadPublicKey(String key) throws InvalidKeySpecException {
        return loadPublicKey(Base64.decodeBase64(key));
    }
    public static PublicKey loadPublicKey(byte[] key) throws InvalidKeySpecException {
        return keyFactory.generatePublic(
                new X509EncodedKeySpec(key)
        );
    }

    //私钥加密
    public static byte[] encryptByPrivate(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
    //公钥解密
    public static byte[] decryptByPublic(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey); 
        return cipher.doFinal(data);
    }

    //私钥解密
    public static byte[] encryptByPublic(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }
    //公钥加密
    public static byte[] decryptByPrivate(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey); 
        return cipher.doFinal(data);
    }

}
