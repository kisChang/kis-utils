package com.kischang.simple_utils.utils;

import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;


public class WxBizDataCryptUtils {

   // https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
   public static String jscode2Session(String wxAppletsAppId, String wxAppletsAppSecret, String jscode) {
      String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code"
              , wxAppletsAppId
              , wxAppletsAppSecret
              , jscode);
      return HttpUtils.getData(url);
   }

   /**
    * 微信数据解密
    *
    * @param encryptedData
    * @param iv
    * @return
    */
   public static JSONObject decryptData(String encryptedData, String iv, String sessionKey) {

      // 被加密的数据
      byte[] dataByte = Base64Utils.decodeFromString(encryptedData);
      // 加密秘钥
      byte[] keyByte = Base64Utils.decodeFromString(sessionKey);
      // 偏移量
      byte[] ivByte = Base64Utils.decodeFromString(iv);
      try {
         // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
         int base = 16;
         if (keyByte.length % base != 0) {
            int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
            keyByte = temp;
         }
         // 初始化
         Security.addProvider(new BouncyCastleProvider());
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
         SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
         AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
         parameters.init(new IvParameterSpec(ivByte));
         cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
         byte[] resultByte = cipher.doFinal(dataByte);
         if (null != resultByte && resultByte.length > 0) {
            String result = new String(resultByte, StandardCharsets.UTF_8);
            return JSONObject.parseObject(result);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;

   }

   private WxBizDataCryptUtils() {
   }

}
