package com.kischang.simple_utils.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ZipUtilApache {

    private static final Logger logger = Logger.getLogger(ZipUtilApache.class.getName());
    private static final int BUFFER = 1024;

    public static void zip(String destDirPath, String inputPath)
            throws Exception {
        File inputFile = new File(inputPath);
        // 创建压缩文件
        File destDir = new File(destDirPath);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        File destFile = new File(destDir + File.separator + inputFile.getName()
                + ".zip");
        // 递归压缩方法
        ZipOutputStream out = new ZipOutputStream(
                new FileOutputStream(destFile));
        // 设置压缩编码.设置为GBK后在windows下就不会乱码了，如果要放到Linux或者Unix下就不要设置了
        out.setEncoding("GBK");
        zip(out, inputFile, "");
        System.out.println("zip done");
        out.close();
    }

    private static void zip(ZipOutputStream out, File f, String base)
            throws Exception {
        logger.log(Level.INFO,"Zipping   " + f.getName());
        long stratTime = System.currentTimeMillis();
        if (f.isDirectory()) { // 如果是文件夹，则获取下面的所有文件
            File[] fl = f.listFiles();
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base + "/"));// 此处要将文件写到文件夹中只用在文件名前加"/"再加文件夹名
            }
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else { // 如果是文件，则压缩
            out.putNextEntry(new ZipEntry(base)); // 生成下一个压缩节点
            FileInputStream in = new FileInputStream(f); // 读取文件内容
            int len;
            byte[] buf = new byte[BUFFER];
            while ((len = in.read(buf, 0, BUFFER)) != -1) {
                out.write(buf, 0, len); // 写入到压缩包
            }
            in.close();
            out.closeEntry();
        }
        logger.log(Level.INFO,"Zip end  :" + f.getName()+"  耗时："+(System.currentTimeMillis()-stratTime));
    }

    /**
     * 解压Zip文件流，仅用于无法获得ZIP文件的情况下（如WEB上传文件）
     * 先创建临时文件，然后执行解压，最后删除临时文件
     *
     * @param zipIn        ZIP文件输入流
     * @param zipFileName  ZIP文件名称
     * @param destFilePath 输出目录
     */
    public static void unZip(InputStream zipIn, String zipFileName, String destFilePath, String filterRegex) {
        long stratTime = System.currentTimeMillis();
        String tempDir = System.getProperty("java.io.tmpdir")+File.separator+"JavaUnZipTemp"+File.separator;
        logger.log(Level.INFO,"获取临时目录:"+tempDir);
        File tempDirFile = new File(tempDir);
        if(!tempDirFile.exists()){
            tempDirFile.mkdirs();
            logger.log(Level.INFO,"临时目录不存在，创建临时目录:"+tempDir);
        }
        String tempZipPath = tempDir + File.separator + System.currentTimeMillis() + zipFileName;
        File tempZipFile = new File(tempZipPath);
        logger.log(Level.INFO,"创建临时文件:"+tempZipFile);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempZipFile);
            int len = 0;
            byte[] data = new byte[BUFFER];
            while ((len = zipIn.read(data)) != -1) {
                fos.write(data, 0, len);
            }
            fos.close();
            fos = null;
            String temp = destFilePath + File.separator + zipFileName + File.separator;
            logger.log(Level.INFO,"临时文件写入完成，开始解压:"+tempZipFile+"  至："+temp);
            unZip(tempZipPath, temp, filterRegex);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            tempZipFile.delete();
            logger.log(Level.INFO,"删除临时文件:"+tempZipFile);
        }
        logger.log(Level.INFO,"Zip end  :" + zipFileName+"  耗时："+(System.currentTimeMillis()-stratTime));
    }

    /**
     * 解压缩zip文件
     *
     * @param fileName     要解压的文件名 包含路径 如："c:\\test.zip"
     * @param destFilePath 解压后存放文件的路径 如："c:\\temp"
     * @throws Exception
     */
    public static void unZip(String fileName, String destFilePath, String filterRegex)
            throws Exception {
        Pattern pattern = filterRegex == null ? null : Pattern.compile(filterRegex);
        ZipFile zipFile = new ZipFile(fileName, "GBK"); // 以“GBK”编码创建zip文件，用来处理winRAR压缩的文件。
        Enumeration emu = zipFile.getEntries();
        while (emu.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) emu.nextElement();
            if (pattern != null){
                //不符合正则，跳过
                if (!pattern.matcher(entry.getName()).matches()){
                    continue;
                }
            }
            File file = new File(destFilePath + File.separator + entry.getName());
            if (entry.isDirectory()) {
                if (!file.exists()) {
                    file.mkdirs();
                }
                continue;
            }
            BufferedInputStream bis = new BufferedInputStream(zipFile
                    .getInputStream(entry));

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
        zipFile.close();
    }

}