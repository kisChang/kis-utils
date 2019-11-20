package com.kischang.simple_utils.taglib;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KisChang on 2015-06-02.
 * 常用的一些Taglib整理开发
 */
public class FunctionUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 截取文件名称
     * @param name  全名称
     */
    public static String subPicName(String name){
        return subPicName(name, "/");
    }

    /**
     * 截取文件名称
     * @param name  全名称
     * @param sn    分隔符
     */
    public static String subPicName(String name, String sn){
        if (name == null || !name.contains(sn)){
            return "";
        }else {
            return name.substring(name.lastIndexOf(sn) + sn.length());
        }
    }

    /**
     * 隐藏字符串的部分字符
     * @param str       隐藏的字符串
     * @param start     开始的位置，从0开始计数
     * @param ch        隐藏之前的字符串
     * @param replace   隐藏的字符替换成该字符
     * @return 隐藏后的字符串
     */
    public static String hidePartStrOnChar(String str, int start, char ch, String replace){
        if (str == null){
            return "";
        }
        if (str.length() < start){
            return str;
        }
        String tmp = str.substring(0, start);
        if (tmp.contains(String.valueOf(ch))){
            return str;
        }else {
            StringBuilder sb = new StringBuilder();
            sb.append(tmp);
            sb.append(replace);
            sb.append(str, str.indexOf(ch), str.length());
            return sb.toString();
        }
    }

    /**
     * 隐藏字符串的部分字符
     * @param str       隐藏的字符串
     * @param start     开始的位置，从0开始计数
     * @param hideNum   隐藏几个字符
     * @param replace   隐藏的字符替换成该字符
     * @return 隐藏后的字符串
     */
    public static String hidePartStrOnNum(String str, int start, int hideNum, String replace){
        if (str == null){
            return "";
        }
        if (str.length() < start){
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str, 0, start);
        if( str.length() < (start + hideNum) ){
            appendRepStr(sb, str.length() - start, replace);
            return sb.toString();
        }else {
            appendRepStr(sb, hideNum, replace);
            sb.append(str.substring(start + hideNum));
            return sb.toString();
        }
    }

    private static void appendRepStr(StringBuilder sb, int num, String replace) {
        for(int i = 0 ; i < num ; i ++){
            sb.append(replace);
        }
    }

    /**
     * 设置时间格式化字符串
     * @param formatStr 时间字符串
     * @return
     */
    public static String setFormatPattern(String formatStr){
        try{
            format = new SimpleDateFormat(formatStr);
        }catch (Exception e){
            return e.getMessage();
        }
        return null;
    }

    /**
     * 根据当前页面的格式化字符串格式化时间对象
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        if (date == null){
            return "";
        }
        return format.format(date);
    }

    /**
     * URL  Encode
     * @param str       URL
     * @param charSet   字符编码，默认UTF-8
     * @return
     */
    public static String urlEncode(String str,String charSet) throws UnsupportedEncodingException {
        if (charSet == null){
            return URLEncoder.encode(str,"utf-8");
        }
        return URLEncoder.encode(str,charSet);
    }

    /**
     * 将输出文件截断，只显示部分
     * @param escapeXml 是否xss
     * @param obj       内容
     * @param len       截取长度
     * @param suffix    后缀
     */
    public static String subString(boolean escapeXml, Object obj,
                                 int len,String suffix) throws IOException {
        StringWriter w = new StringWriter();
        String value;
        //将需要输出的内容转成字符串
        if (obj instanceof Reader) {
            Reader reader = (Reader) obj;
            StringWriter writer = new StringWriter();
            char[] buf = new char[4096];
            int count;
            while ((count = reader.read(buf, 0, 4096)) != -1) {
                writer.write(buf,0,count);
            }
            value = writer.toString();
        } else {
            value = obj == null ? "" : obj.toString();
        }
        String temp;
        if(value.length() > len){
            temp = value.substring(0,len) + ( suffix==null?"":suffix );
        }else if(value.length() <= len){
            temp = value;
        }else{
            temp = "";
        }

        if (escapeXml) {
            writeEscapedXml(temp.toCharArray(), temp.length(), w);
        } else {
            w.write(temp);
        }
        return w.toString();
    }


    public static final int HIGHEST_SPECIAL = '>';
    public static char[][] specialCharactersRepresentation = new char[HIGHEST_SPECIAL + 1][];
    static {
        specialCharactersRepresentation['&'] = "&amp;".toCharArray();
        specialCharactersRepresentation['<'] = "&lt;".toCharArray();
        specialCharactersRepresentation['>'] = "&gt;".toCharArray();
        specialCharactersRepresentation['"'] = "&#034;".toCharArray();
        specialCharactersRepresentation['\''] = "&#039;".toCharArray();
    }

    private static void writeEscapedXml(char[] buffer, int length, Writer w) throws IOException {
        int start = 0;
        for (int i = 0; i < length; i++) {
            char c = buffer[i];
            if (c <= HIGHEST_SPECIAL) {
                char[] escaped = specialCharactersRepresentation[c];
                if (escaped != null) {
                    if (start < i) {
                        w.write(buffer, start, i - start);
                    }
                    w.write(escaped);
                    start = i + 1;
                }
            }
        }
        if (start < length) {
            w.write(buffer, start, length - start);
        }
    }
}
