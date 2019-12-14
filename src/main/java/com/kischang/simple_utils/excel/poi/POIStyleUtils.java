package com.kischang.simple_utils.excel.poi;

import org.apache.poi.ss.usermodel.*;

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
    public static CellStyle initNormalCellStyle(Workbook wb) {
        return initNormalCellStyle(wb, "宋体", 350, true, true, false, false);
    }

    public static CellStyle initNormalCellStyle(Workbook wb, Font font) {
        return initNormalCellStyle(wb, font, true, true, false);
    }

    public static CellStyle initNormalCellStyle(Workbook wb, Font font, boolean align, boolean vertical, boolean wrap) {
        CellStyle cellStyle = wb.createCellStyle();
        if (align)
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        if (vertical)
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        if (wrap)
            cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 居中仅自定义字体
     */
    public static CellStyle initNormalCellStyle(Workbook wb, String fontFamily, int fontSize, boolean bold) {
        return initNormalCellStyle(wb, fontFamily, fontSize, true, true, false, bold);
    }

    public static Font initCellFont(Workbook wb, String fontFamily, int fontSize, boolean bold){
        Font font = wb.createFont();
        font.setBold(bold);
        font.setFontName(fontFamily);
        font.setFontHeight((short) fontSize);
        return font;
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
    public static CellStyle initNormalCellStyle(Workbook wb, String fontFamily, int fontSize, boolean align, boolean vertical, boolean wrap, boolean bold) {
        CellStyle cellStyle = wb.createCellStyle();
        if (align)
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        if (vertical)
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        if (wrap)
            cellStyle.setWrapText(true);
        Font font = initCellFont(wb, fontFamily, fontSize, bold);
        cellStyle.setFont(font);
        return cellStyle;
    }

}
