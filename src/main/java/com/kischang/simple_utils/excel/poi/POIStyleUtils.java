package com.kischang.simple_utils.excel.poi;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * POI 单元格格式创建工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class POIStyleUtils {

    /**
     * 设置默认样式
     */
    public static HSSFCellStyle initNormalCellStyle(HSSFWorkbook wb) {
        return initNormalCellStyle(wb, "宋体", 350, true, true, false, false);
    }

    /**
     * 居中仅自定义字体
     */
    public static HSSFCellStyle initNormalCellStyle(HSSFWorkbook wb, String fontFamily, int fontSize, boolean bold) {
        return initNormalCellStyle(wb, fontFamily, fontSize, true, true, false, bold);
    }

    /**
     * 设置样式
     *
     * @param fontFamily 字体样式
     * @param fontSize   字体大小
     * @param align      是否单元格居中对齐
     * @param vertical   是否单元格垂直居中对齐
     * @param wrap       是否单元格自动换行
     * @param bold       是否加粗
     */
    public static HSSFCellStyle initNormalCellStyle(HSSFWorkbook wb, String fontFamily, int fontSize, boolean align, boolean vertical, boolean wrap, boolean bold) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        if (align)
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        if (vertical)
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        if (wrap)
            cellStyle.setWrapText(true);
        HSSFFont font = wb.createFont();
        if (bold)
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName(fontFamily);
        font.setFontHeight((short) fontSize);
        cellStyle.setFont(font);
        return cellStyle;
    }

}
