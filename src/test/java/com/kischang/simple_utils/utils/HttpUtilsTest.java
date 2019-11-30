package com.kischang.simple_utils.utils;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KisChang
 * @date 2019-11-30
 */
public class HttpUtilsTest {

    private static final String wechat_bot = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=";


    public static void main(String[] args) throws UnsupportedEncodingException {
        String botKey = "861c6ef9-4cfe-4dd0-b3e2-0520b8c60e4a";
        Map<String, Object> params = new HashMap<>();
        params.put("msgtype", "text");
        params.put("text", Collections.singletonMap("content", "hello world"));

        String rv = HttpUtils.postData(wechat_bot + botKey, new StringEntity(JacksonUtils.obj2Json(params)));
        System.out.println(rv);
    }

}
