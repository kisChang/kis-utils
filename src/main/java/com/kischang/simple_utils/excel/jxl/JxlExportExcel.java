package com.kischang.simple_utils.excel.jxl;


import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import java.io.File;
import java.io.IOException;

/**
 * Excel 写入工具
 *
 * @author KisChang
 * @version 1.0
 */
public class JxlExportExcel {
    private WritableWorkbook wb = null;
    private WritableSheet sheet = null;
    private int sheetIndex = 1;
    private int rowIndex = 0;

    public JxlExportExcel(File excelFile) {
        super();
        try {
            this.wb = Workbook.createWorkbook(excelFile);
            newSheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JxlExportExcel(File excelFile, String sheetName) {
        super();
        try {
            this.wb = Workbook.createWorkbook(excelFile);
            newSheet(sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WritableCellFormat initNormalCellStyle() {
        return initNormalCellStyle(Alignment.CENTRE, VerticalAlignment.CENTRE, true, WritableFont.ARIAL, true, 20);
    }

    /**
     * 居中仅自定义字体
     */
    public WritableCellFormat initNormalCellStyle(WritableFont.FontName fontFamily, boolean bold, int fontSize) {
        return initNormalCellStyle(Alignment.CENTRE, VerticalAlignment.CENTRE, true, fontFamily, bold, fontSize);
    }

    /**
     * 设置样式
     *
     * @param align      是否单元格居中对齐
     * @param vertical   是否单元格垂直居中对齐
     * @param wrap       是否单元格自动换行
     * @param fontFamily 字体样式
     * @param bold       是否加粗
     * @param fontSize   字体大小
     */
    public WritableCellFormat initNormalCellStyle(Alignment align, VerticalAlignment vertical, boolean wrap, WritableFont.FontName fontFamily, boolean bold, int fontSize) {
        WritableFont font = null;
        if (bold) {
            font = new WritableFont(fontFamily, fontSize, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
        } else {
            font = new WritableFont(fontFamily, fontSize, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
        }
        WritableCellFormat cellFormat = new WritableCellFormat(font);
        try {
            cellFormat.setAlignment(align);
            cellFormat.setVerticalAlignment(vertical);
            cellFormat.setWrap(wrap);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return cellFormat;
    }

    /**
     * 创建EXCEL头部标题
     *
     * @param headString 头部显示的字符
     * @param colSum     该报表的列数
     */
    public void createHead(String headString, int colSum) {
        createHead(headString, colSum, initNormalCellStyle(WritableFont.ARIAL, true, 30));
    }

    /**
     * @param headString 头部显示的字符
     * @param colSum     该报表的列数
     * @param cellStyle  报表字体样式
     */
    public void createHead(String headString, int colSum, WritableCellFormat cellStyle) {
        try {
            //先合并 第1列第1行  到 第colSum列第1行
            this.sheet.mergeCells(0, 0, colSum, 0);
            Label label = new Label(0, 0, headString);
            label.setCellFormat(cellStyle);
            sheet.addCell(label);
            this.rowIndex++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建通用报表第二行
     *
     * @param params 统计条件数组
     * @param colSum 需要合并到的列索引
     */
    public void createHeadTwo(String params, int colSum) {
        createHeadTwo(params, colSum, initNormalCellStyle(WritableFont.ARIAL, true, 20));
    }

    /**
     * 创建通用报表第二行
     *
     * @param params    内容
     * @param colSum    需要合并到的列索引
     * @param cellStyle 字体样式
     */
    public void createHeadTwo(String params, int colSum, WritableCellFormat cellStyle) {
        try {
            //先合并 第1列第2行  到 第colSum列第2行
            this.sheet.mergeCells(0, 1, colSum, 1);
            Label label = new Label(0, 1, params);
            label.setCellFormat(cellStyle);
            sheet.addCell(label);
            this.rowIndex++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置报表列标题
     *
     * @param columnHeader 标题字符串数组
     */
    public void createColumnHeader(String[] columnHeader) {
        createColumnHeader(columnHeader, initNormalCellStyle(WritableFont.ARIAL, true, 10));
    }

    public void createColumnHeader(String[] columnHeader, WritableCellFormat cellStyle) {
        try {
            int colIndex = 0;
            for (String param : columnHeader) {
                Label label = new Label(colIndex, this.rowIndex, param);
                label.setCellFormat(cellStyle);
                sheet.addCell(label);
                colIndex++;
            }
            this.rowIndex++;
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建内容单元格
     */
    public void createCell(int row, int col, String val) {
        createCell(row, col, val, initNormalCellStyle(WritableFont.ARIAL, false, 8));
    }

    public void createCell(int row, int col, String val, WritableCellFormat cellStyle) {
        try {
            Label label = new Label(col, row, val);
            label.setCellFormat(cellStyle);
            sheet.addCell(label);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建最后一个统计行
     *
     * @param col       需要合并到的列索引
     * @param cellValue
     */
    public void createLastRow(int row, int col, String[] cellValue, String name) {
        createLastRow(row, col, cellValue, name, initNormalCellStyle(WritableFont.ARIAL, true, 8));
    }

    public void createLastRow(int row, int col, String[] cellValue, String name, WritableCellFormat cellStyle) {
        try {
            //合并
            this.sheet.mergeCells(0, row, col, row);
            //头信息
            Label nameLabel = new Label(0, row, name);
            nameLabel.setCellFormat(cellStyle);
            sheet.addCell(nameLabel);
            //后续内容
            for (int i = 0; i < cellValue.length; i++) {
                Label label = new Label(i + col + 1, row, cellValue[i]);
                label.setCellFormat(cellStyle);
                sheet.addCell(label);
            }
            this.rowIndex++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建一行
     */
    public int newRow() {
        return ++this.rowIndex;
    }

    /**
     * 创建一个新的Sheet
     */
    public void newSheet(String sheetName) {
        if (sheetName != null && !"".equals(sheetName)) {
            this.sheet = this.wb.createSheet(sheetName, sheetIndex++);
        } else {
            this.sheet = this.wb.createSheet("Sheet" + sheetIndex, sheetIndex++);
        }
        this.rowIndex = 0;
    }

    /**
     * 创建一个新的Sheet,使用默认命名
     */
    public void newSheet() {
        newSheet(null);
    }

    /**
     * 输出EXCEL文件
     */
    public void outputExcel() {
        try {
            this.wb.write();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.wb != null) {
                try {
                    this.wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
