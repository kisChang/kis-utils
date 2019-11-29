package com.kischang.simple_utils.log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 支持缓存和定义Handler的日志追加器
 *
 * @author KisChang
 * @date 2017-05-22
 */
public class LogHandlerAndCacheAppender extends AppenderSkeleton {

    private static final BlockingQueue<String> queue = new LinkedBlockingDeque<String>();
    private static int maxLogSize = 50;
    private static AppendHandler appendHandler;

    @Override
    protected void append(LoggingEvent loggingEvent) {
        String str = super.layout.format(loggingEvent);
        try {
            if (appendHandler != null){
                appendHandler.handler(str);
            }
            queue.put(str);
            while (queue.size() > maxLogSize){
                queue.take();
            }
        } catch (InterruptedException ignored) {}
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    public static List<String> getQueue() {
        return new ArrayList<>(queue);
    }

    public static void setAppendHandler(AppendHandler param) {
        appendHandler = param;
    }

    public static void setMaxLogSize(int maxLogSize) {
        LogHandlerAndCacheAppender.maxLogSize = maxLogSize;
    }
}
