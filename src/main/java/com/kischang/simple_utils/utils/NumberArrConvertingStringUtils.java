package com.kischang.simple_utils.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Integer、Long 数组和字符串的转换工具
 *
 * @author KisChang
 * @version 1.0
 * @date 2015年10月12日
 * @since 1.0
 */
public class NumberArrConvertingStringUtils {

    public static String toStr(Collection<? extends Number> arr){
        if (arr == null || arr.isEmpty()){
            return "";
        }
        StringBuilder s = new StringBuilder();
        for(Number temp : arr) {
            s.append(" ");
            s.append(temp);
            s.append(",");
        }
        return s.toString();
    }

    public static <T extends Number> Collection<T> toArr(String str, Class<T> type){
        if(str == null || "".equals(str)) {
            return new ArrayList<T>();
        }
        Collection<T> list = new ArrayList<T>();
        TypeHandler<T> handler = TypeHandlerFactory.build(type);
        for(String temp : str.split(",")) {
            if(temp == null || "".equals(temp)) {
                continue;
            }
            temp = temp.trim();
            list.add(handler.parse(temp));
        }
        return list;
    }

    private interface TypeHandler<T>{
        T parse(String str);
    }

    private static class TypeHandlerFactory {

        @SuppressWarnings("unchecked")
        static <T extends Number> TypeHandler<T> build(Class<T> type) {
            if (type.equals(Integer.TYPE) || type.equals(Integer.class)){
                return (TypeHandler<T>) new IntTypeHandler();
            } else if (type.equals(Long.TYPE) || type.equals(Long.class)){
                return (TypeHandler<T>) new LongTypeHandler();
            } else if (type.equals(Double.TYPE) || type.equals(Double.class)){
                return (TypeHandler<T>) new DoubleTypeHandler();
            } else {
                throw new RuntimeException("Not support type :" + type);
            }
        }
    }

    private static class IntTypeHandler implements TypeHandler<Integer>{
        public Integer parse(String str) {
            return Integer.parseInt(str);
        }
    }

    private static class LongTypeHandler implements TypeHandler<Long>{
        public Long parse(String str) {
            return Long.parseLong(str);
        }
    }

    private static class DoubleTypeHandler implements TypeHandler<Double>{
        public Double parse(String str) {
            return Double.parseDouble(str);
        }
    }


}
