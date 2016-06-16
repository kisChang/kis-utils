package com.kischang.simple_utils.execute.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 流操作工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class StreamUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int EOF = -1;

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(InputStream input, OutputStream output, int bufferSize)
            throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static Thread threadCopy(InputStream input, OutputStream output){
        Thread thread = new Thread(new InputStreamPumper(input, output));
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

}
