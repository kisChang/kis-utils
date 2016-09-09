package com.kischang.simple_utils.utils;

/**
 * @author KisChang
 * @version 1.0
 */
public class ZipTest {

    public static void main(String[] args) throws Exception {
        String outPath = "/home/kischang/Desktop/out";
        String comFile = "/home/kischang/Desktop/123.zip";
        ZipUtilApache.unZip(comFile, outPath, "^\\S+\\.(html|sql)$");
    }

}
