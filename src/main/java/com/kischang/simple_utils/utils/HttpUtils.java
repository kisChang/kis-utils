package com.kischang.simple_utils.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * http 工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final Charset def_charset = Charset.forName("UTF-8");
    private static HttpClient HTTP_CLIENT = null;
    static {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        HTTP_CLIENT = clientBuilder.build();
    }

    public static String getData(String url){
        return getData(url, def_charset);
    }
    public static String getData(String url, Charset charset){
        return getData(HTTP_CLIENT, url, charset);
    }

    public static String getData(HttpClient httpclient, String url){
        return getData(httpclient, url, def_charset);
    }
    public static String getData(HttpClient httpclient, String url, Charset charset){
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, charset);
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

    public static String postData(String url, Map<String, Object> data) {
        return postData(url, data, def_charset);
    }

    public static String postData(String url, Map<String, Object> data, Charset charset) {
        return postData(HTTP_CLIENT, url, data, charset);
    }


    public static String postData(HttpClient httpclient, String url, Map<String, Object> data) {
        return postData(httpclient, url, data, def_charset);
    }
    public static String postData(HttpClient httpclient, String url, Map<String, Object> data, Charset charset) {
        HttpPost post = new HttpPost(url);

        try {
            if (data != null && !data.isEmpty()){
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for(Map.Entry<String, Object> entry : data.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
                post.setEntity(new UrlEncodedFormEntity(nvps, charset));
            }

            HttpResponse response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, charset);
            }
            post.abort();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    public static Map postMap(String url, Map<String, Object> data) {
        return postMap(HTTP_CLIENT, url, data);
    }

    public static Map postMap(HttpClient httpclient, String url, Map<String, Object> data) {
        try {
            return JacksonUtils.json2Map(postData(httpclient, url, data));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.EMPTY_MAP;
    }

    private HttpUtils() {
    }
}
