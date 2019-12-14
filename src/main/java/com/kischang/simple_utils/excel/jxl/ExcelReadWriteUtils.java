package com.kischang.simple_utils.excel.jxl;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel 读取写入工具
 *
 * @author KisChang
 * @version 1.0
 */
public class ExcelReadWriteUtils {
    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //支持的列表
    public static final Map<String, Boolean> SUPPORTLIST = new HashMap<String, Boolean>() {{
        put("xls", TYPE_XLS);
        put("xlsx", TYPE_XLSX);
    }};

    public static final boolean TYPE_XLS = false;
    public static final boolean TYPE_XLSX = true;

    //读取excel
    public static org.apache.poi.ss.usermodel.Workbook toWb(InputStream is, boolean type){
        try {
            if(type == TYPE_XLSX){
                /*2007  xlsx*/
                return new XSSFWorkbook(is);
            }else {
                /*2001  xls*/
                return new HSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean readExcel(InputStream inputStream, boolean excelFileType, ReadLineValueInterface readline) {
        return readExcel(inputStream, excelFileType, false, readline);
    }
    public static boolean readExcel(InputStream inputStream, boolean excelFileType, boolean dateTimeType, ReadLineValueInterface readline) {
        return realReadExcel(toWb(inputStream, excelFileType), dateTimeType, readline);
    }

    private static boolean realReadExcel(org.apache.poi.ss.usermodel.Workbook wb, boolean dateTimeType, ReadLineValueInterface readline) {
        if (wb == null)
            return true;
        // 对每个工作表进行循环
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = wb.getSheetAt(sheetIndex);

            // 得到当前工作表的行数
            int rowNum = sheet.getLastRowNum();
            for (int rowIndex = 0; rowIndex <= rowNum; rowIndex++) {
                // 得到当前行的所有单元格
                Row cells = sheet.getRow(rowIndex);
                // 对每个单元格进行循环
                List<String> strList = new LinkedList<>();
                for (int cellIndex = 0; cellIndex < cells.getLastCellNum(); cellIndex++) {
                    // 读取当前单元格的值
                    Cell cell = cells.getCell(cellIndex);
                    String onceStr = null;
                    if (cell.getCellTypeEnum() == CellType.NUMERIC){
                        if (DateUtil.isCellDateFormatted(cell)){
                            //时间类型
                            double date = DateUtil.getExcelDate(cell.getDateCellValue());
                            Date javaDate = DateUtil.getJavaDate(date);
                            if (dateTimeType){
                                onceStr = datetimeFormat.format(javaDate);
                            }else {
                                onceStr = dateFormat.format(javaDate);
                            }
                        }else {
                            //同样转换成字符串类型读取
                            cell.setCellType(CellType.STRING);
                            onceStr = cell.toString();
                        }
                    }else {
                        cell.setCellType(CellType.STRING);
                        onceStr = cell.toString();
                    }
                    if (onceStr != null){
                        if ("".equals(onceStr)){
                            if (!strList.isEmpty()){
                                strList.add(onceStr);
                            }
                        }else {
                            strList.add(onceStr);
                        }
                    }
                }
                // 调用接口处理
                if (!readline.readLineValue(strList.toArray(new String[]{}), rowIndex, sheet.getSheetName())) {
                    break;
                }
            }
        }
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
