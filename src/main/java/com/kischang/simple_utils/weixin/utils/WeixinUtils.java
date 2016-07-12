package com.kischang.simple_utils.weixin.utils;

import com.kischang.simple_utils.utils.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 微信工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class WeixinUtils {
    public static String buildOauthUrl(String wxAppId, String redirectUrl, String state) {
        try {
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                    "appid=" + wxAppId +
                    "&redirect_uri=" + URLEncoder.encode(redirectUrl, "utf-8") +
                    "&response_type=code" +
                    "&scope=snsapi_userinfo";
            if (state != null){
                url = url + "&state=" + state;
            }
            return  url + "#wechat_redirect";
        } catch (UnsupportedEncodingException ignored) {
        }
        return null;
    }

    /**
     * 通过code换取网页授权access_token
     */
    public static Map getAccessToken(String appId, String appSecret, String code){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + appId +
                "&secret=" + appSecret +
                "&code=" + code +
                "&grant_type=authorization_code";
        return HttpUtils.getMap(url);
    }

    /**
     * 刷新access_token
     */
    public static Map refreshAccessToken(String appId, String refresh_token){
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="
                + appId + "&grant_type=refresh_token&refresh_token=" + refresh_token;

        return HttpUtils.getMap(url);
    }


    /**
     * 拉取用户信息
     */
    public static Map getUserInfo(String access_token, String openid){
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";

        return HttpUtils.getMap(url);
    }


    /**
     * 检验授权凭证（access_token）是否有效
     */
    public static boolean checkAccessToken(String access_token, String openid){
        String url = "https://api.weixin.qq.com/sns/auth?access_token=" + access_token + "&openid=" + openid;
        Map map = HttpUtils.getMap(url);
        try {
            if (map.containsKey("errcode")){
                int errcode = Integer.parseInt(map.get("errcode").toString());
                if (errcode == 0) {
                    return true;
                }
            }
        }catch (Exception ignored){}
        return false;
    }
}
