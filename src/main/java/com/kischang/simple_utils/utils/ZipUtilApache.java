package com.kischang.simple_utils.utils;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class ZipUtilApache {

    private static final Logger logger = LoggerFactory.getLogger(ZipUtilApache.class);
    private static final int BUFFER = 1024;

    /**
     * 压缩目录
     * @param destZipPath   打包输出文件父目录
     * @param destZipName   打包输出文件名称
     * @param packPath      打包目录
     * @param encoding      设置压缩编码.设置为GBK后在windows下就不会乱码了，如果要放到Linux或者Unix下请使用null
     */
    public static void zip(String packPath, String destZipPath, String destZipName, String encoding)
            throws Exception {
        // 创建压缩文件
        File destDir = new File(destZipPath);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        File destFile;
        if (destZipPath.endsWith(File.separator)){
            destFile = new File(destZipPath + destZipName);
        }else {
            destFile = new File(destZipPath + File.separator + destZipName);
        }
        if (destFile.exists()){
            destFile.delete();
        }

        zip(packPath, new FileOutputStream(destFile), destFile.getPath(), encoding);
    }

    public static void zip(String packPath, OutputStream outputStream, String filterName, String encoding)
            throws Exception {
        File inputFile = new File(packPath);
        // 1. 先生成压缩列表（防止目标目录还在这把压缩后的文件也打包进来）
        File[] zipFileList;
        if (inputFile.isDirectory()){
            zipFileList = inputFile.listFiles();
        }else {
            zipFileList = new File[]{ inputFile };
        }

        // 递归压缩方法
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(outputStream);
            if (encoding != null){
                out.setEncoding(encoding);
            }
            assert zipFileList != null;
            for (File file : zipFileList){
                if (filterName != null && filterName.equals(file.getPath())){
                    continue;
                }
                zip(out, file, file.getName());
            }
        }finally {
            IOUtils.closeQuietly(out);
        }
    }

    private static void zip(ZipOutputStream out, File packFile, String base)
            throws Exception {
        if (packFile.isDirectory()) { // 如果是文件夹，则获取下面的所有文件
            File[] fl = packFile.listFiles();
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base + "/"));// 此处要将文件写到文件夹中只用在文件名前加"/"再加文件夹名
            }
            base = base.length() == 0 ? "" : base + "/";
            assert fl != null;
            for (File aFl : fl) {
                zip(out, aFl, base + aFl.getName());
            }
        } else { // 如果是文件，则压缩
            logger.debug("Zipping: {} ", base);
            long startTime = System.currentTimeMillis();
            out.putNextEntry(new ZipEntry(base)); // 生成下一个压缩节点
            FileInputStream in = new FileInputStream(packFile); // 读取文件内容
            try {
                IOUtils.copy(in, out);
            }finally {
                out.closeEntry();
                IOUtils.closeQuietly(in);
            }
            logger.debug("Zip end:{}  Time：{}", packFile.getName(), System.currentTimeMillis() - startTime);
        }
    }

    /**
     * 解压Zip文件流，仅用于无法获得ZIP文件的情况下（如WEB上传文件）
     * 先创建临时文件，然后执行解压，最后删除临时文件
     *
     * @param zipIn        ZIP文件输入流
     * @param destFilePath 输出目录
     */
    public static void unZipCoding(InputStream zipIn, String destFilePath, String encoding, String filterRegex) throws Exception {
        unZipCoding(zipIn, encoding, filterRegex, new ZipFileHandlerDest(destFilePath));
    }

    public static void unZipCoding(InputStream zipIn, String encoding, String filterRegex, ZipFileHandler handler) throws Exception {
        long startTime = System.currentTimeMillis();

        String tempDir = PathUtils.appendFileName(System.getProperty("java.io.tmpdir"), "JavaUnZipTemp");

        logger.debug("获取临时目录：{}", tempDir);
        File tempDirFile = new File(tempDir);
        if (!tempDirFile.exists()) {
            tempDirFile.mkdirs();
            logger.debug("临时目录不存在，创建临时目录：{}", tempDir);
        }
        String tempZipPath = PathUtils.appendFileName(tempDir, System.currentTimeMillis());
        File tempZipFile = new File(tempZipPath);
        logger.debug("创建临时文件：{}", tempZipFile);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempZipFile);
            IOUtils.copy(zipIn, fos);
            unZipHandler(tempZipPath, encoding, filterRegex, handler);
        } finally {
            IOUtils.closeQuietly(fos);
            tempZipFile.delete();
            logger.debug("删除临时文件：{}" , tempZipFile);
        }
        logger.debug("Zip end 耗时：{}", (System.currentTimeMillis() - startTime));
    }

    /**
     * 解压缩zip文件
     *
     * @param fileName     要解压的文件名 包含路径 如："c:\\test.zip"
     * @param destFilePath 解压后存放文件的路径 如："c:\\temp"
     * @throws Exception
     */
    public static void unZip(String fileName, String destFilePath, String encoding, String filterRegex)
            throws Exception {
        unZipHandler(fileName, encoding, filterRegex, new ZipFileHandlerDest(destFilePath));
    }

    /**
     * 解压缩zip文件
     * @param fileName     要解压的文件名 包含路径 如："c:\\test.zip"
     */
    public static void unZipHandler(String fileName, String encoding, String filterRegex, ZipFileHandler handler)
            throws Exception {
        Pattern pattern = filterRegex == null ? null : Pattern.compile(filterRegex);
        //以“GBK”编码创建zip文件，用来处理winRAR压缩的文件。
        ZipFile zipFile = new ZipFile(fileName, encoding == null ? "GBK" : encoding);
        Enumeration emu = zipFile.getEntries();
        while (emu.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) emu.nextElement();
            if (pattern != null) {
                //不符合正则，跳过
                if (!pattern.matcher(entry.getName()).matches()) {
                    continue;
                }
            }
            handler.handle(entry, zipFile.getInputStream(entry));
        }
        zipFile.close();
    }

    public static interface ZipFileHandler {

        void handle(ZipEntry entry, InputStream in) throws IOException;

    }

    public static class ZipFileHandlerDest implements ZipFileHandler {

        private String destFilePath;

        public ZipFileHandlerDest(String destFilePath) {
            this.destFilePath = destFilePath;
        }

        @Override
        public void handle(ZipEntry entry, InputStream in) throws IOException {
            File file = new File(destFilePath + File.separator + entry.getName());
            if (entry.isDirectory()) {
                if (!file.exists()) {
                    file.mkdirs();
                }
                return;
            }
            BufferedInputStream bis = new BufferedInputStream(in);

            File parent = file.getParentFile();
            if (parent != null && (!parent.exists())) {
                parent.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
            byte[] buf = new byte[BUFFER];
            int len = 0;
            while ((len = bis.read(buf, 0, BUFFER)) != -1) {
                fos.write(buf, 0, len);
            }
            bos.flush();
            bos.close();
            bis.close();
        }
    }

}