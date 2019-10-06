package com.kischang.simple_utils.utils;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * 压缩工具类
 */
public class CompressUtils {

    public static void main(String[] args) throws Exception {
        CompressUtils cu = new CompressUtils(CompressType.GZIP);

        InputStream in = new FileInputStream("D:\\MyProgram\\apache-tomcat-8.5.33\\temp\\tmp_cl_bak_1570357562342.db.sql");
        OutputStream out = new FileOutputStream("D:\\MyProgram\\apache-tomcat-8.5.33\\temp\\tmp.gz");
        cu.compress(in, out);
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);

        InputStream in2 = new FileInputStream("D:\\MyProgram\\apache-tomcat-8.5.33\\temp\\tmp.gz");
        OutputStream out2 = new FileOutputStream("D:\\MyProgram\\apache-tomcat-8.5.33\\temp\\tmp.sql");
        cu.decompress(in2, out2);
    }

    private CompressType compressType = CompressType.GZIP;

    public CompressUtils(CompressType type) {
        this.compressType = type;
    }

    public enum CompressType {
        GZIP("gz"), BZIP2("bzip2");

        private String value;

        CompressType(String val) {
            this.value = val;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 压缩
     */
    public byte[] compression(byte[] message) {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        byte[] output = null;
        try {
            bais = new ByteArrayInputStream(message);
            baos = new ByteArrayOutputStream();
            compress(bais, baos);
            output = baos.toByteArray();
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(bais);
        }
        return output;
    }

    /**
     * 解压缩
     */
    public byte[] decompression(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] data = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            baos = new ByteArrayOutputStream();
            decompress(bais, baos);
            data = baos.toByteArray();
            baos.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(bais);
        }
        return data;
    }


    /**
     * 数据流压缩
     */
    public void compress(InputStream is, OutputStream os) throws Exception {
        try (CompressorOutputStream gos = new CompressorStreamFactory()
                .createCompressorOutputStream(compressType.getValue(), os)) {
            IOUtils.copy(is, gos);
        }
    }

    /**
     * 获取压缩数据流
     */
    public OutputStream compress(OutputStream os) throws CompressorException {
        return new CompressorStreamFactory().createCompressorOutputStream(compressType.getValue(), os);
    }

    /**
     * 数据流解压缩
     */
    public void decompress(InputStream is, OutputStream os)
            throws Exception {
        try (CompressorInputStream gis = new CompressorStreamFactory()
                .createCompressorInputStream(compressType.getValue(), is)) {
            IOUtils.copy(gis, os);
        }
    }


    /**
     * 数据流解压缩
     */
    public InputStream decompress(InputStream in) throws CompressorException {
        return new CompressorStreamFactory().createCompressorInputStream(compressType.getValue(), in);
    }
}
