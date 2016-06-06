package com.temsoft.simple_utils.utils;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.*;

/**
 * 压缩工具类
 */
public class CompressUtils {
    private static final int BUFFER = 1024;

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
        ByteArrayInputStream bais =null;
        ByteArrayOutputStream baos = null;
        byte[] output = null;
        try {
            bais = new ByteArrayInputStream(message);
            baos = new ByteArrayOutputStream();
            compress(bais, baos);
            output  = baos.toByteArray();
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bais != null){
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        try{
            bais = new ByteArrayInputStream(bytes);
            baos = new ByteArrayOutputStream();
            decompress(bais, baos);
            data = baos.toByteArray();
            baos.flush();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bais != null){
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }


    /**
     * 数据流压缩
     */
    public void compress(InputStream is, OutputStream os) throws Exception {
        CompressorOutputStream gos = null;
        try{
            gos = new CompressorStreamFactory()
                    .createCompressorOutputStream(compressType.getValue(), os);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = is.read(data, 0, BUFFER)) != -1) {
                gos.write(data, 0, count);
            }
            gos.flush();
        }catch (Exception e){
            throw e;
        }finally {
            if(gos != null)
                gos.close();
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
        CompressorInputStream gis = null;
        try{
            gis = new CompressorStreamFactory()
                    .createCompressorInputStream(compressType.getValue(),is);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = gis.read(data, 0, BUFFER)) != -1) {
                os.write(data, 0, count);
            }
        }catch (Exception e){
            throw e;
        }finally {
            if(gis != null)
                gis.close();
        }
    }


    /**
     * 数据流解压缩
     */
    public InputStream decompress( InputStream in) throws CompressorException {
        return new CompressorStreamFactory().createCompressorInputStream(compressType.getValue(), in);
    }
}
