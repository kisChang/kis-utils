package com.temsoft.simple_utils.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 各种目录工具方法
 *
 * @author KisChang
 * @version 1.0
 */
public class PathUtils {

    public static String getWebInfPath(String... fileName){
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        File file;
        if(url == null){
            throw new RuntimeException("获取WEB-INF路径错误！！！");
        }
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("获取WEB-INF路径错误！！！",e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(file.getParentFile().getPath());
        sb.append(File.separator);
        return appendFileName(sb,fileName);
    }

    public static String getWebPath(String... fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        File file;
        if(url == null){
            throw new RuntimeException("获取WebRoot路径错误！！！");
        }
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("获取WebRoot路径错误！！！",e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(file.getParentFile().getParentFile().getPath());
        sb.append(File.separator);
        return appendFileName(sb,fileName);
    }

    //存储ClassPath不用每次都获取
    private static String classpath = null;
    /**
     * 修复特殊路径时无法正常访问的Bug(如在路径中含有空格)
     */
    public static String getClassPath(String... fileName){
        while (classpath == null){
            URL url = Thread.currentThread().getContextClassLoader().getResource("");
            File file;
            if(url == null){
                throw new RuntimeException("获取项目配置文件路径错误！！！");
            }
            try {
                file = new File(url.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                throw new RuntimeException("获取项目配置文件路径错误！！！",e);
            }
            classpath = file.getPath() + File.separator;
        }
        if(fileName == null || fileName.length == 0){
            return classpath;
        }
        return appendFileName(new StringBuilder(classpath),fileName);
    }

    public static String appendFileName(StringBuilder sb, String[] fileName) {
        for (String str : fileName){
            sb.append(str);
        }
        return sb.toString().replace("../", "").replace("./", "");
    }

}
