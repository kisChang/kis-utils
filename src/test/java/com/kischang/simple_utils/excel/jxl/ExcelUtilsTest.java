package com.kischang.simple_utils.excel.jxl;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author KisChang
 * @date 2020-12-24
 */
public class ExcelUtilsTest {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\KisChang\\Desktop\\001.xls";
        List<Map<String, String>> listMap = ExcelReadWriteUtils.readExcelToMap(new File(filePath), null, "判断题");
        listMap.forEach(System.out::println);
    }

}
