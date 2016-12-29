package com.kischang.simple_utils.excel.jxl;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Excel 读取写入工具
 *
 * @author KisChang
 * @version 1.0
 */
public class ExcelReadWriteUtils {

    //支持的列表
    public static final List<String> SUPPORTLIST = Arrays.asList("xls");

    public static boolean readExcel(File file, ReadLineValueInterface readline) {
        Workbook wb = null;
        try {
            // 构造Workbook（工作薄）对象
            wb = Workbook.getWorkbook(file);
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }

        return !readExcel(wb, readline);
    }

    public static boolean readExcel(InputStream inputStream, ReadLineValueInterface readline) {
        Workbook wb = null;
        try {
            // 构造Workbook（工作薄）对象
            wb = Workbook.getWorkbook(inputStream);
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }

        return !readExcel(wb, readline);
    }

    private static boolean readExcel(Workbook wb, ReadLineValueInterface readline) {
        if (wb == null)
            return true;

        // 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
        Sheet[] sheet = wb.getSheets();

        if (sheet != null && sheet.length > 0) {
            // 对每个工作表进行循环
            for (int i = 0; i < sheet.length; i++) {
                // 得到当前工作表的行数
                int rowNum = sheet[i].getRows();
                for (int j = 0; j < rowNum; j++) {
                    // 得到当前行的所有单元格
                    Cell[] cells = sheet[i].getRow(j);
                    if (cells != null && cells.length > 0) {
                        // 对每个单元格进行循环
                        String[] str = new String[cells.length];
                        for (int k = 0; k < cells.length; k++) {
                            // 读取当前单元格的值
                            str[k] = cells[k].getContents();
                        }
                        // 调用接口处理
                        if (!readline.readLineValue(str,j,sheet[i].getName())){
                            break;
                        }
                    }
                }
            }
        }
        // 最后关闭资源，释放内存
        wb.close();
        return false;
    }

    public static void writeExcel(String fileName,
                                  WriteLineValueInterface writeLine) throws Exception {
        WritableWorkbook wwb = null;
        // 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
        wwb = Workbook.createWorkbook(new File(fileName));
        if (wwb != null) {
            // 创建一个可写入的工作表
            // Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
            WritableSheet ws = wwb.createSheet("sheet1", 0);

            writeLine.writeHead(ws);
            // 下面开始添加单元格
            for (int i = 0; i < writeLine.getLinenum(); i++) {
                writeLine.writeLine(ws, i);
            }
            // 从内存中写入文件中
            wwb.write();
            // 关闭资源，释放内存
            wwb.close();
        }
    }
}
