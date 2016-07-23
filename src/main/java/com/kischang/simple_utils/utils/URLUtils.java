package com.kischang.simple_utils.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * url 编码解码工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class URLUtils {

    private static final String DEF_CHARSET = "UTF-8";

    public static String genUrl(String queryUrl, String param){
        try {
            return queryUrl + "?" + URLDecoder.decode(param, DEF_CHARSET);
        } catch (UnsupportedEncodingException ignored) {}
        return null;
    }

    public static String encodeUrl(String param){
        try {
            return URLEncoder.encode(param, DEF_CHARSET);
        } catch (UnsupportedEncodingException ignored) {}
        return null;
    }

    private URLUtils() {
    }
}
