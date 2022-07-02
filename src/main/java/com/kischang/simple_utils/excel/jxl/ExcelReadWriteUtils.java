package com.kischang.simple_utils.excel.jxl;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Excel 读取写入工具
 *
 * @author KisChang
 * @version 1.0
 */
public class ExcelReadWriteUtils {
    private static SimpleDateFormat datetimeFormat(){
        return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    private static SimpleDateFormat dateFormat(){
        return  new SimpleDateFormat("yyyy-MM-dd");
    }

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

    public static org.apache.poi.ss.usermodel.Workbook toWb(File is){
        try {
            return WorkbookFactory.create(is, null, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean readExcel(InputStream inputStream, boolean excelFileType, com.kischang.simple_utils.excel.jxl.ReadLineValueInterface readline) {
        return readExcel(inputStream, excelFileType, false, readline);
    }
    public static boolean readExcel(InputStream inputStream, boolean excelFileType, boolean dateTimeType, com.kischang.simple_utils.excel.jxl.ReadLineValueInterface readline) {
        return realReadExcel(toWb(inputStream, excelFileType), dateTimeType, readline);
    }

    public static List<Map<String, String>> readExcelToMap(InputStream in, boolean excelFileType, Integer sheetIndex, String sheetName){
        return readExcelToMap(toWb(in, excelFileType), true, sheetIndex, sheetName);
    }
    public static List<Map<String, String>> readExcelToMap(File file, Integer sheetIndex, String sheetName){
        return readExcelToMap(toWb(file), true, sheetIndex, sheetName);
    }
    public static List<Map<String, String>> readExcelToMap(org.apache.poi.ss.usermodel.Workbook wb, boolean dateTimeType, Integer sheetIndex, String sheetName){
        List<Map<String, String>> listMap = new LinkedList<>();
        Map<Integer, String> colsName = new HashMap<>();
        Sheet readOnceSheet = null;
        if (sheetIndex != null){
            readOnceSheet = wb.getSheetAt(sheetIndex);
            if (readOnceSheet == null){
                throw new IllegalArgumentException("sheetIndex:" + sheetIndex + " not found!");
            }
        }
        if (sheetName != null){
            readOnceSheet = wb.getSheet(sheetName);
            if (readOnceSheet == null){
                throw new IllegalArgumentException("sheetName:" + sheetName + " not found!");
            }
        }
        ReadLineValueInterface impl = (val, rowIndex, nowSheetName) -> {
            //正式代码
            if (rowIndex == 0){ //这行是第一行
                for (int i = 0; i < val.length; i++) {
                    colsName.put(i, val[i]);
                }
                return true;
            }else {
                Map<String, String> once = new HashMap<>();
                for (int i = 0; i < val.length; i++) {
                    String colName = colsName.getOrDefault(i, "未定义_" + i);
                    once.put(colName, val[i]);
                }
                listMap.add(once);
            }
            /*读到没有数据了*/
            return val.length > 0;
        };
        if (readOnceSheet != null){
            //只读一个表
            realReadExcelSheet(wb, readOnceSheet, dateTimeType, impl);
        }else {
            realReadExcel(wb, dateTimeType, impl);
        }
        return listMap;
    }

    public static boolean readExcel(File file, com.kischang.simple_utils.excel.jxl.ReadLineValueInterface readline) {
        return readExcel(file, false, readline);
    }
    public static boolean readExcel(File file, boolean dateTimeType, com.kischang.simple_utils.excel.jxl.ReadLineValueInterface readline) {
        return realReadExcel(toWb(file), dateTimeType, readline);
    }

    private static boolean realReadExcel(org.apache.poi.ss.usermodel.Workbook wb, boolean dateTimeType, ReadLineValueInterface readline) {
        if (wb == null)
            return true;
        // 对每个工作表进行循环
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = wb.getSheetAt(sheetIndex);
            realReadExcelSheet(wb, sheet, dateTimeType, readline);
        }
        return false;
    }

    private static void realReadExcelSheet(org.apache.poi.ss.usermodel.Workbook wb, Sheet sheet, boolean dateTimeType, ReadLineValueInterface readline) {
        // 得到当前工作表的行数
        int rowNum = sheet.getLastRowNum();
        for (int rowIndex = 0; rowIndex <= rowNum; rowIndex++) {
            // 得到当前行的所有单元格
            Row cells = sheet.getRow(rowIndex);
            // 对每个单元格进行循环
            List<String> strList = new LinkedList<>();
            for (int cellIndex = 0; cellIndex < cells.getLastCellNum(); cellIndex++) {
                String onceStr = null;
                // 读取当前单元格的值
                Cell cell = cells.getCell(cellIndex);
                if (cell == null){ //空的单元格就跳过了
                    break;
                }
                boolean isMerge = isMergedRegion(sheet, rowIndex, cell.getColumnIndex());
                if(isMerge) {//判断是否具有合并单元格
                    onceStr = getMergedRegionValue(sheet, rowIndex, cell.getColumnIndex());
                }else {
                    onceStr = getCellValue(cell, dateTimeType);
                }
                if ("".equals(onceStr)){
                    if (!strList.isEmpty()){
                        strList.add(onceStr);
                    }
                }else {
                    strList.add(onceStr);
                }
            }
            // 调用接口处理
            if (!readline.readLineValue(strList.toArray(new String[]{}), rowIndex, sheet.getSheetName())) {
                break;
            }
        }
    }

    public static String getCellValue(Cell cell) {
        return getCellValue(cell, true);
    }
    public static String getCellValue(Cell cell, boolean dateTimeType) {
        if (cell == null) return "";

        if (cell.getCellTypeEnum() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.FORMULA) { /*公式*/
            return cell.getCellFormula();
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)){
                //时间类型
                double date = DateUtil.getExcelDate(cell.getDateCellValue());
                Date javaDate = DateUtil.getJavaDate(date);
                if (dateTimeType){
                    return datetimeFormat().format(javaDate);
                }else {
                    return dateFormat().format(javaDate);
                }
            }else {
                return String.valueOf(cell.getNumericCellValue());
            }
        }
        return "";
    }


    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet ,int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();
        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }
        return null ;
    }

    /**
     * 判断合并了行 (仅限横向合并)
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private static boolean isMergedRow(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row == firstRow && row == lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定的单元格是否是合并的区域
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private static boolean isMergedRegion(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
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
