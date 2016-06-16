package com.kischang.simple_utils.execute;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 命令执行 输入流操作
 *
 * @author KisChang
 * @version 1.0
 */
public interface ExecuteInputStreamHandler {

    void handlerProcessInputStream(OutputStream os) throws IOException;

}
