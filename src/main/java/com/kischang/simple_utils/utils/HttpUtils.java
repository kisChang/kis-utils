package com.kischang.simple_utils.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * http 工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static HttpClient HTTP_CLIENT = null;
    static {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        HTTP_CLIENT = clientBuilder.build();
    }

    public static String getData(String url){
        return getData(HTTP_CLIENT, url);
    }

    public static String getData(HttpClient httpclient, String url){
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return IOUtils.toString(entity.getContent());
            }
            httpGet.abort();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    public static Map getMap(String url) {
        return getMap(HTTP_CLIENT, url);
    }

    public static Map getMap(HttpClient httpclient, String url) {
        try {
            return JacksonUtils.json2Map(getData(httpclient, url));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.EMPTY_MAP;
    }

    private HttpUtils() {
    }
}
