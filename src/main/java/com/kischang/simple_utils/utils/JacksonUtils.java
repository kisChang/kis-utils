package com.kischang.simple_utils.utils;

import com.fasterxml.jackson.core.type.TypeReference;
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

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2Json(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException ignored) {
        }
        return "";
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T json2Pojo(String jsonStr, Class<T> clazz){
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * json string convert to map
     */
    public static Map json2Map(String jsonStr){
        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * 推荐的方法
     * @param jsonStr   JSON字符串
     * @param type      目标类型
     * @param <T>       目标
     * @return          结果
     */
    public static <T> T jsonToType(String jsonStr,TypeReference<T> type){
        if (jsonStr == null || "".equals(jsonStr.trim())){
            return null;
        }
        try {
            return objectMapper.readValue(jsonStr,type);
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2Map(String jsonStr, Class<T> clazz){
        Map<String, Map<String, Object>> map = null;
        try {
            map = objectMapper.readValue(jsonStr,
                    new TypeReference<Map<String, T>>() {
                    });
        } catch (IOException ignored) {
            return null;
        }
        Map<String, T> result = new HashMap<String, T>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    public static <K,V> Map<K, V> json2EasyMap(String jsonStr, Class<K> clazzK, Class<V> clazzV){
        Map<K, V> map = null;
        try {
            map = objectMapper.readValue(jsonStr, new TypeReference<Map<K, V>>() {});
        } catch (IOException ignored) {
        }
        return map;
    }

    public static <K,V> Map<K, List<V>> json2ArrMap(String data, Class<K> kClass, Class<V> vClass) {
        Map<K, List<V>> map = null;
        try {
            map = objectMapper.readValue(data, new TypeReference<Map<K, List<V>>>() {});
        } catch (IOException ignored) {
        }
        return map;
    }

    public static <K,V> Map<K, V> json2PojoMap(String jsonStr, Class<K> clazzK, Class<V> clazzV){
        Map<K, Map<String, Object>> map = null;
        try {
            map = objectMapper.readValue(jsonStr,
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

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2List(String jsonArrayStr, Class<T> clazz){
        List<Map<String, Object>> list = null;
        try {
            list = objectMapper.readValue(jsonArrayStr,
                    new TypeReference<List<T>>() {
                    });
        } catch (IOException ignored) {
            return null;
        }
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }

    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }
}
