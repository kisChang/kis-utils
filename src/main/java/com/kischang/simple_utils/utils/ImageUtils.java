package com.kischang.simple_utils.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * 图片操作的工具类方法
 *
 * @author KisChang
 * @date 2019-12-12
 */
public class ImageUtils {


    private static Random RANDOM_IMG_TOOL = new Random();
    private static final String FONT_NAME = "Fixedsys";
    private static final int FONT_SIZE = 18;

    /**
     * 生成随机图片
     */
    public static BufferedImage genRandomImage(String randomCode) {
        return genRandomImage(randomCode, 80, 25, 50);
    }
    public static BufferedImage genRandomImage(String randomCode
            , int width, int height, int disturbLineNum) {
        // BufferedImage类是具有缓冲区的Image类
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        // 获取Graphics对象,便于对图像进行各种绘制操作
        Graphics g = image.getGraphics();
        // 设置背景色
        g.setColor(genRandColor(200, 250));
        g.fillRect(0, 0, width, height);

        // 设置干扰线的颜色
        g.setColor(genRandColor(110, 120));

        // 绘制干扰线
        for (int i = 0; i <= disturbLineNum; i++) {
            drawRandomLine(g, width, height, 16);
        }
        // 绘制随机字符
        g.setFont(new Font(FONT_NAME, Font.ROMAN_BASELINE, FONT_SIZE));
        for (int i = 0; i < randomCode.length(); i++) {
            drawString(g, i, String.valueOf(randomCode.charAt(i)));
        }
        g.dispose();
        return image;
    }

    /**
     * 给定范围获得随机颜色
     */
    public static Color genRandColor(int fc, int bc) {
        if (fc > 255){
            fc = 255;
        }
        if (bc > 255){
            bc = 255;
        }
        int r = fc + RANDOM_IMG_TOOL.nextInt(bc - fc);
        int g = fc + RANDOM_IMG_TOOL.nextInt(bc - fc);
        int b = fc + RANDOM_IMG_TOOL.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 绘制字符串
     */
    public static String drawString(Graphics g, int posx, String rand) {
        Color randomColor= new Color(RANDOM_IMG_TOOL.nextInt(101)
                , RANDOM_IMG_TOOL.nextInt(111)
                , RANDOM_IMG_TOOL.nextInt(121));
        return drawString(g, posx, randomColor, rand);
    }
    private static String drawString(Graphics g, int posx, Color color, String rand) {
        g.setColor(color);
        g.translate(RANDOM_IMG_TOOL.nextInt(3), RANDOM_IMG_TOOL.nextInt(3));
        g.drawString(rand, 13 * posx, 16);
        return rand;
    }

    /**
     * 绘制干扰线
     */
    public static void drawRandomLine(Graphics g, int width, int height, int bound) {
        int x = RANDOM_IMG_TOOL.nextInt(width);
        int y = RANDOM_IMG_TOOL.nextInt(height);
        int x0 = RANDOM_IMG_TOOL.nextInt(bound);
        int y0 = RANDOM_IMG_TOOL.nextInt(bound);
        g.drawLine(x, y, x + x0, y + y0);
    }


    /**
     * Java 读取图片，ImageIO.read 存在bug，操作图片后会变色
     * 通过此方法处理该问题
     */
    public static BufferedImage readImage(InputStream logoInput) throws IOException {
        BufferedImage _img = javax.imageio.ImageIO.read(logoInput);
        BufferedImage _newImg = new BufferedImage(_img.getWidth(), _img.getHeight(), BufferedImage.TYPE_INT_RGB);
        _newImg.getGraphics().drawImage(_img, 0, 0, null);
        return _newImg;
    }

    /**
     * 将一个图片放到另一张图片的某个位置
     *
     * @param mainImage   主图
     * @param centerLogo  中间的图片
     * @return 合并后的图片
     */
    public static BufferedImage setMatrixLogo(BufferedImage mainImage
            , BufferedImage centerLogo, ZXingUtils.QrCodeBuilder.Border centerLogoBorder, GenPositionFunc genPosFun){
        ImgPosition position = genPosFun.gen(mainImage, centerLogo);
        return setMatrixLogo(mainImage, centerLogo, centerLogoBorder
                , position.width, position.height, position.posx, position.posy);
    }

    public static BufferedImage setMatrixLogo(BufferedImage mainImage
            , BufferedImage centerLogo, ZXingUtils.QrCodeBuilder.Border centerLogoBorder
            , int logoWidth, int logoHeight, int logoPosx, int logoPosy) {
        // 1. 读取二维码图片，并构建绘图对象
        Graphics2D graph = mainImage.createGraphics();

        // 2. 绘制中间的图片
        graph.drawImage(centerLogo, logoPosx, logoPosy, logoWidth, logoHeight, null);
        graph.drawRoundRect(logoPosx, logoPosy, logoWidth, logoHeight, 10, 10);

        // 3. 边框
        if (centerLogoBorder != null){
            graph.setStroke(new BasicStroke(centerLogoBorder.width));
            graph.setColor(centerLogoBorder.color);
        }

        graph.drawRect(logoPosx, logoPosy, logoWidth, logoHeight);
        graph.dispose();
        return mainImage;
    }

    /**
     * 位置和大小信息
     */
    public static class ImgPosition {
        public int width;
        public int height;
        public int posx;
        public int posy;

        public ImgPosition(int logoWidth, int logoHeight, int logoPosx, int logoPosy) {
            this.width = logoWidth;
            this.height = logoHeight;
            this.posx = logoPosx;
            this.posy = logoPosy;
        }
    }

    /**
     * 定位方法
     */
    public static interface GenPositionFunc {

        ImgPosition gen(BufferedImage mainImage, BufferedImage centerLogo);

        GenPositionFunc CENTER = (mainImage, centerLogo) -> {
            // 1、处理logo大小
            int logoWidth = mainImage.getWidth() / 4;
            int logoHeight = mainImage.getHeight() / 4;
            // 2、计算图片放置的位置
            int logoPosx = (mainImage.getWidth() - logoWidth) / 2;
            int logoPosy = (mainImage.getHeight() - logoHeight) / 2;
            return new ImgPosition(logoWidth, logoHeight, logoPosx, logoPosy);
        };
    }


}
