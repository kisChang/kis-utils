package com.kischang.simple_utils.utils;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author KisChang
 * @date 2019-07-23
 */
public class ZXingTest {

    public static void main(String[] args) throws Exception {
//        testCenterLogo();
//        testBorder();
        testGenRandomImg();
    }

    private static void testGenRandomImg() throws Exception {
        BufferedImage bi = ImageUtils.genRandomImage("123Q");
        Path path = FileSystems.getDefault().getPath("C:\\Users\\KisChang\\Desktop\\test.png");
        if (!ImageIO.write(bi, ZXingUtils.ImageType.JPEG.getValue(), path.toFile())) {
            throw new IOException("Could not write an image of format " + ZXingUtils.ImageType.JPEG.getValue());
        }
    }

    private static void testBorder() throws Exception {
        ZXingUtils.QrCodeBuilder.genBuilder("http://www.baidu.com")
                .setOutput("C:\\Users\\KisChang\\Desktop\\test.png")
                .setWidthAndHeight(500)
                .setMargin(1)
                .setErrCorrection(ErrorCorrectionLevel.Q)
                //剧中图
                .setLogoInput(new FileInputStream("C:\\Users\\KisChang\\Desktop\\logo.jpg"))
                //边框图
                .setBorderQrSize(330).setBorderPosx(135).setBorderPosy(360)
                .setBorderInput(new FileInputStream("C:\\Users\\KisChang\\Desktop\\tmp_1.png"))
                .gen();
    }

    private static void testCenterLogo() throws Exception {
        ZXingUtils.QrCodeBuilder.genBuilder("http://www.baidu.com")
                .setOutput("C:\\Users\\KisChang\\Desktop\\test.png")
                .setWidthAndHeight(500)
                .setMargin(1)
                .setErrCorrection(ErrorCorrectionLevel.Q)
                .setLogoInput(new FileInputStream("C:\\Users\\KisChang\\Desktop\\logo.png"))
                .gen();
    }

}
