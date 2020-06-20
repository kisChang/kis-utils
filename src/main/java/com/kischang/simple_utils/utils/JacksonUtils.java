package com.kischang.simple_utils.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KisChang on 15-7-17.
 * 封装Jackson JSON工具类的方法，简化使用
 */
public class JacksonUtils {

    private static final ObjectMapper omBase = new ObjectMapper();

    private static final ObjectMapper OmIgnoreUnknow = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    static {
        OmIgnoreUnknow.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getOmBase(){
        return omBase;
    }

    public static ObjectMapper getOmIgnoreUnknow(){
        return OmIgnoreUnknow;
    }

    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2Json(Object obj){
        try {
            return omBase.writeValueAsString(obj);
        } catch (IOException ignored) {
        }
        return "";
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T json2Pojo(ObjectMapper om, String jsonStr, Class<T> clazz){
        try {
            return om.readValue(jsonStr, clazz);
        } catch (IOException ignored) {
        }
        return null;
    }
    public static <T> T json2Pojo(String jsonStr, Class<T> clazz){
        return json2Pojo(getOmBase(), jsonStr, clazz);
    }

    /**
     * json string convert to map
     */
    public static Map json2Map(ObjectMapper om, String jsonStr){
        try {
            return om.readValue(jsonStr, Map.class);
        } catch (IOException ignored) {
        }
        return null;
    }
    public static Map json2Map(String jsonStr){
        return json2Map(getOmBase(), jsonStr);
    }

    /**
     * 推荐的方法
     * @param jsonStr   JSON字符串
     * @param type      目标类型
     * @param <T>       目标
     * @return          结果
     */
    public static <T> T jsonToType(ObjectMapper om, String jsonStr,TypeReference<T> type){
        if (jsonStr == null || "".equals(jsonStr.trim())){
            return null;
        }
        try {
            return om.readValue(jsonStr,type);
        } catch (IOException ignored) { }
        return null;
    }

    public static <T> T jsonToType(String jsonStr,TypeReference<T> type){
        return jsonToType(getOmBase(), jsonStr, type);
    }

    public static <K,V> Map<K, V> json2EasyMap(ObjectMapper om, String jsonStr, Class<K> clazzK, Class<V> clazzV){
        Map<K, V> map = null;
        try {
            map = om.readValue(jsonStr, new TypeReference<Map<K, V>>() {});
        } catch (IOException ignored) {
        }
        return map;
    }
    public static <K,V> Map<K, V> json2EasyMap(String jsonStr, Class<K> clazzK, Class<V> clazzV){
        return json2EasyMap(getOmBase(), jsonStr, clazzK, clazzV);
    }

    public static <K,V> Map<K, List<V>> json2ArrMap(ObjectMapper om, String data, Class<K> kClass, Class<V> vClass) {
        Map<K, List<V>> map = null;
        try {
            map = om.readValue(data, new TypeReference<Map<K, List<V>>>() {});
        } catch (IOException ignored) {
        }
        return map;
    }
    public static <K,V> Map<K, List<V>> json2ArrMap(String data, Class<K> kClass, Class<V> vClass) {
        return json2ArrMap(getOmBase(), data, kClass, vClass);
    }

    public static <K,V> Map<K, V> json2PojoMap(ObjectMapper om, String jsonStr, Class<K> clazzK, Class<V> clazzV){
        Map<K, Map<String, Object>> map = null;
        try {
            map = om.readValue(jsonStr,
                    new TypeReference<Map<K, Map<String, Object>>>() {
                    });
        } catch (IOException ignored) {
            return null;
        }
        Map<K, V> result = new HashMap<K, V>();
        for (Map.Entry<K, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazzV));
        }
        return result;
    }
    public static <K,V> Map<K, V> json2PojoMap(String jsonStr, Class<K> clazzK, Class<V> clazzV){
        return json2PojoMap(getOmBase(), jsonStr, clazzK, clazzV);
    }

    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(ObjectMapper om, Map map, Class<T> clazz) {
        return om.convertValue(map, clazz);
    }

    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return map2pojo(getOmBase(), map, clazz);
    }
}
