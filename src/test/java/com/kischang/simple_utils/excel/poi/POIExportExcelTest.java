package com.kischang.simple_utils.excel.poi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * POI导出
 *
 * @author KisChang
 * @version 1.0
 */
public class POIExportExcelTest {

    public static void main(String[] args) throws Exception {
        InputStream in = POIExportExcelTest.class.getClassLoader().getResourceAsStream("student-list.xls");

        POIExportExcel exportExcel = new POIExportExcel(in);
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

}
