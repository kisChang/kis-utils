package com.kischang.simple_utils.utils;

import com.google.zxing.*;
import com.google.zxing.Reader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

/**
 * 二维码工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class ZXingUtils {

    public static enum ImageType {
        JPEG("jpeg"),PNG("png"),GIF("gif");
        private String value;

        ImageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**编码*/
    public static class Encode {

        private static Map<EncodeHintType, Object> HINTS;
        static {
            HINTS = new EnumMap<EncodeHintType,Object>(EncodeHintType.class);
            HINTS.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            HINTS.put(EncodeHintType.MARGIN, 1);
        }

        public static Map<EncodeHintType, Object> getDefHints(){
            return new EnumMap<EncodeHintType, Object>(HINTS);
        }

        /**
         * 生成二维码
         * @param widthAndHeight    高宽
         * @param content           二维码内容
         * @param os                输出流
         */
        public static void buildQRCode(int widthAndHeight, String content, OutputStream os, ImageType imageType) throws WriterException, IOException {
            Map<EncodeHintType, Object> hintsMap = getDefHints();
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(content, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hintsMap);// 生成矩阵
            MatrixToImageWriter.writeToStream(bitMatrix, imageType.getValue(), os);
        }

        public static void buildQRCode(String content, OutputStream os, ImageType imageType) throws WriterException, IOException {
            buildQRCode(200, content, os, imageType);
        }

        /**
         * 生成二维码
         * @param widthAndHeight    高宽
         * @param content           二维码内容
         * @param filePath          输出目录
         * @param fileName          输出文件名
         * @param imageType         输出文件类型
         */
        public static void buildQRCode(int widthAndHeight, String content, String filePath, String fileName, ImageType imageType) throws WriterException, IOException {
            Map<EncodeHintType, Object> hintsMap = getDefHints();
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(content, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hintsMap);
            Path path = FileSystems.getDefault().getPath(filePath, fileName);
            MatrixToImageWriter.writeToPath(bitMatrix, imageType.getValue(), path);// 输出图像
        }

        public static void buildQRCode(String content, String filePath, String fileName, ImageType imageType) throws WriterException, IOException {
            buildQRCode(200, content,filePath,fileName,imageType);
        }
    }

    public static class QrCodeBuilder {
        public static final int BLACK = 0xFF000000;
        public static final int WHITE = 0xFFFFFFFF;

        private String content;
        private ImageType imageType = ImageType.JPEG;
        private int widthAndHeight = 200;
        private Map<EncodeHintType, Object> hintsMap;

        private int onColor = BLACK;
        private int offColor = WHITE;

        //LOGO 配置
        private Border logoBorder;
        private InputStream logoInput;

        //外边框配置
        private InputStream borderInput;
        private int borderQrSize;       //二维码大小（正方形）
        private int borderPosx;         //在边框图的什么位置开始绘制
        private int borderPosy;         //x\y


        //输出配置
        private boolean outType = true; //true用流
        private OutputStream outStream;
        private String filePath;

        private QrCodeBuilder(String content) {
            this.hintsMap = Encode.getDefHints();
            this.content = content;
        }

        public static QrCodeBuilder genBuilder(String content){
            return new QrCodeBuilder(content);
        }

        public QrCodeBuilder setImageType(ImageType imageType) {
            this.imageType = imageType;
            return this;
        }

        public QrCodeBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public QrCodeBuilder setWidthAndHeight(int widthAndHeight) {
            this.widthAndHeight = widthAndHeight;
            return this;
        }

        public QrCodeBuilder clearHints(){
            this.hintsMap = new HashMap<>();
            return this;
        }

        public QrCodeBuilder setChartset(String chartset) {
            this.hintsMap.put(EncodeHintType.CHARACTER_SET, chartset);
            return this;
        }

        public QrCodeBuilder setErrCorrection(int level) {
            ErrorCorrectionLevel tmp = ErrorCorrectionLevel.M;
            for (ErrorCorrectionLevel once : ErrorCorrectionLevel.values()){
                if (once.getBits() == level){
                    tmp = once;
                }
            }
            return setErrCorrection(tmp);
        }
        public QrCodeBuilder setErrCorrection(ErrorCorrectionLevel level) {
            this.hintsMap.put(EncodeHintType.ERROR_CORRECTION, level);
            return this;
        }

        public QrCodeBuilder setMargin(int margin) {
            this.hintsMap.put(EncodeHintType.MARGIN, margin);
            return this;
        }

        public QrCodeBuilder setOutput(OutputStream out) {
            this.outType = true;
            this.outStream = out;
            return this;
        }

        public QrCodeBuilder setOutput(String filePath) {
            this.outType = false;
            this.filePath = filePath;
            return this;
        }

        public QrCodeBuilder setLogoBorder(Border logoBorder) {
            this.logoBorder = logoBorder;
            return this;
        }

        public QrCodeBuilder setLogoInput(InputStream logoInput) {
            this.logoInput = logoInput;
            return this;
        }

        public QrCodeBuilder setBorderInput(InputStream borderInput) {
            this.borderInput = borderInput;
            return this;
        }

        public QrCodeBuilder setBorderQrSize(int borderQrSize) {
            this.borderQrSize = borderQrSize;
            return this;
        }

        public QrCodeBuilder setBorderPosx(int borderPosx) {
            this.borderPosx = borderPosx;
            return this;
        }

        public QrCodeBuilder setBorderPosy(int borderPosy) {
            this.borderPosy = borderPosy;
            return this;
        }

        public BufferedImage genBufferedImage() throws IOException, WriterException {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(this.content
                    , BarcodeFormat.QR_CODE
                    , this.widthAndHeight, this.widthAndHeight
                    , this.hintsMap);
            BufferedImage qrImage = toBufferedImage(bitMatrix);

            //合并logo
            if (this.logoInput != null){
                qrImage = ImageUtils.setMatrixLogo(qrImage
                        , ImageUtils.readImage(this.logoInput)
                        , this.logoBorder
                        , ImageUtils.GenPositionFunc.CENTER
                );
            }

            //处理边框
            if (this.borderInput != null){
                qrImage = ImageUtils.setMatrixLogo(
                        ImageUtils.readImage(this.borderInput)
                        , qrImage
                        , null
                        , this.borderQrSize, this.borderQrSize
                        , this.borderPosx, this.borderPosy
                );
            }
            return qrImage;
        }

        public void gen() throws WriterException, IOException{
            try {
                BufferedImage qrImage = genBufferedImage();
                if (this.outType){
                    if (!ImageIO.write(qrImage, this.imageType.getValue(), this.outStream)) {
                        throw new IOException("Could not write an image of format " + this.imageType.getValue());
                    }
                } else {
                    Path path = FileSystems.getDefault().getPath(filePath);
                    if (!ImageIO.write(qrImage, this.imageType.getValue(), path.toFile())) {
                        throw new IOException("Could not write an image of format " + this.imageType.getValue());
                    }
                }
            }finally {
                IOUtils.closeQuietly(this.logoInput);
                IOUtils.closeQuietly(this.outStream);
            }
        }

        /**
         * 将二维码转成Image，不调用ZXing的，是因为ImageType 需要使用TYPE_INT_RGB才能是彩色的
         * @param matrix 二维码图片
         * @return 二维码图片对象
         */
        private BufferedImage toBufferedImage(BitMatrix matrix) {
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int[] rowPixels = new int[width];
            BitArray row = new BitArray(width);
            for (int y = 0; y < height; y++) {
                row = matrix.getRow(y, row);
                for (int x = 0; x < width; x++) {
                    rowPixels[x] = row.get(x) ? this.onColor : this.offColor;
                }
                image.setRGB(0, y, width, 1, rowPixels, 0, width);
            }
            return image;
        }

        public static class Border {
            public float width;
            public Color color;

            public Border() {
                this.width = 1;
                this.color = new Color(128, 128, 128);
            }

            public Border(float width, Color color) {
                this.width = width;
                this.color = color;
            }
        }

    }


    /**解码*/
    public static class Decode {

        private static final Map<DecodeHintType,Object> HINTS;
        private static final Map<DecodeHintType,Object> HINTS_PURE;
        static {
            HINTS = new EnumMap<DecodeHintType,Object>(DecodeHintType.class);
            HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
            HINTS_PURE = new EnumMap<DecodeHintType,Object>(HINTS);
            HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        }

        /**
         * 解析二维码
         */
        public static Collection<Result> readQRCode(File qrCode) throws ReaderException, IOException {
            FileInputStream inputStream = new FileInputStream(qrCode);
            return readQRCode(inputStream);
        }

        public static Collection<Result> readQRCode(InputStream inputStream) throws ReaderException, IOException {
            LuminanceSource source = new BufferedImageLuminanceSource(ImageIO.read(inputStream));
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            Collection<Result> results = new ArrayList<Result>(1);
            ReaderException savedException = null;
            Reader reader = new MultiFormatReader();
            try {
                //寻找多个条码
                MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(reader);
                Result[] theResults = multiReader.decodeMultiple(binaryBitmap, HINTS);
                if (theResults != null) {
                    results.addAll(Arrays.asList(theResults));
                }
            } catch (ReaderException re) {
                savedException = re;
            }

            if (results.isEmpty()) {
                try {
                    //寻找纯条码
                    Result theResult = reader.decode(binaryBitmap, HINTS_PURE);
                    if (theResult != null) {
                        results.add(theResult);
                    }
                } catch (ReaderException re) {
                    savedException = re;
                }
            }

            if (results.isEmpty()) {
                try {
                    //寻找图片中的正常条码
                    Result theResult = reader.decode(binaryBitmap, HINTS);
                    if (theResult != null) {
                        results.add(theResult);
                    }
                } catch (ReaderException re) {
                    savedException = re;
                }
            }

            if (results.isEmpty()) {
                try {
                    //再次尝试其他特殊处理
                    BinaryBitmap hybridBitmap = new BinaryBitmap(new HybridBinarizer(source));
                    Result theResult = reader.decode(hybridBitmap, HINTS);
                    if (theResult != null) {
                        results.add(theResult);
                    }
                } catch (ReaderException re) {
                    savedException = re;
                }
            }
            if (results.isEmpty()){
                throw savedException;
            }else {
                return results;
            }
        }


        public static Result readQRCodeResult(File qrCode) throws ReaderException, IOException {
            FileInputStream inputStream = new FileInputStream(qrCode);
            return readQRCodeResult(inputStream);
        }
        public static Result readQRCodeResult(InputStream inputStream) throws ReaderException, IOException {
            Collection<Result> results = readQRCode(inputStream);
            if (!results.isEmpty()){
                //寻找结果集中非空的结果
                for (Result result : results){
                    if (result != null){
                        return result;
                    }
                }
            }
            throw NotFoundException.getNotFoundInstance();
        }
    }
}
