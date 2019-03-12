package com.kischang.simple_utils.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 各种目录工具方法
 *
 * @author KisChang
 * @version 1.0
 */
public class PathUtils {

    /**动态添加library路径*/
    public static void addLibraryDir(String path) throws IOException {
        try {
            // This enables the java.library.path to be modified at runtime
            // From a Sun engineer at http://forums.sun.com/thread.jspa?threadID=707176
            //
            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            String[] paths = (String[])field.get(null);
            for (int i = 0; i < paths.length; i++) {
                if (path.equals(paths[i])) {
                    return;
                }
            }
            String[] tmp = new String[paths.length+1];
            System.arraycopy(paths,0,tmp,0,paths.length);
            tmp[paths.length] = path;
            field.set(null,tmp);
            System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + path);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }

    /**获取程序运行目录*/
    private static String RUN_PATH = null;
    public synchronized static String getRunPath() {
        return getRunPath(PathUtils.class);
    }

    public synchronized static String getRunPath(Class clzss) {
        if (RUN_PATH == null){
            URL url = clzss.getProtectionDomain().getCodeSource().getLocation();
            String filePath = "";
            try {
                filePath = URLDecoder.decode(url.getFile(), "utf-8");// 转化为utf-8编码
            } catch (Exception ignored) {
            }
            //jar文件的话，可能是这个开头，需要去除
            if ("jar".equals(url.getProtocol()) && filePath.startsWith("file:")){
                filePath = filePath.substring(5);
            }

            if (filePath.endsWith(".jar")) {// 可执行jar包运行的结果里包含".jar"
                // 截取路径中的jar包名
                filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
            }
            String tmp = ".jar!/BOOT-INF/classes!/";
            if (filePath.endsWith(tmp)) {// spring打包的是这个后缀
                // 截取路径中的多余部分
                filePath = filePath.substring(0, filePath.lastIndexOf(tmp));
            }
            File file = new File(filePath);
            RUN_PATH = file.getAbsolutePath();//得到windows下的正确路径
        }
        return RUN_PATH;
    }

    public static String linkPath(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String once : paths) {
            sb.append(once);
            if (!once.endsWith(File.separator)) {
                sb.append(File.separator);
            }
        }
        return sb.toString();
    }

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

    public static String appendFileName(StringBuilder sb, Object[] fileName) {
        for (int i=0 ; i< fileName.length ; i++) {
            String str = String.valueOf(fileName[i]);
            if (File.separator.equals(str)){
                sb.append(File.separator);
            } else {
                str = str.replace("../", "").replace("./", "");
                sb.append(str);
                if (i < fileName.length-1){
                    sb.append(File.separator);
                }
            }
        }
        return sb.toString();
    }

    public static String appendFileName(Object... fileName) {
        StringBuilder sb = new StringBuilder();
        return appendFileName(sb, fileName);
    }

}
