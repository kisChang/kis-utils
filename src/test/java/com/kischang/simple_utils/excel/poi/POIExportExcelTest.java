package com.kischang.simple_utils.excel.poi;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.awt.*;
import java.io.*;

/**
 * POI导出
 *
 * @author KisChang
 * @version 1.0
 */
public class POIExportExcelTest {

    public static void main(String[] args) throws Exception {
        //testMain1();
        //testMain2();
        //testMain3();
        testMain4();
    }

    private static void testMain4() throws IOException {
        POIExportExcel exportExcel = new POIExportExcel(true);
        exportExcel.toSheet("sheet1");
        CellStyle cellStyle = exportExcel.createCellStyle(true, true, IndexedColors.PALE_BLUE);
        Font font = exportExcel.createFont(
                "宋体", 200
                , HSSFColor.HSSFColorPredefined.WHITE.getColor()
                , true, false, null);
        cellStyle.setFont(font);

        exportExcel.newRow().writeRow(cellStyle, 0, "1", "1", "1");
        exportExcel.newRow().writeRow(cellStyle, 0, "2", "2", "2");
        exportExcel.newRow().writeRow(cellStyle, 0, "3", "3", "3");
        exportExcel.newRow().writeRow(cellStyle, 0, "4", "长中文，自动调整列宽，功能测试", "4");
        exportExcel.newRow().writeRow(cellStyle, 0, "5", "5", "5");
        exportExcel.newRow().writeRow(cellStyle, 0, "6", "6", "6");
        exportExcel.autoSizeColumn(1);
        exportExcel.autoSizeColumn(2);

        exportExcel.outputExcel("C:\\Users\\KisChang\\Desktop\\test." + exportExcel.getXlsType());
    }


    private static void testMain3() throws IOException {
        POIExportExcel exportExcel = new POIExportExcel(false);
        exportExcel.toSheet("Grade");
        exportExcel.newRow().createCell(0, "1");
        exportExcel.newRow().createCell(0, "2");
        exportExcel.newRow().createCell(0, "3");
        exportExcel.newRow().createCell(0, "4");
        exportExcel.newRow().createCell(0, "5");
        exportExcel.newRow().createCell(0, "6");
        exportExcel.outputExcel("C:\\Users\\KisChang\\Desktop\\test." + exportExcel.getXlsType());
    }

    public static void testMain1() throws Exception {
        InputStream in = POIExportExcelTest.class.getClassLoader().getResourceAsStream("student-list.xls");

        POIExportExcel exportExcel = new POIExportExcel(in, false);
        exportExcel.toSheet("Grade");
        exportExcel.newRow().createCell(0, "1");
        exportExcel.newRow().createCell(0, "2");
        exportExcel.newRow().createCell(0, "3");
        exportExcel.newRow().createCell(0, "4");
        exportExcel.newRow().createCell(0, "5");
        exportExcel.newRow().createCell(0, "6");

        OutputStream os = null;
        try {
            os = new FileOutputStream("/home/kischang/Desktop/temp.xls");
            exportExcel.outputExcel(os);
        } finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException ignored) {}
            }
        }
    }
    public static void testMain2() throws Exception {
        InputStream tempIn = new FileInputStream("C:\\Users\\KisChang\\Desktop\\template.xlsx");

        POIExportExcel exportExcel = new POIExportExcel(tempIn, true, "sheet1");
        CellStyle cellStyle = exportExcel.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        exportExcel.setRowIndex(1)
                .writeRow(cellStyle, 2, "智开学院-2019.12月销售记录")
                .nextRow().writeRow(cellStyle, 8, String.valueOf(System.currentTimeMillis()))
                .nextRow()
                //////
                .newRow().writeRow("---序号", "订单结束时间", "商品名称", "出售数量", "原价", "出售价格", "出售总金额", "抽取金额", "最终金额")
                .newRow().writeRow("cellStyle", "123", "1233", "123")
                .newRow().writeRow("cellStyle", "23", "1233", "123")
                .newRow().writeRow("cellStyle", "123", "123111113", "123323")
        ;

        exportExcel.outputExcel("C:\\Users\\KisChang\\Desktop\\test.xlsx");
    }

}
