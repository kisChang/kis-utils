package com.kischang.simple_utils.execute.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copies all data from an input stream to an output stream.
 */
public class StreamPumper implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(StreamPumper.class);

    /** the default size of the internal buffer for copying the streams */
    private static final int DEFAULT_SIZE = 1024 * 4;

    /** the input stream to pump from */
    private final InputStream is;

    /** the output stream to pmp into */
    private final OutputStream os;

    /** the size of the internal buffer for copying the streams */ 
    private final int size;

    /** was the end of the stream reached */
    private boolean finished;

    /** close the output stream when exhausted */
    private final boolean closeWhenExhausted;
    
    /**
     * Create a new stream pumper.
     * 
     * @param is input stream to read data from
     * @param os output stream to write data to.
     * @param closeWhenExhausted if true, the output stream will be closed when the input is exhausted.
     */
    public StreamPumper(final InputStream is, final OutputStream os,
            final boolean closeWhenExhausted) {
        this.is = is;
        this.os = os;
        this.size = DEFAULT_SIZE;
        this.closeWhenExhausted = closeWhenExhausted;
    }

    /**
     * Create a new stream pumper.
     *
     * @param is input stream to read data from
     * @param os output stream to write data to.
     * @param closeWhenExhausted if true, the output stream will be closed when the input is exhausted.
     * @param size the size of the internal buffer for copying the streams
     */
    public StreamPumper(final InputStream is, final OutputStream os,
            final boolean closeWhenExhausted, final int size) {
        this.is = is;
        this.os = os;
        this.size = size > 0 ? size : DEFAULT_SIZE;
        this.closeWhenExhausted = closeWhenExhausted;
    }

    /**
     * Create a new stream pumper.
     * 
     * @param is input stream to read data from
     * @param os output stream to write data to.
     */
    public StreamPumper(final InputStream is, final OutputStream os) {
        this(is, os, false);
    }

    /**
     * Copies data from the input stream to the output stream. Terminates as
     * soon as the input stream is closed or an error occurs.
     */
    public void run() {
        synchronized (this) {
            finished = false;
        }

        try {
            StreamUtils.copyLarge(is, os, this.size);
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            // nothing to do - happens quite often with watchdog
        } finally {
            if (closeWhenExhausted) {
                try {
                    os.close();
                } catch (final IOException e) {
                    final String msg = "Got exception while closing exhausted output stream";
                    logger.error(msg, e);
                }
            }
            synchronized (this) {
                finished = true;
                notifyAll();
            }
        }
    }

    /**
     * Tells whether the end of the stream has been reached.
     * 
     * @return true is the stream has been exhausted.
     */
    public synchronized boolean isFinished() {
        return finished;
    }

    /**
     * This method blocks until the stream pumper finishes.
     * 
     * @exception InterruptedException
     *                if any thread interrupted the current thread before or while the current thread was waiting for a
     *                notification.
     * @see #isFinished()
     */
    public synchronized void waitFor() throws InterruptedException {
        while (!isFinished()) {
            wait();
        }
    }
}
