package com.kischang.simple_utils.excel.jxl;

import jxl.write.WritableSheet;

public interface WriteLineValueInterface {

    int getLinenum();

    boolean writeLine(WritableSheet ws, int line) throws Exception;

    boolean writeHead(WritableSheet ws) throws Exception;

}
