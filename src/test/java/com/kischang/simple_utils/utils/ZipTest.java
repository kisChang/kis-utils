package com.kischang.simple_utils.utils;

/**
 * @author KisChang
 * @version 1.0
 */
public class ZipTest {

    public static void main(String[] args) throws Exception {
        String outPath = "/home/kischang/Desktop/zipTest";
        ZipUtilApache.zip(outPath, outPath, "tmp.zip", null);
    }

}
