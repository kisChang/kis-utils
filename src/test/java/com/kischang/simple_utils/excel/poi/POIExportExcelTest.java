package com.kischang.simple_utils.excel.poi;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.*;

/**
 * POI导出
 *
 * @author KisChang
 * @version 1.0
 */
public class POIExportExcelTest {

    public static void main(String[] args) throws Exception {
        //testMain2();
        testMain3();
    }

    private static void testMain3() {
        POIExportExcel exportExcel = new POIExportExcel(false);
        exportExcel.toSheet("Grade");
        exportExcel.newRow().createCell(0, "1");
        exportExcel.newRow().createCell(0, "2");
        exportExcel.newRow().createCell(0, "3");
        exportExcel.newRow().createCell(0, "4");
        exportExcel.newRow().createCell(0, "5");
        exportExcel.newRow().createCell(0, "6");

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
