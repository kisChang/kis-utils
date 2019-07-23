package com.kischang.simple_utils.utils;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author KisChang
 * @date 2019-07-23
 */
public class ZXingTest {

    public static void main(String[] args) throws IOException, WriterException {
        ZXingUtils.QrCodeBuilder.genBuilder("C:\\Users\\73461\\Desktop\\test.pngC:\\Users\\73461\\Desktop\\test.pngC:\\Users\\73461\\Desktop\\test.pngC:\\Users\\73461\\Desktop\\test.png")
                .setOutput("C:\\Users\\73461\\Desktop\\test.png")
                .setWidthAndHeight(500)
                .setMargin(1)
                .setErrCorrection(ErrorCorrectionLevel.Q)
                .setLogoInput(new FileInputStream("C:\\Users\\73461\\Desktop\\logo.png"))
                .gen();
    }

}
