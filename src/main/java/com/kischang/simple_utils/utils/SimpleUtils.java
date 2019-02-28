package com.kischang.simple_utils.utils;

import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.StorageManager;
import com.kischang.simple_utils.formbean.ResponseData;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 常用方法
 *
 * @author KisChang
 * @version 1.0
 */
public class SimpleUtils {

    public static State saveBinaryFile(byte[] data, String savePath){
        return new StorageManager().saveBinaryFile(data, savePath, "");
    }

    /**
     * 排序，默认升序
     */
    public static <T,V extends Comparable<V>> List<Map.Entry<T, V>> sortMapByValue(Map<T, V> map) {
        return sortMapByValue(map, true);
    }
    /**
     * 排序，true升序 false 降序
     */
    public static <T,V extends Comparable<V>> List<Map.Entry<T, V>> sortMapByValue(Map<T, V> map, final boolean asc) {
        ArrayList<Map.Entry<T, V>> rv = new ArrayList<Map.Entry<T, V>>(map.entrySet());
        Collections.sort(rv, new Comparator<Map.Entry<T, V>>() {
            public int compare(Map.Entry<T, V> o1, Map.Entry<T, V> o2) {
                if (asc){
                    return o1.getValue().compareTo(o2.getValue());
                }else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });
        return rv;
    }


    public static <T> Map<Long,T> toIdMap(List<T> objList, String pn) {
        Map<Long, T> map = new HashMap<>();
        if (objList != null && !objList.isEmpty()){
            for (T obj : objList){
                try {
                    Long id = Long.valueOf(BeanUtils.getProperty(obj, pn));
                    map.put(id, obj);
                } catch (Exception ignored) {}
            }
        }
        return map;
    }

    public static boolean checkAjax(ServletRequest request){
        HttpServletRequest hr = (HttpServletRequest) request;
        return ("true".equalsIgnoreCase(request.getParameter("ajax")))
                || "XMLHttpRequest".equalsIgnoreCase(hr.getHeader("X-Requested-With"));
    }


    /**返回JSON数据*/
    public static void writeJsonToResponse(ResponseData rm, ServletResponse resp) {
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            IOUtils.write(rm.toString(), out);
            out.flush();
        } catch (IOException ignored) {
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static String getUid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        return getRandomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", length);
    }

    public static String getRandomString(String base, int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
