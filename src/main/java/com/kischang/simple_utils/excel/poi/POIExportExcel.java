package com.kischang.simple_utils.excel.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * POI导出工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class POIExportExcel {
    private String defaultFontFamily = "微软雅黑";
    private int defaultFontSize = 200;

    private HSSFWorkbook wb = null;
    private HSSFSheet sheet = null;
    private HSSFRow nowRow = null;

    public POIExportExcel() {
        super();
        this.wb = new HSSFWorkbook();
    }

    public POIExportExcel(String sheetName) {
        super();
        this.wb = new HSSFWorkbook();
        newSheet(sheetName);
    }

    public POIExportExcel(HSSFWorkbook wb) {
        super();
        this.wb = wb;
    }

    public POIExportExcel(HSSFWorkbook wb, String sheetName) {
        super();
        this.wb = wb;
        newSheet(sheetName);
    }

    /**
     * 创建EXCEL头部标题
     *
     * @param headString 头部显示的字符
     * @param colSum     该报表的列数
     */
    public POIExportExcel createHead(String headString, int colSum) {
        return createHead(headString, colSum, POIStyleUtils.initNormalCellStyle(this.wb, this.defaultFontFamily, 350, true));
    }

    /**
     * @param headString 头部显示的字符
     * @param colSum     该报表的列数
     * @param cellStyle  报表字体样式
     */
    public POIExportExcel createHead(String headString, int colSum, HSSFCellStyle cellStyle) {
        HSSFRow row = getSheet().createRow(0);
        HSSFCell cell = row.createCell(0);
        row.setHeight((short) (cellStyle.getFont(wb).getFontHeight() * 2));
        //定义单元格为字符串类型
        cell.setCellType(HSSFCell.ENCODING_UTF_16);
        cell.setCellValue(new HSSFRichTextString(headString));
        //合并区域
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 0, colSum));
        cell.setCellStyle(cellStyle);
        return this;
    }

    /**
     * 创建通用报表第二行
     *
     * @param params 统计条件数组
     * @param colSum 需要合并到的列索引
     */
    public POIExportExcel createHeadTwo(String params, int colSum) {
        return createHeadTwo(params, colSum, POIStyleUtils.initNormalCellStyle(this.wb, this.defaultFontFamily, 300, true));
    }

    /**
     * 创建通用报表第二行
     *
     * @param params    内容
     * @param colSum    需要合并到的列索引
     * @param cellStyle 字体样式
     */
    public POIExportExcel createHeadTwo(String params, int colSum, HSSFCellStyle cellStyle) {
        HSSFRow row1 = getSheet().createRow(1);
        row1.setHeight((short) (cellStyle.getFont(wb).getFontHeight() * 2));

        HSSFCell cell2 = row1.createCell(0);

        cell2.setCellType(HSSFCell.ENCODING_UTF_16);
        cell2.setCellValue(new HSSFRichTextString(params));

        // 指定合并区域
        getSheet().addMergedRegion(new CellRangeAddress(1, 1, 0, colSum));
        cell2.setCellStyle(cellStyle);
        return this;
    }

    public POIExportExcel createColumnHeader(int index, Collection<String> columnHeader) {
        return createColumnHeader(index, columnHeader.toArray(new String[]{}));
    }

    /**
     * 设置报表列标题
     *
     * @param index        缩进
     * @param columnHeader 标题字符串数组
     */
    public POIExportExcel createColumnHeader(int index, String[] columnHeader) {
        // 设置列头
        HSSFRow row2 = this.newRow().getNowRow();

        // 指定行高
        row2.setHeight((short) (250 * 2));

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行

        // 单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName(this.defaultFontFamily);
        font.setFontHeight((short) 250);
        cellStyle.setFont(font);

        HSSFCell cell3;

        for (int i = 0; i < columnHeader.length; i++) {
            cell3 = row2.createCell(i + index);
            cell3.setCellType(HSSFCell.ENCODING_UTF_16);
            cell3.setCellStyle(cellStyle);
            cell3.setCellValue(new HSSFRichTextString(columnHeader[i]));
        }
        return this;
    }

    /**
     * 创建最后一个统计行
     *
     * @param colSum    需要合并到的列索引
     * @param cellValue
     */
    public POIExportExcel createLastRow(int colSum, String[] cellValue, String name) {
        return createLastRow(colSum, cellValue, name, POIStyleUtils.initNormalCellStyle(this.wb, this.defaultFontFamily, 250, false));
    }

    public POIExportExcel createLastRow(int colSum, String[] cellValue, String name, HSSFCellStyle cellStyle) {

        HSSFRow lastRow = getSheet().createRow((short) (getSheet().getLastRowNum() + 1));
        lastRow.setHeight((short) (cellStyle.getFont(wb).getFontHeight() * 2));
        HSSFCell sumCell = lastRow.createCell(0);

        sumCell.setCellValue(new HSSFRichTextString(name));

        //靠右
        HSSFCellStyle sumCellStyle = POIStyleUtils.initNormalCellStyle(wb, cellStyle.getFont(wb).getFontName(), cellStyle.getFont(wb).getFontHeight(), false);
        sumCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        sumCell.setCellStyle(sumCellStyle);
        getSheet().addMergedRegion(new CellRangeAddress(getSheet().getLastRowNum(), getSheet().getLastRowNum(), 0, colSum));// 指定合并区域

        colSum++;
        for (int i = colSum; i < (cellValue.length + colSum); i++) {
            sumCell = lastRow.createCell(i);
            sumCell.setCellStyle(cellStyle);
            sumCell.setCellValue(new HSSFRichTextString(cellValue[i - colSum]));
        }
        return this;
    }

    /**
     * 创建一个新的Sheet,使用默认命名
     */
    public POIExportExcel newSheet() {
        return newSheet(null);
    }

    /**
     * 创建一个新的Sheet
     */
    public POIExportExcel newSheet(String sheetName) {
        if (sheetName == null || "".equals(sheetName)) {
            this.sheet = this.wb.createSheet();
        } else {
            this.sheet = this.wb.createSheet(sheetName);
        }
        this.nowRow = this.getSheet().createRow(0);
        return this;
    }

    /**
     * 转到指定Sheet
     */
    public POIExportExcel toSheet(String grade) {
        this.sheet = this.wb.getSheet(grade);
        this.nowRow = this.getSheet().createRow(0);
        return this;
    }

    /**
     * 在当前行后追加一行
     */
    public POIExportExcel newRow() {
        this.nowRow = getSheet().createRow(getSheet().getLastRowNum() + 1);
        return this;
    }

    /**
     * 创建内容单元格
     *
     * @param col       short型的列索引
     * @param val       列值
     */
    public POIExportExcel createCell(int col, String val) {
        return createCell(col, val, POIStyleUtils.initNormalCellStyle(this.wb, this.defaultFontFamily, this.defaultFontSize, false));
    }
    public POIExportExcel createCell(int col, String val, HSSFCellStyle cellStyle){
        HSSFCell cell = this.nowRow.createCell(col);
        cell.setCellType(HSSFCell.ENCODING_UTF_16);
        cell.setCellValue(new HSSFRichTextString(val));
        cell.setCellStyle(cellStyle);
        return this;
    }
    public POIExportExcel createCell(int col, double val) {
        return createCell(col, val, POIStyleUtils.initNormalCellStyle(this.wb, this.defaultFontFamily, this.defaultFontSize, false));
    }
    public POIExportExcel createCell(int col, double val, HSSFCellStyle cellStyle){
        HSSFCell cell = this.nowRow.createCell(col);
        cell.setCellType(HSSFCell.ENCODING_UTF_16);
        cell.setCellValue(val);
        cell.setCellStyle(cellStyle);
        return this;
    }

    /**行、列合并*/
    public POIExportExcel merge(int firstRow, int lastRow, int firstCol, int lastCol) {
        this.getSheet().addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
        return this;
    }

    /**
     * 输出EXCEL文件
     *
     * @param fileName 文件名
     */
    public void outputExcel(String fileName) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(fileName));
            outputExcel(fos);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 输出EXCEL文件到流
     */
    public void outputExcel(OutputStream os) throws IOException {
        try {
            wb.write(os);
        }finally {
            wb.close();
        }
    }

    public String getDefaultFontFamily() {
        return defaultFontFamily;
    }

    public void setDefaultFontFamily(String defaultFontFamily) {
        this.defaultFontFamily = defaultFontFamily;
    }

    public int getDefaultFontSize() {
        return defaultFontSize;
    }

    public void setDefaultFontSize(int defaultFontSize) {
        this.defaultFontSize = defaultFontSize;
    }

    private HSSFSheet getSheet(){
        while (this.sheet == null){
            newSheet();
        }
        return this.sheet;
    }

    public HSSFWorkbook getWb() {
        return wb;
    }

    public HSSFRow getNowRow() {
        return nowRow;
    }

    public int getNowRowLine() {
        return getNowRow().getRowNum();
    }
}