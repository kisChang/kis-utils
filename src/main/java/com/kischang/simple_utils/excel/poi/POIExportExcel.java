package com.kischang.simple_utils.excel.poi;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Collection;

/**
 * POI导出工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class POIExportExcel {

    private String defaultFontFamily = "宋体";
    private int defaultFontSize = 200;
    private boolean is2007Xlsx;

    private Workbook wb;
    private Sheet sheet = null;
    private Row nowRow = null;
    private int rowIndex = 0;// 行索引

    public POIExportExcel() {
        this(true);
    }
    public POIExportExcel(boolean is2007Xlsx) {
        if (is2007Xlsx){
            this.wb = new XSSFWorkbook();
        }else {
            this.wb = new HSSFWorkbook();
        }
        this.is2007Xlsx = is2007Xlsx;
        this.initSheet(wb.getSheetName(0));
    }

    public POIExportExcel(String sheetName) {
        this(true, sheetName);
    }
    public POIExportExcel(boolean is2007Xlsx, String sheetName) {
        if (is2007Xlsx){
            this.wb = new XSSFWorkbook();
        }else {
            this.wb = new HSSFWorkbook();
        }
        this.is2007Xlsx = is2007Xlsx;
        this.initSheet(sheetName);
    }

    public POIExportExcel(InputStream in, boolean is2007Xlsx, String sheetName) throws IOException {
        this(in, is2007Xlsx);
        this.initSheet(sheetName);
    }

    public POIExportExcel(InputStream in, boolean is2007Xlsx) throws IOException {
        this.is2007Xlsx = is2007Xlsx;
        if (is2007Xlsx){
            this.wb = new XSSFWorkbook(in);
        }else {
            this.wb = new HSSFWorkbook(in);
        }
        this.initSheet(wb.getSheetName(0));
    }

    public POIExportExcel(Workbook wb) {
        this.wb = wb;
        this.initSheet(wb.getSheetName(0));
    }

    public POIExportExcel(Workbook wb, String sheetName) {
        this.wb = wb;
        this.initSheet(sheetName);
    }

    private void initSheet(String sheetName) {
        this.sheet = this.wb.getSheet(sheetName);
        this.rowIndex = 0;
        if (this.sheet == null) {
            newSheet(sheetName);
        }
        this.nowRow = this.sheet.getRow(this.rowIndex);
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
        this.rowIndex = 0;
        return this;
    }

    public POIExportExcel setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
        this.nowRow = this.sheet.getRow(this.rowIndex);
        return this;
    }

    /**
     * 创建EXCEL头部标题
     *
     * @param headString 头部显示的字符
     * @param colSum     该报表的列数
     */
    public POIExportExcel createHead(String headString, int colSum) {
        CellStyle cellStyle = POIStyleUtils.initNormalCellStyle(this.wb, getDefaultFont());
        return createHead(headString, colSum, cellStyle);
    }

    /**
     * @param headString 头部显示的字符
     * @param colSum     该报表的列数
     * @param cellStyle  报表字体样式
     */
    public POIExportExcel createHead(String headString, int colSum, CellStyle cellStyle) {
        return createHead(headString, colSum, cellStyle, getDefaultFont());
    }
    public POIExportExcel createHead(String headString, int colSum, CellStyle cellStyle, Font font) {
        this.rowIndex = 0;
        Row row = getSheet().createRow(0);
        Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        row.setHeight((short) (font.getFontHeight() * 2));
        //定义单元格为字符串类型
        cell.setCellType(CellType.STRING);
        cell.setCellValue(headString);
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
        CellStyle cellStyle = POIStyleUtils.initNormalCellStyle(this.wb, getDefaultFont());
        return createHeadTwo(params, colSum, cellStyle);
    }
    public POIExportExcel createHeadTwo(String params, int colSum, CellStyle cellStyle) {
        return this.createHeadTwo(params, colSum, cellStyle, getDefaultFont());
    }

    /**
     * 创建通用报表第二行
     *
     * @param params    内容
     * @param colSum    需要合并到的列索引
     * @param cellStyle 字体样式
     */
    public POIExportExcel createHeadTwo(String params, int colSum, CellStyle cellStyle, Font font) {
        cellStyle.setFont(font);

        this.rowIndex = 1;
        Row row1 = getSheet().createRow(1);
        row1.setHeight((short) (font.getFontHeight() * 2));

        Cell cell2 = row1.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        cell2.setCellType(CellType.STRING);
        cell2.setCellValue(params);

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
        this.rowIndex = 2;
        // 设置列头
        Row row2 = getSheet().createRow(2);

        // 指定行高
        row2.setHeight((short) (250 * 2));

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行

        // 单元格字体
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontName(this.defaultFontFamily);
        font.setFontHeight((short) 250);
        cellStyle.setFont(font);

        Cell cell3;

        for (int i = 0; i < columnHeader.length; i++) {
            cell3 = row2.getCell(i + index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell3.setCellType(CellType.STRING);
            cell3.setCellStyle(cellStyle);
            cell3.setCellValue(columnHeader[i]);
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
        Font font = POIStyleUtils.initCellFont(this.wb, this.defaultFontFamily, 300, true);
        CellStyle cellStyle = POIStyleUtils.initNormalCellStyle(this.wb, font);
        return createLastRow(colSum, cellValue, name, cellStyle, font);
    }

    public POIExportExcel createLastRow(int colSum, String[] cellValue, String name, CellStyle cellStyle, Font font) {
        cellStyle.setFont(font);

        Row lastRow = newRow().getNowRow();
        lastRow.setHeight((short) (font.getFontHeight() * 2));
        Cell sumCell = lastRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        sumCell.setCellValue(name);

        //靠右
        CellStyle sumCellStyle = POIStyleUtils.initNormalCellStyle(wb, font);
        sumCellStyle.setAlignment(HorizontalAlignment.CENTER);
        sumCell.setCellStyle(sumCellStyle);
        getSheet().addMergedRegion(new CellRangeAddress(getSheet().getLastRowNum(), getSheet().getLastRowNum(), 0, colSum));// 指定合并区域

        colSum++;
        for (int i = colSum; i < (cellValue.length + colSum); i++) {
            sumCell = lastRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            sumCell.setCellStyle(cellStyle);
            sumCell.setCellValue(cellValue[i - colSum]);
        }
        return this;
    }


    public POIExportExcel writeImg(byte[] imgBuffer, int picType, int col1, int row1, int col2, int row2) throws IOException {
        return writeImg(imgBuffer, picType, 0, 0,0,0, col1, row1, col2, row2);
    }
    /**
     * 创建一个新的客户端锚点，附加到excel工作表，并设置左上角和右下角
     *
     * 具体每个参数的意义： https://blog.csdn.net/chenssy/article/details/20524563
     * @param picType  Workbook.PICTURE_TYPE_JPEG|PICTURE_TYPE_PNG
     * @param dx1  图片的左上角在开始单元格（col1,row1）中的横坐标
     * @param dy1  图片的左上角在开始单元格（col1,row1）中的纵坐标
     * @param dx2  图片的右下角在结束单元格（col2,row2）中的横坐标
     * @param dy2  图片的右下角在结束单元格（col2,row2）中的纵坐标
     * @param col1  开始单元格所处的列号, base 0, 图片左上角在开始单元格内
     * @param row1  开始单元格所处的行号, base 0, 图片左上角在开始单元格内
     * @param col2  结束单元格所处的列号, base 0, 图片右下角在结束单元格内
     * @param row2  结束单元格所处的行号, base 0, 图片右下角在结束单元格内
     * */
    public POIExportExcel writeImg(byte[] imgBuffer, int picType, int dx1, int dy1, int dx2, int dy2, int col1, int row1, int col2, int row2) throws IOException {
        Drawing patriarch = sheet.createDrawingPatriarch();
        ClientAnchor anchor = this.createClientAnchor(dx1, dy1, dx2, dy2, (short) col1, row1, (short) col2, row2);
        int puctureIndex = this.wb.addPicture(imgBuffer, picType);
        patriarch.createPicture(anchor, puctureIndex);
        return this;
    }

    private ClientAnchor createClientAnchor(int dx1, int dy1, int dx2, int dy2, short col1, int row1, short col2, int row2) {
        if (this.is2007Xlsx){
            return new XSSFClientAnchor(dx1, dy1, dx2, dy2, col1, row1, col2, row2);
        }else {
            return new HSSFClientAnchor(dx1, dy1, dx2, dy2, col1, row1, col2, row2);
        }
    }

    /**
     * 转到指定Sheet
     */
    public POIExportExcel toSheet(String sheetName) {
        this.sheet = this.wb.getSheet(sheetName);
        this.rowIndex = 0;
        this.nowRow = this.sheet.getRow(this.rowIndex);
        return this;
    }

    /**
     * 在当前行后追加一行
     */
    public POIExportExcel newRow() {
        this.rowIndex ++;
        this.nowRow = getSheet().createRow(this.rowIndex);
        return this;
    }

    /**
     * 在当前行后追加一行
     */
    public POIExportExcel nextRow() {
        this.rowIndex ++;
        this.nowRow = getSheet().getRow(this.rowIndex);
        return this;
    }

    private CellStyle defCellStyle;
    private CellStyle genCellStyle() {
        if (defCellStyle == null){
            defCellStyle = POIStyleUtils.initNormalCellStyle(this.wb, this.defaultFontFamily, this.defaultFontSize, false);
        }
        return defCellStyle;
    }

    /**
     * 创建内容单元格
     *
     * @param col       short型的列索引
     * @param val       列值
     */
    public POIExportExcel createCell(int col, String val) {
        return createCell(col, val, genCellStyle());
    }
    public POIExportExcel createCell(int col, String val, CellStyle cellStyle){
        Cell cell = this.getNowRow().createCell(col);
        cell.setCellType(CellType.STRING);
        cell.setCellValue(val);
        cell.setCellStyle(cellStyle);
        return this;
    }
    public POIExportExcel createCell(int col, double val) {
        return createCell(col, val, genCellStyle());
    }

    public POIExportExcel createCell(int col, double val, CellStyle cellStyle){
        Cell cell = this.getNowRow().createCell(col);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(val);
        cell.setCellStyle(cellStyle);
        return this;
    }

    public POIExportExcel writeRow(String... values){
        return this.writeRow(0, values);
    }
    public POIExportExcel writeRow(int index, String... values){
        return this.writeRow(genCellStyle(), index, values);
    }
    public POIExportExcel writeRow(CellStyle cellStyle, int index, String... values){
        for (String once : values){
            Cell cell = this.getNowRow().getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(once);
            cell.setCellStyle(cellStyle);
            index ++;
        }
        return this;
    }

    public POIExportExcel writeRow(Double... values){
        return this.writeRow(0, values);
    }
    public POIExportExcel writeRow(int index, Double... values){
        return this.writeRow(genCellStyle(), index, values);
    }
    public POIExportExcel writeRow(CellStyle cellStyle, int index, Double... values){
        for (Double once : values){
            Cell cell = this.getNowRow().getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(once);
            cell.setCellStyle(cellStyle);
            index ++;
        }
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
        this.defFont = null;
    }

    public int getDefaultFontSize() {
        return defaultFontSize;
    }

    public void setDefaultFontSize(int defaultFontSize) {
        this.defaultFontSize = defaultFontSize;
        this.defFont = null;
    }

    private Font defFont;
    private Font getDefaultFont() {
        if (defFont == null){
            defFont = POIStyleUtils.initCellFont(this.wb, this.defaultFontFamily, this.defaultFontSize, true);
        }
        return defFont;
    }

    private Sheet getSheet(){
        while (this.sheet == null){
            newSheet();
        }
        return this.sheet;
    }

    public Workbook getWb() {
        return wb;
    }

    public Row getNowRow() {
        while (this.nowRow == null){
            newRow();
        }
        return this.nowRow;
    }

    public int getNowRowLine() {
        return getNowRow().getRowNum();
    }

    public Cell getCell(int index) {
        return this.getNowRow().getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    public CellStyle createCellStyle() {
        return this.wb.createCellStyle();
    }

    public Font createFont() {
        return this.wb.createFont();
    }

    public boolean isIs2007Xlsx() {
        return is2007Xlsx;
    }

    public String getXlsType(){
        return is2007Xlsx ? "xlsx" : "xls";
    }

}