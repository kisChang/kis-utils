package com.kischang.simple_utils.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 主机工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class HostUtils {

    /**
     * 获取请求中 来访者的真实IP
     * //需要Nginx 配置中增加
     * proxy_set_header X-Real-IP $remote_addr;
     * proxy_set_header REMOTE-HOST $remote_addr;
     * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 获取请求中 访问的真实域名
     * //需要Nginx 配置中增加
     * proxy_set_header Host $host;
     */
    public static String getHost(HttpServletRequest request) {
        return request.getHeader("Host");
    }

    /**
     * 统计字符串中某个字符出现次数
     */
    public static int countChar(String str, char c) {
        if (isNullStr(str)){
            return 0;
        }
        int count = 0;
        for (char ch  : str.toCharArray()){
            if (ch == c){
                count ++;
            }
        }
        return count;
    }

    private static boolean isNullStr(String str){
        return str == null || "".equals(str);
    }

    private HostUtils() {
    }
}
