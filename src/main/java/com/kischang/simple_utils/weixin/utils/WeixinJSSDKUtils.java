package com.kischang.simple_utils.weixin.utils;

import com.kischang.simple_utils.utils.HttpUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Map;

/**
 * JSSDK 工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class WeixinJSSDKUtils {


    private static HttpClient HTTP_CLIENT = null;
    static {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.disableAuthCaching();
        HTTP_CLIENT = clientBuilder.build();
    }

    /**
     * 获取JSSDK 的 access_token
     */
    public static String getClientCredential(String appId, String appSecret){
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        Map map = HttpUtils.getMap(HTTP_CLIENT, url);
        if (map.containsKey("access_token")){
            return String.valueOf(map.get("access_token"));
        }else {
            return null;
        }
    }

    /**
     * 获取JSSDK 的 jsapi_ticket
     */
    public static String getJsapiTicket(String accessToken){
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
        Map map = HttpUtils.getMap(HTTP_CLIENT, url);
        if (map.containsKey("ticket")){
            return String.valueOf(map.get("ticket"));
        }else {
            return null;
        }
    }

    private static volatile String access_token = null;
    private static volatile String js_ticket = null;
    private static volatile long js_time = -1;
    /**
     * 获取JSSDK 的 jsapi_ticket（会缓存 accessToken）
     */
    public static String getJsapiTicket(String appId, String appSecret){
        return getJsapiTicket(appId, appSecret, 0);
    }

    private static String getJsapiTicket(String appId, String appSecret, int loop){
        if (js_ticket != null &&
                (System.currentTimeMillis() - js_time) < 7200 * 1000){
            return js_ticket;
        }
        if (loop > 3){
            return null;
        }
        if (access_token == null){
            access_token = getClientCredential(appId, appSecret);
        }
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
        Map map = HttpUtils.getMap(HTTP_CLIENT, url);
        if (map.containsKey("ticket")){
            js_ticket = String.valueOf(map.get("ticket"));
            js_time = System.currentTimeMillis();
            return js_ticket;
        }else {
            int errorCode = Integer.parseInt(String.valueOf(map.get("errcode")));
            if (errorCode == 42001 || errorCode == 40001 || errorCode == 40014 || errorCode == 41001){
                access_token = null;
                return getJsapiTicket(appId, appSecret, ++loop);
            }
            return null;
        }
    }



    private WeixinJSSDKUtils() {
    }
}
